package codepath.com.goingout;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import codepath.com.goingout.adapters.FeedAdapter;
import codepath.com.goingout.models.Event;
import codepath.com.goingout.models.Feeds;
import cz.msebera.android.httpclient.Header;

public class EventListActivity extends AppCompatActivity {
    private RecyclerView rvFeeds;
    private FeedAdapter adapter;
    // Rafael!!
    private List<Feeds> events2;

    // the base URL for the API
    public final static String API_BASE_URL = "http://api.eventful.com/json/events/search?/app_key=";
    // the parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // tag for logging from this activity
    public final static String TAG = "EventListActivity";
    // image config
    // Config config;

    // instance fields
    AsyncHttpClient client;
    // the list of events
    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        //initialize the client
        client = new AsyncHttpClient();

        // Bind recycler view to adapter
        rvFeeds = (RecyclerView) findViewById(R.id.rvFeeds);

        // allows for optimizations
        rvFeeds.setHasFixedSize(true);

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(EventListActivity.this, 1);

        // Unlike ListView, you have to explicitly give a LayoutManager to the RecyclerView to position items on the screen.
        // There are three LayoutManager provided at the moment: GridLayoutManager, StaggeredGridLayoutManager and LinearLayoutManager.
        rvFeeds.setLayoutManager(layout);

        // get data
        events2 = Feeds.getFeeds();

        // Create an adapter
        adapter = new FeedAdapter(EventListActivity.this, events2);

        // Bind adapter to list
        rvFeeds.setAdapter(adapter);

        // get the configuration on app creation
        //getConfiguration();
    }

    // get the list of nearby events according to preferences
    private void getEvents() {
        // create the url
        String url = API_BASE_URL;
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API key, always required
        // execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // load the results into movies list
                try {
                    JSONArray results = response.getJSONArray("event");
                    // iterate through result set and create Movie objects
                    for (int i = 0; i < results.length(); i++) {
                        Event event = new Event(results.getJSONObject(i));
                        events.add(event);
                        // notify adapter that a row was added
                        adapter.notifyItemInserted(events.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now_playing endpoint", throwable, true);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    // hangle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser) {
        // always log the error
        Log.e(TAG, message, error);
        // alert the user to avoid silent errors
        if (alertUser) {
            // show a long toast with the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
