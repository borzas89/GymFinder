package hu.zsoltborza.gymfinderhun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.util.List;

import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.database.MarkerEntity;


/**
 * Created by Borzas on 2018. 09. 06..
 * This adapter is using offline markers from db.
 */
public class GymOfflineAdapter extends RecyclerView.Adapter<GymOfflineAdapter.ViewHolder>{


    private final Context context;
    private List<MarkerEntity> gymList;
    private final LayoutInflater inflater;
    private OnItemClickListener listener;
    DecimalFormat df = new DecimalFormat("#.00");

    public void setActucalPosition(LatLng actucalPosition) {
        this.actucalPosition = actucalPosition;
    }

    private LatLng actucalPosition;

    public GymOfflineAdapter(Context context, List<MarkerEntity> gymList,OnItemClickListener listener) {
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
    public void onBindViewHolder(GymOfflineAdapter.ViewHolder holder, int position) {

        MarkerEntity item = gymList.get(position);

        holder.titleTV.setText(item.getTitle());
        holder.addressTV.setText(item.getAddress());


        actucalPosition = new LatLng(47.4544331, 19.633235);

//        double itemLat = Double.valueOf(item.getLatitude());
//        double itemLon = Double.valueOf(item.getLongitude());
        LatLng gymposition = new LatLng(item.getLatitude(),item.getLongitude());

        String distance = df.format(SphericalUtil.computeDistanceBetween(actucalPosition,gymposition)/1000) + " km";


        holder.infoTV.setText(distance);
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(position);
            }
        });

    }




    public LatLng getActucalPosition() {
        return actucalPosition;
    }



    public List<MarkerEntity> getAdapterList() {
        return gymList;
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
