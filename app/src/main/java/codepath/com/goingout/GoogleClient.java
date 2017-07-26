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
    // the parameter name for the API key
    public final static String APP_KEY_PARAM = "key";
    public final static String PHOTO_SEARCH_PARAM = "maxwidth=400&photoreference";
    public final static String TEXT_SEARCH_PARAM = "query";
    // tag for logging from this activity
    public final static String TAG = "EventListActivity";
    // image config
    // Config config;

    // instance fields
    private AsyncHttpClient client = new AsyncHttpClient();
    ;
    // the list of events
    private ArrayList<Venue> venues = new ArrayList<>();

    public Venue getInfo(Event event) {
        // create the url
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(TEXT_SEARCH_PARAM, event.getPlace().replaceAll(" ",""));
        params.put(APP_KEY_PARAM, "AIzaSyAAlnXR9FWu_4dgufQXL6FbLdMQkZHrLTQ");
        // execute a GET request expecting a JSON object response
        client.get(API_TEXT_SEARCH_BASE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // load the results into movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    // iterate through result set and create Movie objects
                    Venue venue = new Venue(results.getJSONObject(0));
                    venues.add(venue);
                } catch (JSONException e) {
                    logError("Failed to parse events", e, true);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from endpoint", throwable, true);
            }
        });
        if (venues.size() > 0) {
            return venues.get(0);
        } else {
            return null;
        }
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
