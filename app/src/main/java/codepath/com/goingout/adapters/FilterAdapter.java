package codepath.com.goingout.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import codepath.com.goingout.R;
import codepath.com.goingout.models.Preference;

// Provide the underlying view for an individual list item.
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.VH> {
    private Activity context;
    private List<Preference> preferences;

    public FilterAdapter(Activity context, List<Preference> preferences) {
        this.context = context;
        if (preferences == null) {
            throw new IllegalArgumentException("contacts must not be null");
        }
        this.preferences = preferences;
    }

    // Inflate the view based on the viewType provided.
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false);
        return new VH(itemView, context);
    }

    // Display data at the specified position
    @Override
    public void onBindViewHolder(final VH holder, int position) {
        final Preference contact = preferences.get(position);
        holder.rootView.setTag(contact);
        holder.tvType.setText(contact.getName());
        Glide.with(context).load(contact.getThumbnailImage()).centerCrop().into(holder.ivBackground);
    }

    @Override
    public int getItemCount() {
        return preferences.size();
    }

    // Provide a reference to the views for each contact item
    public class VH extends RecyclerView.ViewHolder {
        final View rootView;
        final ImageView ivBackground;
        final TextView tvType;

        public VH(View itemView, final Context context) {
            super(itemView);
            rootView = itemView;
            ivBackground = (ImageView)itemView.findViewById(R.id.ivBackground);
            tvType = (TextView)itemView.findViewById(R.id.tvType);

        // on Click Method goes here TODO
        }
    }
}
