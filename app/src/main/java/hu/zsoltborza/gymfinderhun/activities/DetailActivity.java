package hu.zsoltborza.gymfinderhun.activities;

import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.model.GymItemDto;


public class DetailActivity extends AppCompatActivity {

    private GymItemDto received;

    @BindView(R.id.gym_pic)
    ImageView gymPicture;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.favorite)
    FloatingActionButton favorite;
    @BindView(R.id.toolbar)
    @Nullable
    Toolbar toolbar;
    @BindView(R.id.tvGymTitle)
    TextView gymTitle;
    @BindView(R.id.tvGymInfo)
    TextView gymInfo;
    @BindView(R.id.tvGymAddress)
    TextView gymAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);



        if(getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            received = bundle.getParcelable("gymItem");
            gymTitle.setText(received.getTitle());
            String key = "AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg";
            String acutalPhoto = received.getPhotoReference();
            String imageUri =  "https://maps.googleapis.com/maps/api/place/photo?photoreference="+acutalPhoto+"&sensor=false&maxheight=200&maxwidth=300&key="+key;
            Picasso.with(this).load(imageUri).into(gymPicture);
            setToolbar(received.getTitle());
            gymInfo.setText(received.getInfo());
            gymAddress.setText(received.getAddress());

        }

    }

    private void setToolbar(String title)
    {
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimary));
        collapsingToolbar.setTitle(title);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);
        collapsingToolbar.setTitleEnabled(true);

        if (toolbar != null)
        {
            // toolbar.setVisibility(View.GONE);
            ((AppCompatActivity) this).setSupportActionBar(toolbar);

            ActionBar actionBar = ((AppCompatActivity) this).getSupportActionBar();
            if (actionBar != null)
            {
//                actionBar.setDisplayHomeAsUpEnabled(true);

            }
        } else
        {
            // Don't inflate. Tablet is in landscape mode.
        }
    }



}
