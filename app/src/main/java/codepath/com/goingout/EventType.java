package codepath.com.goingout;

/**
 * Created by acamara on 7/13/17.
 */

public enum EventType {
    ART(R.drawable.art, false),
    CAUSE(R.drawable.cause, false),
    CRAFTS(R.drawable.crafts, false),
    DANCE(R.drawable.dance, false),
    DRINK(R.drawable.drink, false),
    FILM(R.drawable.film, false),
    FITNESS(R.drawable.fitness, false),
    FOOD(R.drawable.food, false),
    GAME(R.drawable.game, false),
    HOME(R.drawable.home, false),
    LITERATURE(R.drawable.literature, false),
    MUSIC(R.drawable.music, false),
    PROFESSIONAL(R.drawable.professional, false),
    PARTY(R.drawable.party, false),
    RECREATION(R.drawable.recreation, false),
    RELIGION(R.drawable.religion, false),
    SHOPPING(R.drawable.shopping, false),
    SPORTS(R.drawable.sports, false),
    THEATER(R.drawable.theater, false),
    WELLNESS(R.drawable.wellness, false);

    private int thumbnailImage;
    private boolean isSelected;

    private EventType(int thumbnailImage, boolean isSelected)
    {
        this.thumbnailImage = thumbnailImage;
        this.isSelected = isSelected;
    }

    public int getThumbnailImage()
    {
        return thumbnailImage;
    }

    public void setIsSelected(boolean b)
    {
        isSelected = b;
    }

    @Override
    public String toString()
    {
        String constName = super.toString();
        return constName.substring(0,1) + constName.substring(1).toLowerCase();
    }
}
