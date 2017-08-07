package codepath.com.goingout.models;

import org.parceler.Parcel;


/**
 * Created by acamara on 7/20/17.
 */

@Parcel
public class Post {


    private String postId;
    private String username;
    private String timeStamp;
    private String body;
    private String image;
    private String video;
    private String string;
    private int num;
    private long order;
    private boolean isLive;



    public Post() {
    }


    // plain post
    public Post(String postId, String username, String timeStamp, String body, long order) {
        this.username = username;
        this.timeStamp = timeStamp;
        this.body = body;
        this.order = order;

    }

    // image, with arbitrary number
    public Post(String postId, String username, String timeStamp, String body, String image, int num, long order) {
        this.postId = postId;
        this.username = username;
        this.timeStamp = timeStamp;
        this.body = body;
        this.image = image;
        this.num = num;
        this.order = order;
    }

    // video, with arbitrary string
    public Post(String postId, String username, String timeStamp, String body, String video, String string, long order, boolean isLive) {
        this.postId = postId;
        this.username = username;
        this.timeStamp = timeStamp;
        this.body = body;
        this.video = video;
        this.string = string;
        this.order = order;
        this.isLive = isLive;


    }


    public String getPostId() { return postId;}

    public String getUsername() {
        return username;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getBody() {
        return body;
    }

    public String getImage() {
        return image;
    }

    public String getVideo() {
        return video;
    }

    public long getOrder() {
        return order;
    }

    public boolean isLive() {
        return isLive;
    }

}
