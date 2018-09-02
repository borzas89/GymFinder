package hu.zsoltborza.gymfinderhun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.model.GymItemDto;


/**
 * Created by Borzas on 2018. 01. 21..
 */
public class GymOnlineSearchAdapter extends RecyclerView.Adapter<GymOnlineSearchAdapter.ViewHolder> {


    private final Context context;
    private List<GymItemDto> gymList;
    private final LayoutInflater inflater;
    private OnItemClickListener listener;

    public GymOnlineSearchAdapter(Context context, List<GymItemDto> gymList, OnItemClickListener listener) {
        this.context = context;
        this.gymList = gymList;
        this.inflater= LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_gym_item_with_image, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        GymItemDto item = gymList.get(position);

        // picture reference...
        String key = "AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg";
        String acutalPhoto = item.getPhotoReference();
        String imageUri =  "https://maps.googleapis.com/maps/api/place/photo?photoreference="+acutalPhoto+"&sensor=false&maxheight=200&maxwidth=300&key="+key;

        Picasso.with(context).load(imageUri).into(holder.image);

//        holder.image.setImageDrawable();
        holder.titleTV.setText(item.getTitle());
        holder.addressTV.setText(item.getAddress());
        holder.infoTV.setText(item.getDistance());
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(position);
            }
        });
    }

    public void updateList(List<GymItemDto> receivedList){
        gymList = new ArrayList<>();
        gymList.addAll(receivedList);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return gymList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView titleTV;
        public final TextView addressTV;
        public final TextView infoTV;
        public final FrameLayout frameLayout;
        public final ImageView image;
        private final View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            mView = itemView;
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
