package codepath.com.goingout.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import codepath.com.goingout.EventType;
import codepath.com.goingout.R;
import codepath.com.goingout.models.Preference;

import static codepath.com.goingout.R.drawable.selected;

// Provide the underlying view for an individual list item.
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.VH> {
    private Activity context;
    private List<EventType> eventTypes;
    private List<Preference> preferences;
    private ArrayList<String> filter= new ArrayList<>();
    private String type;
    //    private int oldColor = Color.BLACK;
    private int selectedPos = -1;


    public FilterAdapter(Activity context, List<Preference> preferences) {
        this.context = context;
        if (preferences == null) {
            throw new IllegalArgumentException("eventTypes must not be null");
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
//        holder.tvType.setText(type.getName());
        holder.tBtnOverlay.setText(type.getName());
        holder.tBtnOverlay.setTextOff(type.getName());
        holder.tBtnOverlay.setTextOn(type.getName());
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
//        final TextView tvType;
        final ToggleButton tBtnOverlay;

        public VH(final View itemView, final Context context) {
            super(itemView);
            itemView.setClickable(true);
            rootView = itemView;
            ivBackground = (ImageView) itemView.findViewById(R.id.ivBackground);
            tBtnOverlay = (ToggleButton) itemView.findViewById(R.id.tBtnOverlay);
//            ivOverlay.setBackgroundColor(oldColor);
            tBtnOverlay.bringToFront();
//            tvType = (TextView) itemView.findViewById(R.id.tvType);
//            tvType.bringToFront();




            // on Click Method goes here TODO
            if (tBtnOverlay.isChecked()){
                tBtnOverlay.setBackground(drawable.selected);
            }else{

            }

            tBtnOverlay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (tBtnOverlay.isChecked()) {
                        type = tBtnOverlay.getText()+"";
                        Toast.makeText(context, tBtnOverlay.getText()+" has been selected.",Toast.LENGTH_SHORT).show();
                        filter.add(type);
                    }else {
                        type = tBtnOverlay.getText()+"";
                        Toast.makeText(context, tBtnOverlay.getText()+" has been unselected.",Toast.LENGTH_SHORT).show();
                        filter.remove(type);

                    }

//                    view.setSelected(true);


//                    notifyItemChanged(selectedPos);
//                    selectedPos = getLayoutPosition();
//                    Toast.makeText(context, tvType.getText()+" has been selected.",Toast.LENGTH_SHORT).show();
//                    if (ivOverlay.isSelected())
//                    {
//                        ivOverlay.setBackgroundColor(getNextColor());
//                    } else {
//                        ivOverlay.setBackgroundColor(Color.BLACK);
//                    }
//                    notifyItemChanged(selectedPos);

                }
            });
        }
    }

}
