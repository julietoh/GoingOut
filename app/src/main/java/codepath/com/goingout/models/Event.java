package codepath.com.goingout.models;


import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by joh on 7/12/17.
 */

@Parcel
public class Event {
    private String title;
    private Preference category;
    private String date;
    private String location;

    public String image;
    private String rating;
    private String place;
    public Venue venue;

    private Post post;
    private String city;
    private String address;
    private long order;

    private boolean isFromJSON;


    public Event(){
        isFromJSON = false;
    }

    public Event (JSONObject object) throws JSONException {
        title = object.getString("title");
//        category = null;
        date = object.getString("start_time");

        location =  object.getString("city_name") + ", " + object.getString("region_abbr");

        place = object.getString("venue_name");

        address = object.getString("venue_address");

        venue = null;

        city = object.getString("city_name");

        order =  -1 * new Date().getTime();

        isFromJSON = true;



    }

    // deserialize json
    // extract the values from JSON



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Preference getCategory() {
        return category;
    }

    public void setCategory(Preference category) {
        this.category = category;
    }

    public String getDate() {
        if (isFromJSON) {
            String displayDate = date.substring(5, 7) + "/"
                    + date.substring(8, 10) + "/" + date.substring(2, 4);

            // converting from military time to standard time
            int hour = Integer.parseInt(date.substring(11, 13));
            String time;
            if (hour > 12) {
                // pm
                hour = (hour - 12);
                time = hour + date.substring(13, 16) + "pm";
            } else if (hour == 0) {
                // default time, time not entered
                return displayDate;
                // am
                // hour = 12;
                // time = hour + date.substring(13, 16) + "am";
            } else if (hour == 12) {
                // pm
                time = hour + date.substring(13, 16) + "pm";
            } else {
                // am
                time = hour + date.substring(13, 16) + "am";
            }
            return displayDate + " " + time;
        }
        else {
            return date;
        }
    }


    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


//

//    public String getPrice() {
//        return price;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

//
//    public String getRating() {
//        return rating;
//    }
//
//    public void setRating(String rating) {
//        this.rating = rating;
//    }


    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isFromJSON() {
        return isFromJSON;
    }
}
