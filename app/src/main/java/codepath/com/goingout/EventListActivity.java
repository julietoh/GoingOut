package codepath.com.goingout;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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

import codepath.com.goingout.adapters.FeedAdapter;
import codepath.com.goingout.models.Event;
import cz.msebera.android.httpclient.Header;

public class EventListActivity extends AppCompatActivity {
    private RecyclerView rvFeeds;
    private FeedAdapter adapter;
    // Rafael!!
//    private List<Event> events2;

    // the base URL for the API
    public final static String API_BASE_URL = "http://api.eventful.com/json/events/search?";
    // the parameter name for the API key
    public final static String APP_KEY_PARAM = "app_key";
    public final static String LOCATION_PARAM = "location";
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
        //initialize the list of movies
        events = new ArrayList<>();
        //initialize the adapter -- movies array list cannot be reinitialized after this point
        adapter = new FeedAdapter(events);

        //resolve the recycler view and connect a layout manager and the adapter
        rvFeeds = (RecyclerView) findViewById(R.id.rvFeeds);
        rvFeeds.setLayoutManager(new LinearLayoutManager(this));
        rvFeeds.setAdapter(adapter);
        getEvents();

        ArrayList<String> filter = getIntent().getStringArrayListExtra("preferences");
//        String list = getFilterList(filter);
        Toast.makeText(this, "There are "+filter.size()+" filters you chose", Toast.LENGTH_LONG).show();


    }


    // get the list of nearby events according to preferences
    private void getEvents() {
        // create the url
        String url = API_BASE_URL;
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(APP_KEY_PARAM, "8KFwLj3XshfZCdLP"); // API key, always required
        params.put("page_size", 25);
        params.put(LOCATION_PARAM, "San Francisco");
        // execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // load the results into movies list
                try {
                    JSONObject events1 = response.getJSONObject("events");
                    JSONArray results = events1.getJSONArray("event");
                    // iterate through result set and create Movie objects
                    for (int i = 0; i < results.length()-1; i++) {
                        Event event = new Event(results.getJSONObject(i));
                        events.add(event);
                        // notify adapter that a row was added
                        adapter.notifyItemInserted(events.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s events", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse events", e, true);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from endpoint", throwable, true);
            }
        });
    }


//    private String getFilterList(ArrayList<String> filter){
//        String string = "";
//        for(int i = 0; i< filter.size()-1; i++)
//        {
//            String item = filter.get(i);
//            string = string + item + " ";
//        }
//
//        return string;
//    }

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
