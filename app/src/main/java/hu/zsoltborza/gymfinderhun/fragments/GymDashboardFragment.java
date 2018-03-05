package hu.zsoltborza.gymfinderhun.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.fragments.base.DrawerItemBaseFragment;

/**
 * Created by Zsolt Borza on 2018.03.05..
 */

public class GymDashboardFragment extends DrawerItemBaseFragment {

    public static final String TAG = "GymDashboard";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
