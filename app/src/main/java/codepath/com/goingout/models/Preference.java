package codepath.com.goingout.models;

import java.util.ArrayList;
import java.util.List;

import codepath.com.goingout.EventType;

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
        for (EventType eventType : EventType.values())
        {
            String name = eventType.toString();
            int thumbnailImage = eventType.getThumbnailImage();
            filter.add(new Preference(name, thumbnailImage));
        }

        return filter;
    }

    public boolean isSelected() {
        return isSelected;
    }


}
