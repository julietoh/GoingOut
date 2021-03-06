package codepath.com.goingout;

import android.util.Log;
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
import codepath.com.goingout.models.Venue;
import cz.msebera.android.httpclient.Header;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by rafelix on 7/24/17.
 */

public class GoogleClient {
    public final static String API_PHOTO_SEARCH_BASE_URL = "https://maps.googleapis.com/maps/api/place/photo?";
    public final static String API_TEXT_SEARCH_BASE_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
    public final static String API_DISTANCEMATRIX_BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial";
    public final static String ORIGIN_PARAM = "origins";
    public final static String DESTINATION_PARAM = "destinations";
    // the parameter name for the API key
    public final static String APP_KEY_PARAM = "key";
    public final static String PHOTO_SEARCH_PARAM = "maxwidth=400&photoreference";
    public final static String TEXT_SEARCH_PARAM = "query";
    // tag for logging from this activity
    public final static String TAG = "EventListActivity";
    // image config
    // Config config;

    // instance fields
    public AsyncHttpClient client = new AsyncHttpClient();
    public AsyncHttpClient mapsClient = new AsyncHttpClient();


    //GooglePlaces clientelle = new GooglePlaces("AIzaSyCPa7WzZjkYiq1qRofuqSBJIt6G1xvEtJA");
    //List<Place> places = clientelle.getPlacesByQuery("Empire State Building", GooglePlaces.MAXIMUM_RESULTS);
    // the list of events
    private ArrayList<Venue> venues = new ArrayList<>();

    //public Venue
    public void getInfo(final Event event, final FeedAdapter adapter) {
        // create the url
        String url = API_TEXT_SEARCH_BASE_URL;
        String photoUrl = API_PHOTO_SEARCH_BASE_URL;
        //RequestParams photoparams = new RequestParams();
        //photoparams.put("photoreference", event.venue.getPhotoRef());
        //photoparams.put(APP_KEY_PARAM, "AIzaSyCvETKKM_AixmtWCUtdb3jM6v-KGOTahGA");
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(TEXT_SEARCH_PARAM, event.getPlace() + " " + event.getCity());


        params.put(APP_KEY_PARAM, "AIzaSyB_vvM5MytC0iauPGC9Hz5QCYTQ-B9M7KQ");





        // execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // load the results into movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    Log.e(TAG, results.toString());

                    // iterate through result set and create Movie objects
//                    if (results.length() > 0) {
                    Venue venue = new Venue(results.getJSONObject(0));
                    Log.e(TAG, venue.toString());

                    event.setVenue(venue);
//                        event.setVenue(venue);
//                    }
                    getDistanceAway(event, adapter);
                } catch (JSONException e) {
                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from endpoint", throwable, true);
            }
        });
//        if (venues.size() > 0) {
//            return venues.get(0);
//        } else {
//            return null;
//        }

    }

    //public Venue
    public void getDistanceAway(final Event event, final FeedAdapter adapter) {
        // create the url

            String url = API_DISTANCEMATRIX_BASE_URL;
            RequestParams params = new RequestParams();
            // origin is set to Facebook HQ's place ID
            params.put(ORIGIN_PARAM, "place_id:ChIJZa6ezJa8j4AR1p1nTSaRtuQ");
        // non user event with venue
        if (event.getImage() == null) {
            params.put(DESTINATION_PARAM, "place_id:" + event.getVenue().getPlaceId());
        } else {
            // user event without venue
            params.put(DESTINATION_PARAM, event.getLocation());
        }
            params.put(APP_KEY_PARAM, "AIzaSyBF4RtLcPosKQg8kFeBCCVDtBvN5tirNxg");

            // execute a GET request expecting a JSON object response
            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // load the results into movies list
                    try {

                        JSONArray rows = response.getJSONArray("rows");
                        JSONObject element0 = rows.getJSONObject(0);
                        JSONArray elements = element0.getJSONArray("elements");
                        JSONObject distAway = elements.getJSONObject(0);
                        JSONObject text = distAway.getJSONObject("distance");
                        String finalText = text.getString("text");

                        event.setDistAway(finalText);
                    } catch (JSONException e) {
                    }
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    logError("Failed to get data from endpoint", throwable, true);
                }
            });
    }

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
