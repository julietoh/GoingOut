package codepath.com.goingout.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by acamara on 7/20/17.
 */

public class Venue {
    private String location;
    private String imageUrl;
    private int rating;
    private int price;

    public Venue(JSONObject object) throws JSONException{
        location = object.getString("");
        imageUrl = object.getString("");
        rating = object.getInt("");
        price = object.getInt("");
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
