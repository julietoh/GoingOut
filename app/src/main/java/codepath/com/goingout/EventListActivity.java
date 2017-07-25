package codepath.com.goingout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

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
    ArrayList<String> filter;
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

    EventfulClient client;

    // instance fields
//    AsyncHttpClient client;
    // the list of events
    ArrayList<Event> events;
    Toolbar FeedToolbar;
    ImageButton ibFilter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        filter = getIntent().getStringArrayListExtra("preferences");

        //initialize the client
//        client = new AsyncHttpClient();
        client = EventfulApp.getRestClient();

        //initialize the list of movies
        events = new ArrayList<>();
        //initialize the adapter -- movies array list cannot be reinitialized after this point
        adapter = new FeedAdapter(events, this);

        //resolve the recycler view and connect a layout manager and the adapter
        rvFeeds = (RecyclerView) findViewById(R.id.rvFeeds);
        rvFeeds.setLayoutManager(new LinearLayoutManager(this));
        rvFeeds.setAdapter(adapter);
        getFeed();

        FeedToolbar = (Toolbar) findViewById(R.id.FeedToolbar);
//        FeedToolbar.setTitle(filter.get(0)+" filter applied!");

        ibFilter = (ImageButton) findViewById(R.id.ibFilter);

        //TODO eventually change to filter activity!
        ibFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventListActivity.this, PreferenceActivity.class);
                startActivity(intent);
            }
        });


//        String list = getFilterList(filter);
//        Toast.makeText(this, "There are "+filter.size()+" filters you chose", Toast.LENGTH_LONG).show();


    }

//    // get the list of nearby events according to preferences
//    private void getEvents() {
//        // create the url
//        String url = API_BASE_URL;
//        // set the request parameters
//        RequestParams params = new RequestParams();
//        params.put(APP_KEY_PARAM, "8KFwLj3XshfZCdLP"); // API key, always required
//        params.put("page_size", 25);
//        params.put("q", filter.get(0));
//        params.put(LOCATION_PARAM, "San Francisco");
//        // execute a GET request expecting a JSON object response
//        client.get(url, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                // load the results into movies list
//                try {
//                    JSONObject events1 = response.getJSONObject("events");
//                    JSONArray results = events1.getJSONArray("event");
//                    // iterate through result set and create Movie objects
//                    for (int i = 0; i < results.length()-1; i++) {
//                        Event event = new Event(results.getJSONObject(i));
//                        events.add(event);
//                        // notify adapter that a row was added
//                        adapter.notifyItemInserted(events.size() - 1);
//                    }
//                    Log.i(TAG, String.format("Loaded %s events", results.length()));
//                } catch (JSONException e) {
//                    logError("Failed to parse events", e, true);
//                }
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                logError("Failed to get data from endpoint", throwable, true);
//            }
//        });
//    }



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

    public void getFeed() {
        client.getEvents(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Event event = null;
                try {
                    for (int i = 0; i<response.length(); i++)
                    {
                        event = Event.fromJSON(response.getJSONObject(i));
                        events.add(event);
                        adapter.notifyItemInserted(events.size() - 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }
        });
    }
}
