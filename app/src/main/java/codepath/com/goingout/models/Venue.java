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
    public double longitude;
    public double latitude;
    public String placeId;

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

            sb.append("&key=AIzaSyDB_sSx3jP4yV6eE0zSjjhCDymWqb-UPMI");


            finalURL = sb.toString();

        }

        if (object.has("rating")) {
            rating = object.getInt("rating");
        }
        if (object.has("place_id")) {
            placeId = object.getString("place_id");
        }

//        if (object.has("geometry")) {
//            if (object.has("location")){
//                latitude = object.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
//                longitude = object.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
//            }
//        }


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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
