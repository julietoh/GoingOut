package codepath.com.goingout.adapters;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import codepath.com.goingout.R;
import codepath.com.goingout.models.Post;

/**
 * Created by acamara on 7/20/17.
 */



public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    // list of posts
    ArrayList<Post> posts;
    //context for rendering
    Context context;


    // initialized with list
    public PostAdapter(ArrayList<Post> posts)
    {
        this.posts = posts;
    }

    //creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //get the context and create the inflator
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create the view using the item_poat layout
        View postView = inflater.inflate(R.layout.item_post, parent, false);
        //return a new ViewHolder
        return new ViewHolder(postView);
    }

    //binds an inflated view to a new item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final Post post = posts.get(position);
        holder.tvUserName.setText(post.getUsername());
        holder.tvTimeStamp.setText(post.getTimeStamp());
        holder.tvBody.setText(post.getBody());
        ImageView imageView = holder.ivPicture;
        VideoView videoView = holder.vvVideo;
        //holder.vvVideo.getLayoutParams().height = getScreenWidth(this.context) * 9 /16;
        // display post with image, hide video view
        if (post.getImage() != null) {
            holder.ivPicture.setVisibility(View.VISIBLE);
            holder.vvVideo.setVisibility(View.GONE);

            // load from post
            // load image using glide
            Glide.with(context)
                    .load(post.getImage())
                    //.placeholder(placeholderId)
                    //.error(placeholderId)
                    .centerCrop()
                    .into(imageView);
        } else if (post.getVideo() != null) {
            holder.ivPicture.setVisibility(View.GONE);
            holder.vvVideo.setVisibility(View.VISIBLE);

            // load video into video view

            videoView.setVideoPath(post.getVideo());
            videoView.start();
            Log.e("hi", "hi");

        } else {
            // both image and video are null, so just plain text post
            holder.ivPicture.setVisibility(View.GONE);
            holder.vvVideo.setVisibility(View.GONE);
            Toast.makeText(context, "hi", Toast.LENGTH_LONG);
        }


    }


    public static int getScreenWidth(Context c) {
        int screenWidth = 0; // this is part of the class not the method
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    // returns the total number of items in the list
    @Override
    public int getItemCount()
    {
        return posts.size();
    }

    // create the viewholder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // track view objects
        TextView tvUserName;
        TextView tvTimeStamp;
        TextView tvBody;
        ImageView ivPicture;
        VideoView vvVideo;

        public ViewHolder(View itemView)
        {
            super(itemView);
            // lookup view objects by id
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            ivPicture = (ImageView) itemView.findViewById(R.id.ivPicture);
            vvVideo = (VideoView) itemView.findViewById(R.id.vvVideo);

        }
    }

}
