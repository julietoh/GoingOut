package codepath.com.goingout;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codepath.com.goingout.adapters.FeedAdapter;
import codepath.com.goingout.models.Event;
import codepath.com.goingout.models.User;
import cz.msebera.android.httpclient.Header;

public class EventListActivity extends AppCompatActivity{
    public static final int REQUEST_CODE = 20;

    private RecyclerView rvFeeds;
    private FeedAdapter adapter;
    ArrayList<String> filter;
    ArrayList<String> newFilter;

    public ArrayList<String> categoryFilter;
    public String locationFilter = "San Francisco";
    boolean locationChanged = false;
    public int priceFilter = 4;
    boolean priceChanged = false;
    public int ratingFilter;
    boolean ratingChanged = false;
    private String dateFilter = "This Week";
    boolean dateChanged = false;



    // the base URL for the API
    public final static String API_BASE_URL = "http://api.eventful.com/json/events/search?";
    // the parameter name for the API key
    public final static String APP_KEY_PARAM = "app_key";
    public final static String LOCATION_PARAM = "location";
    // tag for logging from this activity
    public final static String TAG = "EventListActivity";

    // instance fields
    AsyncHttpClient client;
    // the list of events
    ArrayList<Event> events;


    ImageButton ibFilter;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    ExpandableListAdapter adapterEx;
    ExpandableListView expandableList;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ImageButton ibAddEvent;
    public static User currentUser;




    GoogleClient googleClient;
    public static int[] images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        filter = getIntent().getStringArrayListExtra("preferences");
        currentUser = Parcels.unwrap(getIntent().getParcelableExtra("current_user"));


        //initialize the client
        client = new AsyncHttpClient();

        googleClient = new GoogleClient();

        // googleClient = new GoogleClient();


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
        toolbar.setTitleTextColor(getResources().getColor(R.color.babyWhite));
        ibAddEvent = (ImageButton) findViewById(R.id.ibAddEvent);
//        toolbar.setTitle(filter.get(0)+" filter applied!");
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nvView);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                if (categoryFilter.size()>0 || priceChanged || locationChanged || dateChanged || ratingChanged){
                    adapter.clear();
                    getEvents(categoryFilter, dateFilter, locationFilter, priceFilter);
                    categoryFilter.clear();
                    priceChanged = false;
                    locationChanged = false;
                    dateChanged = false;
                    ratingChanged = false;
                }


            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();


        rvFeeds.bringToFront();
        toolbar.setNavigationIcon(R.drawable.filter);

        expandableList= (ExpandableListView) findViewById(R.id.navigationmenu);
        prepareListData();
        adapterEx = new codepath.com.goingout.adapters.ExpandableListAdapter(this, listDataHeader,   listDataChild, expandableList);
        expandableList.setAdapter(adapterEx);

        categoryFilter = new ArrayList<>();

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String item = (String) adapterEx.getChild(groupPosition, childPosition);
                if (groupPosition == 0){
//                    if (v.isSelected()){
//                        v.setBackgroundResource(R.drawable.filter_selector);
//                    } else{
//                        v.setBackgroundResource(R.drawable.filter_selector);
//                    }
                    if (!categoryFilter.contains(item)){
                        categoryFilter.add(translate(item));
                        Toast.makeText(getApplicationContext(), item+" added.", Toast.LENGTH_SHORT).show();
                    } else {
                        categoryFilter.remove(translate(item));
                        Toast.makeText(getApplicationContext(), item+" removed.", Toast.LENGTH_SHORT).show();
                    }

                }else if (groupPosition == 1){
                    dateFilter = (String) adapterEx.getChild(groupPosition, childPosition);
                    Toast.makeText(getApplicationContext(),
                            "Events shown are from "+((String) adapterEx.getChild(groupPosition, childPosition)).toLowerCase(),
                            Toast.LENGTH_SHORT).show();
                    dateChanged = true;
                }else if (groupPosition == 2){
                    priceFilter = (int) ((String) adapterEx.getChild(groupPosition, childPosition)).length();
                    //TODO PRICE AND RATING FILTER!! Pos 2&3
                    Toast.makeText(getApplicationContext(),
                            (String) adapterEx.getChild(groupPosition, childPosition),
                            Toast.LENGTH_SHORT).show();
                    priceChanged = true;
                }else if (groupPosition == 3){
//                    if (childPosition==0)
                    ratingFilter = Integer.parseInt(((String) adapterEx.getChild(groupPosition, childPosition)).substring(0,1));
                    Toast.makeText(getApplicationContext(),
                            (String) adapterEx.getChild(groupPosition, childPosition),
                            Toast.LENGTH_SHORT).show();
                    ratingChanged = true;
                }else if (groupPosition == 4){
                    locationFilter = (String) adapterEx.getChild(groupPosition, childPosition);
                    Toast.makeText(getApplicationContext(),
                            "Events in " + adapterEx.getChild(groupPosition, childPosition)+ " will be displayed",
                            Toast.LENGTH_SHORT).show();
                    locationChanged = true;
                }
                //Nothing here ever fires
                return true;
            }
        });


        ibAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventListActivity.this, AddEventActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
            }
        });

        getEvents(filter, dateFilter, locationFilter, priceFilter);

        Toast.makeText(this, "There are "+filter.size()+" filters you chose", Toast.LENGTH_LONG).show();


        newFilter = new ArrayList<>();
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding data header
        listDataHeader.add("Categories");
        listDataHeader.add("Date");
        listDataHeader.add("Price");
        listDataHeader.add("Rating");
        listDataHeader.add("Location");

        // Adding child data
        List<String> categories= new ArrayList<String>();
        for (EventType eventType : EventType.values())
        {
            String name = eventType.toString();
            categories.add(name);

        }

        List<String> date= new ArrayList<String>();
        date.add("Today");
        date.add("Last Week");
        date.add("This Week");
        date.add("Next Week");

        List<String> price= new ArrayList<String>();
        price.add("$");
        price.add("$$");
        price.add("$$$");
        price.add("$$$$");

        List<String> ratings= new ArrayList<String>();
        ratings.add("1 Star");
        ratings.add("2 Star");
        ratings.add("3 Star");
        ratings.add("4 Star");
        ratings.add("5 Star");

        List<String> location= new ArrayList<String>();
        location.add("San Francisco");
        location.add("New York");
        location.add("Boston");
        location.add("Seattle");



        listDataChild.put(listDataHeader.get(0), categories);// Header, Child data
        listDataChild.put(listDataHeader.get(1), date);
        listDataChild.put(listDataHeader.get(2), price);
        listDataChild.put(listDataHeader.get(3), ratings);
        listDataChild.put(listDataHeader.get(4), location);

        images= new int[]{R.drawable.filter,
                R.drawable.ic_calendar,
                R.drawable.ic_price,
                R.drawable.ic_ratings,
                R.drawable.ic_location};

    }

    @Override
    public void onBackPressed()
    {
        filter.clear();
        Intent intent = new Intent(this,PreferenceActivity.class);
        startActivity(intent);
    }


//    private void setupDrawerContent(NavigationView navigationView) {
//        navigationView.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        menuItem.setChecked(true);
//                        selectDrawerItem(menuItem);
//                        return true;
//                    }
//                });
//    }

    public String translate(String item) {
        item = item.toLowerCase();
        switch (item) {
            case "activism":
                item = "politics_activism";
                break;
            case "education":
                item = "learning_education";
                break;
            case "film":
                item = "movies_film";
                break;
            case "family":
                item = "family_fun_kids";
                break;
            case "nightlife":
                item = "singles_social";
                break;
            case "theater":
                item = "performing_arts";
                break;
            case "recreation":
                item = "outdoors_recreation";
                break;
            case "religion":
                item = "religion_spirituality";
                break;
            case "tech":
                item = "technology";
                break;
        }
        return item;
    }




    //     get the list of nearby events according to preferences
    private void getEvents(ArrayList categoryList, String date, String location, final int price) {
        // create the url
        String url = API_BASE_URL;
        // set the request parameters

        //final GooglePlaces clientelle = new GooglePlaces("AIzaSyCPa7WzZjkYiq1qRofuqSBJIt6G1xvEtJA");

        RequestParams params = new RequestParams();
        params.put(APP_KEY_PARAM, "8KFwLj3XshfZCdLP"); // API key, always required
        params.put("page_size", 5);
        params.put("category", getFilterList(categoryList));
        params.put("sort_order", "popularity");
        params.put("date",date);
        params.put(LOCATION_PARAM, location);
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

                        //List<Place> places = clientelle.getPlacesByQuery("Empire State Building", GooglePlaces.MAXIMUM_RESULTS);
                        //Place place = places.get(0);

                        googleClient.getInfo(event, adapter);



                        events.add(event);
                        adapter.notifyItemInserted(events.size() - 1);


//                        event.setVenue(googleClient.getInfo(event));
                        // notify adapter that a row was added
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
        for(int i = 0; i< filter.size(); i++)
        {
            String item = filter.get(i);

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
                drawerLayout.openDrawer(GravityCompat.START);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            // Extract name value from result extras
            Event event = Parcels.unwrap(data.getParcelableExtra("event"));

            events.add(0, event);
            adapter.notifyItemInserted(0);
            rvFeeds.getLayoutManager().scrollToPosition(0);

            // Toast the name to display temporarily on screen
            Toast.makeText(this, "Event Posted!", Toast.LENGTH_SHORT).show();
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
