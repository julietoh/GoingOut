package codepath.com.goingout.models;

import java.util.ArrayList;
import java.util.List;

import codepath.com.goingout.R;

/**
 * Created by acamara on 7/13/17.
 */

public class Preference {
    private String name;
    private int thumbnailImage;
    public boolean isSelected = false;

    public Preference(String name, int thumbnailImage) {
        this.name = name;
        this.thumbnailImage = thumbnailImage;
    }

    public String getName() {
        return name;
    }

    public int getThumbnailImage() {
        return  thumbnailImage;
    }

    public static List<Preference> getTypes() {
        List<Preference> filter = new ArrayList<>();
        filter.add(new Preference("Art", R.drawable.art));
        filter.add(new Preference("Cause", R.drawable.cause));
        filter.add(new Preference("Crafts", R.drawable.crafts));
        filter.add(new Preference("Dance", R.drawable.dance));
        filter.add(new Preference("Drink", R.drawable.drink));
        filter.add(new Preference("Film", R.drawable.film));
        filter.add(new Preference("Fitness", R.drawable.fitness));
        filter.add(new Preference("Food", R.drawable.food));
        filter.add(new Preference("Game", R.drawable.game));
        filter.add(new Preference("Home", R.drawable.home));
        filter.add(new Preference("Literature", R.drawable.literature));
        filter.add(new Preference("Music", R.drawable.music));
        filter.add(new Preference("Professional", R.drawable.professional));
        filter.add(new Preference("Party", R.drawable.party));
        filter.add(new Preference("Recreation", R.drawable.recreation));
        filter.add(new Preference("Religion", R.drawable.religion));
        filter.add(new Preference("Shopping", R.drawable.shopping));
        filter.add(new Preference("Sports", R.drawable.sports));
        filter.add(new Preference("Theater", R.drawable.theater));
        filter.add(new Preference("Wellness", R.drawable.wellness));
        return filter;
    }

    public boolean isSelected() {
        return isSelected;
    }


}
