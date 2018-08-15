package hu.zsoltborza.gymfinderhun.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import hu.zsoltborza.gymfinderhun.BuildConfig;
import hu.zsoltborza.gymfinderhun.fragments.GymDashboardFragment;
import hu.zsoltborza.gymfinderhun.location.InternalLocationReceiver;
import hu.zsoltborza.gymfinderhun.location.LocationService;
import hu.zsoltborza.gymfinderhun.model.GymItemDto;
import hu.zsoltborza.gymfinderhun.fragments.GymDetailFragment;
import hu.zsoltborza.gymfinderhun.fragments.GymListFragment;
import hu.zsoltborza.gymfinderhun.fragments.GymMapFragment;
import hu.zsoltborza.gymfinderhun.fragments.GymSearchFragment;
import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.fragments.base.BaseFragment;
import hu.zsoltborza.gymfinderhun.fragments.base.DrawerItemBaseFragment;
import hu.zsoltborza.gymfinderhun.fragments.base.HomeInterface;
import hu.zsoltborza.gymfinderhun.model.GymListItem;


public class MainActivity extends AppCompatActivity implements HomeInterface{

    private DrawerLayout drawer;
    private static final String TAG ="MainActivity";

    private static final String bundleLatKey ="lat";
    private static final String bundleLonKey ="lon";

    LatLng sampleLocation = new LatLng(47.548, 19.0719793);

    private Toolbar toolbar;
    private NavigationView nvDrawer;

    private BaseFragment selectedFragment;
    private DrawerItemBaseFragment selectedDrawerItemFragment;

    private boolean isWarnedToClose = false;

    private boolean isDrawerLocked = false;

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;

    private InternalLocationReceiver mInternalLocationReceiver;

    private Location currentLocation;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setVisibility(View.GONE);

        // Find our drawer view
        drawer = findViewById(R.id.drawer_layout);

        nvDrawer = findViewById(R.id.nvView);

        setupDrawerContent(nvDrawer);

        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        drawer.addDrawerListener(drawerToggle);


        if (savedInstanceState == null) {

            selectDrawerItem( nvDrawer.getMenu().getItem(0));

        }

        //Create internal receiver object in this method only.
        mInternalLocationReceiver = new InternalLocationReceiver(this);

        requestLocationService();
//        requestUpdates();
    }

    private void requestLocationService(){

        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        requestUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                          openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void hideTool(){
        toolbar.setVisibility(View.GONE);
    }

    public void ShowTool(){
        toolbar.setVisibility(View.VISIBLE);
    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_dashboard_fragment:
                toolbar.setTitle("Dashboard");;
                fragmentClass = GymDashboardFragment.class;
                break;
            case R.id.nav_first_fragment:
                toolbar.setTitle("Map Search");
                fragmentClass = GymMapFragment.class;
                break;
            case R.id.nav_second_fragment:
                toolbar.setTitle("Offline list");
                fragmentClass = GymListFragment.class;
                break;
            case R.id.nav_third_fragment:
                toolbar.setTitle("Online Search");
                fragmentClass = GymSearchFragment.class;
                break;
            default:
                fragmentClass = GymDashboardFragment.class;
        }

        try {
            fragment.setArguments(getLocationBundle());
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
            // Close the navigation drawer
        drawer.closeDrawers();
    }

    private Bundle getLocationBundle(){
        Bundle locationBundle = new Bundle();

        if(currentLocation != null){
            locationBundle.putDouble(bundleLatKey, currentLocation.getLatitude());
            locationBundle.putDouble(bundleLonKey, currentLocation.getLongitude());
        }else{
            locationBundle.putDouble(bundleLatKey, sampleLocation.latitude);
            locationBundle.putDouble(bundleLonKey, sampleLocation.longitude);
        }

        return locationBundle;
    }


    private void showDrawerItemFragment(DrawerItemBaseFragment firstTierFragment) {
        // Clear backstack if app is not at a first-tier fragment
        // and drawer is not locked, so that app could be switched between
        // any first-tier fragment from anywhere.
        if(!(selectedFragment instanceof DrawerItemBaseFragment) && !isDrawerLocked) {
            clearBackStack();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContent, firstTierFragment, firstTierFragment.getTagText());
        ft.commit();
    }

    public void openDrawer() {
        drawer.openDrawer(Gravity.LEFT);
    }

    public void closeDrawer() {
        if(drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        }
    }


    private void lockDrawer() {
        if(isDrawerLocked) {
            isDrawerLocked = false;
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    private void unlockDrawer() {
        if(!isDrawerLocked) {
            isDrawerLocked = true;
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    @Override
    public void setSelectedFragment(BaseFragment fragment) {
        this.selectedFragment = selectedFragment;

        if(selectedFragment instanceof DrawerItemBaseFragment) {
            // If foreground fragment is drawer item, unlock drawer
            unlockDrawer();
        } else {
            // If foreground fragment is not drawer item, lock drawer
//			lockDrawer();
        }
    }


    @Override
    public void addFragment(BaseFragment fragment, boolean withAnimation) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if(withAnimation) {
            // TO ENABLE FRAGMENT ANIMATION
            // Format: setCustomAnimations(old_frag_exit, new_frag_enter, old_frag_enter, new_frag_exit);
            ft.setCustomAnimations(R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_left, R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_right);
        }

        ft.replace(R.id.flContent, fragment, fragment.getTagText());
        ft.addToBackStack(fragment.getTagText());
        ft.commit();

    }

    /**
     * Clears transaction backstack. In this case, after this method
     * all (or any) extended fragment(s) will get removed and
     * view will resort back to current drawer-item fragment.
     *
     * Will only be useful when we are not locking the drawer while in
     * extended fragments and can switch between drawer-item fragments
     * from anywhere in the app.
     */
    private void clearBackStack() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    @Override
    public void popBackStack() {
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void popBackStackTillTag(String tag) {
        getSupportFragmentManager().popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


    @Override
    public void setSelectedDrawerItem(DrawerItemBaseFragment fragment) {
        this.selectedDrawerItemFragment = fragment;
    }

    /**
     * Just to demonstrate how it is transaction backstack rather than fragment
     * backstack. Not a method to be used normally.
     */
    @Override
    public void addMultipleFragments(BaseFragment[] fragments) {
        // Initialize a Fragment Transaction.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Record all steps for the transaction.
        for(int i = 0 ; i < fragments.length ; i++) {
            ft.setCustomAnimations(R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_left, R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_right);
            ft.replace(R.id.flContent, fragments[i], fragments[i].getTagText());
        }

        // Add the transaction to backStack with tag of first added fragment
        ft.addToBackStack(fragments[0].getTagText());

        // Commit the transaction.
        ft.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();
        //Register to receive updates in activity only when activity is visible
        LocalBroadcastManager.getInstance(this).registerReceiver(mInternalLocationReceiver, new
                IntentFilter("googleLocation"));
    }
    @Override
    protected void onPause(){
        super.onPause();

        //Unregister to stop receiving updates in activity when it is not visible.
        //NOTE: You will still receive updates even if this activity is killed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mInternalLocationReceiver);
    }
    //Helper method to get updates
    private void requestUpdates(){
        startService(new Intent(this, LocationService.class).putExtra("request", true));
    }
    //Helper method to stop updates
    private void stopUpdates(){
        startService(new Intent(this, LocationService.class).putExtra("remove", true));
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopUpdates();
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void showListDetail(GymItemDto item) {
//        GymDetailFragment gymDetailFragment = new GymDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("gymItem",item);

        Intent intent = new Intent(MainActivity.this,DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

//        gymDetailFragment.setArguments(bundle);

//        addFragment(gymDetailFragment,true);
    }

    @Override
    public void showListDetailItem(GymListItem item) {

        GymDetailFragment gymDetailFragment = new GymDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("gymItem",item);

        gymDetailFragment.setArguments(bundle);

        addFragment(gymDetailFragment,true);

    }

}
