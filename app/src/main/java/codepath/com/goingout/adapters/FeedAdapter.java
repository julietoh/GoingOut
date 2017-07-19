package codepath.com.goingout.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

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
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
            return new VH(itemView, context);
        }

        // Display data at the specified position
        @Override
        public void onBindViewHolder(final VH holder, int position) {
            final Event contact = events.get(position);
            holder.rootView.setTag(contact);
            holder.tvFeeds.setText(contact.getTitle());
            holder.tvTime.setText(contact.getDate());
            holder.tvPrice.setText(contact.getPrice());
//        holder.tvRating.getNumStars();
            Glide.with(context).load(contact.getImage()).centerCrop().into(holder.ivBackground);
        }

        @Override
        public int getItemCount() {
            return events.size();
        }

        // Provide a reference to the views for each contact item
        public class VH extends RecyclerView.ViewHolder {
            final View rootView;
            final ImageView ivBackground;
            final TextView tvFeeds;
            final TextView tvTime;
            final TextView tvPrice;
            //final RatingBar tvRating;

            public VH(View itemView, final Context context) {
                super(itemView);
                rootView = itemView;
                ivBackground = (ImageView)itemView.findViewById(R.id.ivBackground);
                tvFeeds = (TextView)itemView.findViewById(R.id.tvFeeds);
                tvTime = (TextView)itemView.findViewById(R.id.tvTime);
                tvPrice = (TextView)itemView.findViewById(R.id.tvPrice);
                //tvRating = (RatingBar) itemView.findViewById(R.id.tvRating);

                // on Click Method goes here TODO
            }
        }
}
