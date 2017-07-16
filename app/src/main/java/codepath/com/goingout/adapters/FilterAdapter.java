package codepath.com.goingout.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
    private int oldColor = Color.BLACK;
    private int selectedPos = 0;


    public FilterAdapter(Activity context, List<Preference> preferences) {
        this.context = context;
        if (preferences == null) {
            throw new IllegalArgumentException("types must not be null");
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
        final Preference type = preferences.get(position);
        holder.rootView.setTag(type);
        holder.tvType.setText(type.getName());
        Glide.with(context).load(type.getThumbnailImage()).centerCrop().into(holder.ivBackground);
        holder.itemView.setSelected(selectedPos == position);
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
        final ImageView ivOverlay;

        public VH(final View itemView, final Context context) {
            super(itemView);
            rootView = itemView;
            ivBackground = (ImageView) itemView.findViewById(R.id.ivBackground);
            ivOverlay = (ImageView) itemView.findViewById(R.id.ivOverlay);
            ivOverlay.setBackgroundColor(oldColor);
            ivOverlay.bringToFront();
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            tvType.bringToFront();

            // on Click Method goes here TODO

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemChanged(selectedPos);
                    ivOverlay.setBackgroundColor(getNextColor());
                    selectedPos = getLayoutPosition();
                    notifyItemChanged(selectedPos);

                }
            });
        }

        private int getNextColor() {
            int newColor = (oldColor == Color.BLACK) ? Color.GREEN : Color.BLACK;
            oldColor = newColor;
            return newColor;
        }

    }
}
