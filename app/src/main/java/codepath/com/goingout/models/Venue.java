package codepath.com.goingout.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by acamara on 7/20/17.
 */

@Parcel
public class Venue {
    public String location;
    public String photoRef;
    public int rating;
    public String price;
    public String finalURL;
    public int pay;

    public Venue(){
    }

    public Venue(JSONObject object) throws JSONException {
        location = object.getString("formatted_address");

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=");

        //photoRef = object.getJSONObject("photos").getString("photo_reference");
        if (object.has("price_level")) {
            price = object.getString("price_level");
            pay = Integer.parseInt(price);
        }


        if (object.has("photos")) {
            photoRef = object.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
            sb.append(photoRef);

            sb.append("&key=AIzaSyDUEuKt4fzSuxvB96vEooFBRyp-WO50H6g");

            finalURL = sb.toString();

        }

        if (object.has("rating")) {
            rating = object.getInt("rating");
        }


    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhotoRef() {
        return photoRef;
    }

    public void setPhotoRef(String imageUrl) {
        this.photoRef = imageUrl;
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

    public String getFinalURL() {
        return finalURL;
    }

    public void setFinalURL(String finalURL) {
        this.finalURL = finalURL;
    }

    public String getPay(int num) {
        if (num == 0) {
            return "";
        }
        else {
            return "$" + getPay(num - 1);
        }
    }

    public void setPay(int pay) {
        this.pay = pay;
    }
}
