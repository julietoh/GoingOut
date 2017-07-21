package codepath.com.goingout.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import codepath.com.goingout.DetailsActivity;
import codepath.com.goingout.R;
import codepath.com.goingout.models.Event;

/**
 * Created by rafelix on 7/17/17.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.VH> {
        Context context; //private Activity context;
        ArrayList<Event> events; //private List<Feeds> events;

        public FeedAdapter(ArrayList<Event> events) {
            this.events = events;
            //this.context = context;
            //if (events == null) {
            //    throw new IllegalArgumentException("contacts must not be null");
            //}
            //this.events = events;


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

        // Display data at the specified position
        @Override
        public void onBindViewHolder(final VH holder, int position) {
            final Event event = events.get(position);
            holder.rootView.setTag(event);
            holder.tvTitle.setText(event.getTitle());
            holder.tvTime.setText(event.getDate());
            holder.tvLocation.setText(event.getLocation());
//        holder.tvRating.getNumStars();
            holder.ivBackground.setBackgroundColor(Color.BLACK);
//            Glide.with(context).load(R.drawable.art.into(holder.ivBackground);

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
            //final RatingBar tvRating;

            public VH(View itemView, Context c) {
                super(itemView);
                rootView = itemView;
                ivBackground = (ImageView)itemView.findViewById(R.id.ivBackground);
                tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
                tvTime = (TextView)itemView.findViewById(R.id.tvTime);
                tvLocation = (TextView)itemView.findViewById(R.id.tvLocation);
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
//                    intent.putExtra("image_url", ivBackground.getImage());
                    context.startActivity(intent);
                }
            }
        }
}
