package codepath.com.goingout.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import codepath.com.goingout.DetailsActivity;
import codepath.com.goingout.EventListActivity;
import codepath.com.goingout.R;
import codepath.com.goingout.models.Event;
import codepath.com.goingout.models.User;

/**
 * Created by rafelix on 7/17/17.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.VH> {
        Context context; //private Activity context;
        ArrayList<Event> events; //private List<Feeds> events;
    User currentUser= EventListActivity.currentUser;

    public interface FeedAdapterListener{
        public void onItemSelected(View view, int position, boolean isPic);
    }

    public FeedAdapter(ArrayList<Event> events, Context context) {
        this.events = events;
        this.context = context;
        if (events == null) {
            throw new IllegalArgumentException("feed must not be null");
        }
        this.events = events;
    }

    // Inflate the view based on the viewType provided.
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewFeeds) {
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
//            return new VH(itemView, context);
        //get the context and create the inflator
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create the view using the item_movie layout
        View eventView = inflater.inflate(R.layout.item_feed, parent, false);
        //return a new ViewHolder

        return new VH(eventView,context);
    }

    public void clear() {
        events.clear();
        notifyDataSetChanged();
    }
        // Display data at the specified position
        @Override
        public void onBindViewHolder(final VH holder, int position) {
            final Event event = events.get(position);
//            Drawable image = LoadImageFromWebOperations("https://lh4.googleusercontent.com/-1wzlVdxiW14/USSFZnhNqxI/AAAAAAAABGw/YpdANqaoGh4/s1600-w400/Google%2BSydney");


            if (event.venue != null) {
                holder.rootView.setTag(event);
                holder.tvTitle.setText(event.getTitle());
                holder.tvTime.setText(event.getDate());
                holder.tvLocation.setText(event.getPlace() + ", " + event.venue.getLocation());
                //holder.tvRating.setText(event.venue.getRating() + "");
                //holder.tvPrice.setText(event.venue.getPrice());
                holder.tvPrice.setText(event.venue.getPay(event.venue.pay));
                String GoogleUriString = event.venue.getFinalURL();
                if (GoogleUriString != null) {
                    Uri GoogleUri = Uri.parse(GoogleUriString);
                    int hey = event.venue.getRating();
                    holder.ratingBar.setRating(hey);
                    Picasso.with(context).load(GoogleUri).into(holder.ivBackground);
                }else{
                    holder.ivBackground.setBackgroundColor(holder.id);

                }
                //holder.ivBackground.setBackground(image);
                //holder.ivBackground.setBackgroundColor(holder.id);
            } else {
                holder.rootView.setTag(event);
                holder.tvTitle.setText(event.getTitle());
                holder.tvTime.setText(event.getDate());
                holder.tvLocation.setText(event.getPlace() + ", " + event.getAddress());
                String GoogleUriString = "https://maps.googleapis.com/maps/api/plac/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=AIzaSyB4MzgjTcqzPIL_6v468qvhyLCbPzeTKlo";
                Uri GoogleUri = Uri.parse(GoogleUriString);
                Picasso.with(context).load(GoogleUri).into(holder.ivBackground);
                //holder.ivBackground.setBackground(image);
                //holder.ivBackground.setBackgroundColor(holder.id);
            }
//        holder.tvRating.getNumStars();
//            holder.ivBackground.setBackgroundColor(Color.BLACK);
//            Glide.with(context).load(R.drawable.art.into(holder.ivBackground);
        }

        public static Drawable LoadImageFromWebOperations(String url) {
            try {
                InputStream is = (InputStream) new URL(url).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");
                return d;
            } catch (Exception e) {
                return null;
            }
        }


        @Override
        public int getItemCount() {
            return events.size();
        }

        // Provide a reference to the views for each contact item
        public class VH extends ViewHolder implements View.OnClickListener{
            final View rootView;
            final ImageView ivBackground;
            final TextView tvTitle;
            final TextView tvTime;
            final TextView tvLocation;
            //final TextView tvRating;
            final TextView tvPrice;
            final int id;
            final LinearLayout llFeed;
            final RatingBar ratingBar;
            //final RatingBar tvRating;

            public VH(View itemView, Context c) {
                super(itemView);
                rootView = itemView;
                ivBackground = (ImageView)itemView.findViewById(R.id.ivBackground);
                Random rand = new Random();
                int rndInt = rand.nextInt(4) + 1;// n = the number of images, that start at idx 1
                String imgName = "img" + "_" + rndInt;
                id = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
                ivBackground.setImageResource(id);

//                if (post.hasVideo){
//                    itemview.setVV(Link of video)
//                }else
//                {
//                    view.serIV(Link of picture)
//                }

                tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
                tvTime = (TextView)itemView.findViewById(R.id.tvTime);
                ratingBar = (RatingBar)itemView.findViewById(R.id.rating);
                tvLocation = (TextView)itemView.findViewById(R.id.tvLocation);
                //tvRating = (TextView)itemView.findViewById(R.id.tvRating);
                tvPrice = (TextView)itemView.findViewById(R.id.tvPrice);
                llFeed = (LinearLayout) itemView.findViewById(R.id.llFeed);
                llFeed.bringToFront();
                //tvRating = (RatingBar) itemView.findViewById(R.id.tvRating);
                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                // gets the item position
                int position = getAdapterPosition();
                // make sure the position is valid (actually exists)
                if (position != RecyclerView.NO_POSITION)
                {
                    Event event = events.get(position);
                    //create intent for the new activity
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("title", tvTitle.getText());
                    intent.putExtra("time", tvTime.getText());
                    intent.putExtra("location",tvLocation.getText());
                    if (tvPrice.getText() != null) {
                    intent.putExtra("price",tvPrice.getText());

                    }
                    if (event.venue.getFinalURL() != null) {
                        intent.putExtra("image_url", event.venue.getFinalURL());
                    }

                    intent.putExtra("current_user", Parcels.wrap(currentUser));

                    context.startActivity(intent);
                }
            }
        }
}
