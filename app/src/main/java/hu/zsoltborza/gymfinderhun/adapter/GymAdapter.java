package hu.zsoltborza.gymfinderhun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.model.GymListItem;

/**
 * Created by Borzas on 2018. 01. 21..
 */
public class GymAdapter extends RecyclerView.Adapter<GymAdapter.ViewHolder>{


    private final Context context;
    private List<GymListItem> gymList;
    private final LayoutInflater inflater;
    private OnItemClickListener listener;

    public GymAdapter(Context context, List<GymListItem> gymList,OnItemClickListener listener) {
        this.context = context;
        this.gymList = gymList;
        this.inflater= LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lits_gym_item_new, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        GymListItem item = gymList.get(position);

        holder.titleTV.setText(item.getTitle());
        holder.addressTV.setText(item.getAddress1());
        holder.infoTV.setText(item.getInfo());
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(position);
            }
        });
    }

    public void setFilter(List<GymListItem> filteredItems){
        gymList = new ArrayList<>();
        gymList.addAll(filteredItems);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if(gymList != null){
            return gymList.size();
        }else{
            return 0;
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView titleTV;
        public final TextView addressTV;
        public final TextView infoTV;
        public final FrameLayout frameLayout;
        public final ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.list_frame_item);
            titleTV= itemView.findViewById(R.id.titleTV);
            addressTV = itemView.findViewById(R.id.addressTV);
            infoTV = itemView.findViewById(R.id.infoTV);
            image = itemView.findViewById(R.id.image);


        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

}