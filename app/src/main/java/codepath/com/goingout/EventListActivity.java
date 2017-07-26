package codepath.com.goingout;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
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
    ArrayList<String> filter;

    // the base URL for the API
    public final static String API_BASE_URL = "http://api.eventful.com/json/events/search?";
    // the parameter name for the API key
    public final static String APP_KEY_PARAM = "app_key";
    public final static String LOCATION_PARAM = "location";
    // tag for logging from this activity
    public final static String TAG = "EventListActivity";
    // image config
    // Config config;

//    EventfulClient client;

    // instance fields
    AsyncHttpClient client;
    // the list of events
    ArrayList<Event> events;

    ImageButton ibFilter;
    Toolbar toolbar;
    DrawerLayout mDrawer;
    NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        filter = getIntent().getStringArrayListExtra("preferences");

        //initialize the client
        client = new AsyncHttpClient();
//        client = EventfulApp.getRestClient();

        //initialize the list of movies
        events = new ArrayList<>();
        //initialize the adapter -- movies array list cannot be reinitialized after this point
        adapter = new FeedAdapter(events, this);

        //resolve the recycler view and connect a layout manager and the adapter
        rvFeeds = (RecyclerView) findViewById(R.id.rvFeeds);
        rvFeeds.setLayoutManager(new LinearLayoutManager(this));
        rvFeeds.setAdapter(adapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(filter.get(0)+" filter applied!");
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
        rvFeeds.bringToFront();

//        ibFilter = (ImageButton) findViewById(R.id.ibFilter);

        //TODO eventually change to filter activity!
//        ibFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(EventListActivity.this, PreferenceActivity.class);
//                startActivity(intent);
//            }
//        });

        getEvents();


//        String list = getFilterList(filter);
        Toast.makeText(this, "There are "+filter.size()+" filters you chose", Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = EventListActivity.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = EventListActivity.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = EventListActivity.class;
                break;
            default:
                fragmentClass = EventListActivity.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }



    //     get the list of nearby events according to preferences
    private void getEvents() {
        // create the url
        String url = API_BASE_URL;
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(APP_KEY_PARAM, "8KFwLj3XshfZCdLP"); // API key, always required
        params.put("page_size", 25);
        params.put("category", getFilterList(filter));
        params.put("sort_order", "popularity");
        params.put("date","This Week");
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



        private String getFilterList(ArrayList<String> filter){
        String string = "";
        for(int i = 0; i< filter.size()-1; i++)
        {
            String item = filter.get(i);
            item = item.toLowerCase();
            if (item.equals("activism")) {
                item = "politics_activism";
            }else if (item.equals("education")){
                item = "learning_education";
            }else if (item.equals("family")){
                item ="family_fun_kids";
            }else if (item.equals("nightlife")){
                item = "singles_social";
            }else if (item.equals("theater")){
                item = "performing_arts";
            }else if (item.equals("recreation")){
                item = "outdoors_recreation";
            }else if (item.equals("religion")){
                item = "religion_spirituality";
            }else if (item.equals("tech")) {
                item = "technology";
            }else if (item.equals("tech")) {
                item = "technology";
            }
                string = string + item + ",";
        }

        return string;
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
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);

    }


    // handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser) {
        // always log the error
        Log.e(TAG, message, error);
        // alert the user to avoid silent errors
        if (alertUser) {
            // show a long toast with the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

//    public void getFeed() {
//        client.getEvents(new JsonHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.d("TwitterClient", response.toString());
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                Event event = null;
//                try {
//                    for (int i = 0; i<response.length(); i++)
//                    {
//                        event = Event.fromJSON(response.getJSONObject(i));
//                        events.add(event);
//                        adapter.notifyItemInserted(events.size() - 1);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                Log.d("TwitterClient", errorResponse.toString());
//                throwable.printStackTrace();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("TwitterClient", errorResponse.toString());
//                throwable.printStackTrace();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                Log.d("TwitterClient", responseString);
//                throwable.printStackTrace();
//            }
//        });
//    }
}
