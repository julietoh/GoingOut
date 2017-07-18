package codepath.com.goingout.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by joh on 6/22/17.
 */

public class Config {
    // the base url for loading images
    String imageBaseUrl;
    // the poster size to use when fetching images, part of url
    String posterSize;


    public Config(JSONObject object) throws JSONException {
        JSONObject venue = object.getJSONObject("venue_");
        JSONObject images = object.getJSONObject("images");
        // get the image base url
        imageBaseUrl = images.getString("secure_base_url");
        // get the poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        // use the option at index 3 or w342 as a fallback
        posterSize = posterSizeOptions.optString(3, "w342");

    }
    // helper method for creating urls
    public String getImageBaseUrl(String size, String path) {
        return String.format("%s%s%s", imageBaseUrl, size, path); //concat all three
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getImage_Base_Url() {
        return imageBaseUrl;
    }


}