package hu.zsoltborza.gymfinderhun.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.activities.MainActivity;
import hu.zsoltborza.gymfinderhun.model.GymListItem;
import hu.zsoltborza.gymfinderhun.fragments.base.DrawerItemBaseFragment;
import hu.zsoltborza.gymfinderhun.model.GymItemDto;
import hu.zsoltborza.gymfinderhun.fragments.base.BaseFragment;


public class GymDetailFragment extends DrawerItemBaseFragment {

    private GymListItem received;


    @BindView(R.id.favorite)
    FloatingActionButton favorite;
    @BindView(R.id.tvGymTitle)
    TextView gymTitle;

    public static final String TAG = "GymDetail";

    public GymDetailFragment() {
        // Required empty public constructor
    }

     @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hostActivityInterface.popBackStack();
            }
        }, 0);

        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            received = getArguments().getParcelable("gymItem");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gym_detail, container, false);
        ButterKnife.bind(this, rootView);






        if(getArguments() != null){

            gymTitle.setText(received.getTitle());
//            String key = "AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg";
//            String acutalPhoto = received.getPhotoReference();
//            String imageUri =  "https://maps.googleapis.com/maps/api/place/photo?photoreference="+acutalPhoto+"&sensor=false&maxheight=200&maxwidth=300&key="+key;
//            Picasso.with(getContext()).load(imageUri).into(gymPicture);

        }





        return rootView;
    }






    @Override
    public void onDetach() {
        super.onDetach();

    }


}
