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
import codepath.com.goingout.models.Type;

// Provide the underlying view for an individual list item.
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.VH> {
    private Activity context;
    private List<Type> types;

    public FilterAdapter(Activity context, List<Type> types) {
        this.context = context;
        if (types == null) {
            throw new IllegalArgumentException("contacts must not be null");
        }
        this.types = types;
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
        final Type contact = types.get(position);
        holder.rootView.setTag(contact);
        holder.tvType.setText(contact.getName());
        Glide.with(context).load(contact.getThumbnailImage()).centerCrop().into(holder.ivBackground);
    }

    @Override
    public int getItemCount() {
        return types.size();
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

            // Navigate to contact details activity on click of card view.
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final Type type = (Type) v.getTag();
//                    if (type != null) {
//                        // Fire an intent when a contact is selected
//                        Intent intent = new Intent(com.codepath.goingout.adapters.ContactsAdapter.this.context, DetailsActivity.class);
//                        // Pass contact object in the bundle and populate details activity.
//                        intent.putExtra("EXTRA_CONTACT", type);
//                        com.codepath.android.lollipopexercise.adapters.ContactsAdapter.this.context.startActivity(intent);
//                    }
//                }
//            });
        }
    }
}
