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

// Provide the underlying view for an individual list item.
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.VH>
{
    private Activity context;
    private List<EventType> eventTypes;
    private List<Preference> preferences;
    public ArrayList<String> filter= new ArrayList<>();
    private String type;
    private int selectedPos = -1;


    public FilterAdapter(Activity context, List<Preference> preferences)
    {
        this.context = context;
        if (preferences == null)
        {
            throw new IllegalArgumentException("eventTypes must not be null");
        }
        this.preferences = preferences;
    }

    // Inflate the view based on the viewType provided.
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false);
        return new VH(itemView, context);
    }

    // Display data at the specified position
    @Override
    public void onBindViewHolder(final VH holder, int position)
    {
        final Preference type = preferences.get(position);
        holder.rootView.setTag(type);
        holder.tBtnOverlay.setText(type.getName());
        holder.tBtnOverlay.setTextOff(type.getName());
        holder.tBtnOverlay.setTextOn(type.getName());
        if(holder.tBtnOverlay.isChecked())
        {
            holder.tBtnOverlay.setBackgroundResource(R.drawable.my_selecter);
        } else {
            holder.tBtnOverlay.setBackgroundResource(R.drawable.my_selecter);;
        }
        Glide.with(context).load(type.getThumbnailImage()).centerCrop().into(holder.ivBackground);
        holder.itemView.setSelected(selectedPos == position);
    }

    @Override
    public int getItemCount()
    {
        return preferences.size();
    }

    // Provide a reference to the views for each contact item
    public class VH extends RecyclerView.ViewHolder
    {
        final View rootView;
        final ImageView ivBackground;
        final ToggleButton tBtnOverlay;

        public VH(final View itemView, final Context context)
        {
            super(itemView);
            itemView.setClickable(true);
            rootView = itemView;
            ivBackground = (ImageView) itemView.findViewById(R.id.ivBackground);
            tBtnOverlay = (ToggleButton) itemView.findViewById(R.id.tBtnOverlay);
            tBtnOverlay.bringToFront();

            // on Click Method goes here TODO
            tBtnOverlay.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (tBtnOverlay.isChecked())
                    {
                        type = tBtnOverlay.getText()+"";
                        type = type.toLowerCase();
                        switch (type) {
                            case "activism":
                                type = "politics_activism";
                                break;
                            case "education":
                                type = "learning_education";
                                break;
                            case "film":
                                type = "movies_film";
                                break;
                            case "family":
                                type = "family_fun_kids";
                                break;
                            case "nightlife":
                                type = "singles_social";
                                break;
                            case "theater":
                                type = "performing_arts";
                                break;
                            case "recreation":
                                type = "outdoors_recreation";
                                break;
                            case "religion":
                                type = "religion_spirituality";
                                break;
                            case "tech":
                                type = "technology";
                                break;
                        }
                        Toast.makeText(context, tBtnOverlay.getText()+" has been selected.",Toast.LENGTH_SHORT).show();
                        tBtnOverlay.setBackgroundResource(R.drawable.my_selecter);
                        filter.add(type);
                    }
                    else
                    {
                        type = tBtnOverlay.getText()+"";
                        type = type.toLowerCase();
                        switch (type) {
                            case "activism":
                                type = "politics_activism";
                                break;
                            case "education":
                                type = "learning_education";
                                break;
                            case "film":
                                type = "movies_film";
                                break;
                            case "family":
                                type = "family_fun_kids";
                                break;
                            case "nightlife":
                                type = "singles_social";
                                break;
                            case "theater":
                                type = "performing_arts";
                                break;
                            case "recreation":
                                type = "outdoors_recreation";
                                break;
                            case "religion":
                                type = "religion_spirituality";
                                break;
                            case "tech":
                                type = "technology";
                                break;
                        }
                        Toast.makeText(context, tBtnOverlay.getText()+" has been unselected.",Toast.LENGTH_SHORT).show();
                        tBtnOverlay.setBackgroundResource(R.drawable.my_selecter);
                        filter.remove(type);
                    }

                }
            });

        }
    }

    public ArrayList<String> getAdapter(){
        return filter;
    }

}
