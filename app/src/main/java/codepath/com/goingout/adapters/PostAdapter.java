package codepath.com.goingout.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
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



public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    // list of posts
    ArrayList<Post> posts;
    //context for rendering
    Context context;

    // initialized with list
    public PostAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    //creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Post post = posts.get(position);
        holder.tvUserName.setText(post.getUsername());
        holder.tvTimeStamp.setText(post.getTimeStamp());
        holder.tvBody.setText(post.getBody());
        ImageView imageView = holder.ivPicture;
        VideoView videoView = holder.vvVideo;
        // add media controller to each video view
        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(holder.vvVideo);
        videoView.setMediaController(mediaController);
        
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
            //videoView.start();

            videoView.setVideoPath(post.getVideo());

        } else {
            // both image and video are null, so just plain text post
            holder.ivPicture.setVisibility(View.GONE);
            holder.vvVideo.setVisibility(View.GONE);
            Toast.makeText(context, "hi", Toast.LENGTH_LONG);
        }
    }


    // returns the total number of items in the list
    @Override
    public int getItemCount() {
        return posts.size();
    }

    // create the viewholder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder
            //implements View.OnClickListener
            {
        // track view objects
        TextView tvUserName;
        TextView tvTimeStamp;
        TextView tvBody;
        ImageView ivPicture;
        VideoView vvVideo;
        //ImageButton ibPlay;

        public ViewHolder(View itemView) {
            super(itemView);
            // lookup view objects by id
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            ivPicture = (ImageView) itemView.findViewById(R.id.ivPicture);
            vvVideo = (VideoView) itemView.findViewById(R.id.vvVideo);
            //ibPlay = (ImageButton) itemView.findViewById(R.id.ibPlay);
            //itemView.setOnClickListener(this);

        }

//        @Override
//        public void onClick(View v) {
//            // gets item position
//            int position = getAdapterPosition();
//            // make sure the position is valid, i.e. actually exists in the view
//            if (position != RecyclerView.NO_POSITION) {
//                // get the movie at the position, this won't work if the class is static
//                Post post = posts.get(position);
//                vvVideo.setVideoPath(post.getVideo());
//                vvVideo.start();
//
//            }
//        }

    }
}
