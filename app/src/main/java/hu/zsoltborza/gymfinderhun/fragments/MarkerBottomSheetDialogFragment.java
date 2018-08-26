package hu.zsoltborza.gymfinderhun.fragments;

import android.annotation.SuppressLint;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import hu.zsoltborza.gymfinderhun.R;

public class MarkerBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private String title;
    private String address;
    private String distance;


    private static final String KEY_TITLE = "title";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DISTANCE = "distance";

    public MarkerBottomSheetDialogFragment() {

    }


    public MarkerBottomSheetDialogFragment newInstance(String title, String address, String distance) {
        MarkerBottomSheetDialogFragment fragment = new MarkerBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putString(KEY_ADDRESS, address);
        args.putString(KEY_DISTANCE, distance);
        fragment.setArguments(args);
        return fragment;
    }


    //Bottom Sheet Callback
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

   /* @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if ( savedInstanceState != null) {
            title = savedInstanceState.getString(KEY_TITLE);
            address = savedInstanceState.getString(KEY_ADDRESS);
            distance = savedInstanceState.getString(KEY_DISTANCE);
        }

    }*/


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //Get the content View
        View contentView = View.inflate(getContext(), R.layout.fragment_marker_dialog, null);
        dialog.setContentView(contentView);

        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        AppCompatTextView titleText = contentView.findViewById(R.id.tvDialogTitle);
        titleText.setText(title);
        AppCompatTextView addressText = contentView.findViewById(R.id.tvDialogAddress);
        addressText.setText(address);
        AppCompatTextView distanceText = contentView.findViewById(R.id.tvDialogDistance);
        distanceText.setText(distance);

        //Set callback
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
