package codepath.com.goingout.models;

import java.util.ArrayList;
import java.util.List;

import codepath.com.goingout.R;

/**
 * Created by rafelix on 7/13/17.
 */

public class Feeds {
    private String venue;
    private String time;
    private String price;
    private String rating;
    private int venueImage;

    public Feeds (String venue, String time, String price, String rating, int venueImage) {
        this.venue = venue;
        this.time = time;
        this.price = price;
        this.rating = rating;
        this.venueImage = venueImage;
    }

    public String getVenue() {
        return venue;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }

    public int getVenueImage() {
        return  venueImage;
    }

    public static List<Feeds> getFeeds() {
        List<Feeds> events = new ArrayList<>();
        events.add(new Feeds("Chupitos", "10:00PM", "$$$", "5", R.drawable.chupitos));
        events.add(new Feeds("Barcelo Club", "11:00PM", "$$", "3.5", R.drawable.clubparty));
        events.add(new Feeds("Teatro Amador", "10:30PM", "$", "4", R.drawable.teatro));
        events.add(new Feeds("Carpe Diem Lounge", "9:00PM", "$$$", "4", R.drawable.teatro2));
        events.add(new Feeds("Cafe Tacvba Live at The Greek", "11:45PM", "$$", "5", R.drawable.teatro3));
        events.add(new Feeds("Opening of The Shisha Hookah Bar", "8:00PM", "$$$", "3", R.drawable.teatro4));
        events.add(new Feeds("The Phantom of the Opera at The Greek", "6:00PM", "$$$", "5", R.drawable.teatro5));
        events.add(new Feeds("Dare Night", "9:00PM", "$$$", "5", R.drawable.teatro6));
        events.add(new Feeds("Yerma", "8:00PM", "$", "3", R.drawable.theater));
        return events;
    }
}
