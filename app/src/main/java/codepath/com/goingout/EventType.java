package codepath.com.goingout;

/**
 * Created by acamara on 7/13/17.
 */

public enum EventType {
    ACTIVISM(R.drawable.cause),
    ART(R.drawable.art),
    ANIMALS(R.drawable.drink),
    BOOKS(R.drawable.literature),
    BUSINESS(R.drawable.professional),
    COMEDY(R.drawable.crafts),
    EDUCATION(R.drawable.game),
    FAMILY(R.drawable.home),
    FILM(R.drawable.film),
    FOOD(R.drawable.food),
    MUSIC(R.drawable.music),
    NIGHTLIFE(R.drawable.party),
    THEATER(R.drawable.dance),
    RECREATION(R.drawable.recreation),
    RELIGION(R.drawable.religion),
    SALES(R.drawable.shopping),
    SCIENCE(R.drawable.fitness),
    SPORTS(R.drawable.sports),
    TECH(R.drawable.theater),
    OTHER(R.drawable.wellness);

    private int thumbnailImage;

    private EventType(int thumbnailImage)
    {
        this.thumbnailImage = thumbnailImage;
    }

    public int getThumbnailImage()
    {
        return thumbnailImage;
    }

    @Override
    public String toString()
    {
        String constName = super.toString();
        if (constName.contains("_")) {
        }
        return constName.substring(0,1) + constName.substring(1).toLowerCase();
    }
}
