package codepath.com.goingout.models;

import org.parceler.Parcel;

/**
 * Created by acamara on 7/20/17.
 */

@Parcel
public class Post {
    public String username;
    public String timeStamp;
    public String body;
    public int image;
    public String video;

    public Post(){

    }

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
