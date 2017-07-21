package codepath.com.goingout.models;

/**
 * Created by acamara on 7/20/17.
 */

public class Post {
    private String username;
    private String timeStamp;
    private String body;
    private int image;
    private String video;

    public Post(String username, String timeStamp, String body) {
        this.username = username;
        this.timeStamp = timeStamp;
        this.body = body;
    }

    public Post(String username, String timeStamp, String body, int image) {
        this.username = username;
        this.timeStamp = timeStamp;
        this.body = body;
        this.image = image;
    }

    public Post(String username, String timeStamp, String body, String video) {
        this.username = username;
        this.timeStamp = timeStamp;
        this.body = body;
        this.video = video;

    }

    public String getUsername() {
        return username;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getBody() {
        return body;
    }

    public int getImage() {
        return image;
    }

    public String getVideo() {
        return video;
    }


}
