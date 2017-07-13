package codepath.com.goingout.models;

import java.util.ArrayList;
import java.util.List;

import codepath.com.goingout.R;

/**
 * Created by acamara on 7/13/17.
 */

public class Type {
    private String name;
    private int thumbnailImage;

    public Type (String name, int thumbnailImage) {
        this.name = name;
        this.thumbnailImage = thumbnailImage;
    }

    public String getName() {
        return name;
    }

    public int getThumbnailImage() {
        return  thumbnailImage;
    }

    public static List<Type> getTypes() {
        List<Type> filter = new ArrayList<>();
        filter.add(new Type("Art", R.drawable.art));
        filter.add(new Type("Cause", R.drawable.cause));
        filter.add(new Type("Crafts", R.drawable.crafts));
        filter.add(new Type("Dance", R.drawable.dance));
        filter.add(new Type("Drink", R.drawable.drink));
        filter.add(new Type("Film", R.drawable.film));
        filter.add(new Type("Fitness", R.drawable.fitness));
        filter.add(new Type("Food", R.drawable.food));
        filter.add(new Type("Game", R.drawable.game));
        filter.add(new Type("Home", R.drawable.home));
        filter.add(new Type("Literature", R.drawable.literature));
        filter.add(new Type("Music", R.drawable.music));
        filter.add(new Type("Professional", R.drawable.professional));
        filter.add(new Type("Party", R.drawable.party));
        filter.add(new Type("Recreation", R.drawable.recreation));
        filter.add(new Type("Religion", R.drawable.religion));
        filter.add(new Type("Shopping", R.drawable.shopping));
        filter.add(new Type("Sports", R.drawable.sports));
        filter.add(new Type("Theater", R.drawable.theater));
        filter.add(new Type("Wellness", R.drawable.wellness));
        return filter;
    }
}
