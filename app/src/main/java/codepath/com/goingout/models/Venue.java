package codepath.com.goingout.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by acamara on 7/20/17.
 */

public class Venue {
    public String location;
    public String imageUrl;
    public int rating;
    public String price;

    public Venue(JSONObject object) throws JSONException{
        location = object.getString("formatted_address");
    //    imageUrl = object.getString("");
        if (object.has("rating")) {
            rating = object.getInt("rating");
        }
        if (object.has("price_level")) {
            price = object.getString("price_level");
        }

    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
