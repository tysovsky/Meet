package tysovsky.meet;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager = null;
    FragmentMyProfile fragmentMyProfile = new FragmentMyProfile();
    FragmentPeopleNearMe fragmentPeopleNearMe = new FragmentPeopleNearMe();
    FragmentMessages fragmentMessages = new FragmentMessages();
    FragmentAbout fragmentAbout = new FragmentAbout();
    FragmentProfile fragmentProfile = new FragmentProfile();

    FloatingActionButton fab = null;


    private GCMClientManager gcmManager = null;
    private String PROJECT_NUMBER = "644026005539";

    private AlarmManager alarmManager = null;
    private PendingIntent meetPendingIntent = null;
    private boolean meetModeEnable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(fabOnCLickListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        gcmManager = new GCMClientManager(this, PROJECT_NUMBER);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        fragmentManager = getFragmentManager();

        gcmManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {

            }
        });

        if(savedInstanceState == null){
            fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.main_container, fragmentPeopleNearMe, FragmentPeopleNearMe.TAG);
            transaction.commit();
        }
        else{
            if(fragmentManager.findFragmentByTag(FragmentMyProfile.TAG) != null){
                fragmentMyProfile = (FragmentMyProfile)getFragmentManager().findFragmentByTag(FragmentMyProfile.TAG);
            }
            if(fragmentManager.findFragmentByTag(FragmentPeopleNearMe.TAG) != null){
                fragmentPeopleNearMe = (FragmentPeopleNearMe)getFragmentManager().findFragmentByTag(FragmentPeopleNearMe.TAG);
            }
            if(fragmentManager.findFragmentByTag(FragmentMessages.TAG) != null){
                fragmentMessages = (FragmentMessages)getFragmentManager().findFragmentByTag(FragmentMessages.TAG);
            }
            if(fragmentManager.findFragmentByTag(FragmentAbout.TAG) != null){
                fragmentAbout = (FragmentAbout)getFragmentManager().findFragmentByTag(FragmentAbout.TAG);
            }
            if(fragmentManager.findFragmentByTag(FragmentProfile.TAG) != null){
                fragmentProfile = (FragmentProfile)getFragmentManager().findFragmentByTag(FragmentProfile.TAG);
            }
        }
    }

    private void startMeetMode(){
        new LocationUpdateHandler().updateLocation(this, true);
        meetPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, LocationUpdateHandler.class), 0);
        int interval = 60000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, meetPendingIntent);
        meetModeEnable = true;
    }

    private void stopMeetMode(){
        if (alarmManager != null) {
            new LocationUpdateHandler().updateLocation(this, false);
            alarmManager.cancel(meetPendingIntent);
            meetModeEnable = false;
        }

    }

    public void profileViewRequested(PeopleNearMe profile){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragmentProfile, FragmentProfile.TAG);
        fragmentTransaction.addToBackStack(FragmentProfile.TAG);
        fragmentTransaction.commit();
    }

    public void showFab(){
        fab.show();
    }

    public void hideFab(){
        fab.hide();
    }

    public FloatingActionButton getFab(){
        return fab;
    }

    private View.OnClickListener fabOnCLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!meetModeEnable){
                //Ask for location permissions
                //Android M and up only
                if(!Utilities.hasPermissions(Utilities.PERMISSIONS_LOCATION, getApplicationContext())){
                    requestPermissions(Utilities.PERMISSIONS_LOCATION, Utilities.LOCATIONS_REQUEST_CODE);
                }

                if(Utilities.hasPermissions(Utilities.PERMISSIONS_LOCATION, getApplicationContext())){
                    startMeetMode();
                    Snackbar.make(view, "Meet mode started!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else{
                    Snackbar.make(view, "Lacking necessary permissions!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
            else{
                stopMeetMode();
                Snackbar.make(view, "Meet mode stopped!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    };

    public void updatePeopleNearMe(ArrayList<PeopleNearMe> peopleNearMe){
        if(fragmentManager.findFragmentByTag(FragmentPeopleNearMe.TAG) != null){
            fragmentPeopleNearMe.updatePeopleNearMe(peopleNearMe);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        synchronized (GCMMessageHandler.CURRENTACTIVITYLOCK){
            GCMMessageHandler.currentActivity = this;
        }
        synchronized (LocationUpdateHandler.CURRENTACTIVITYLOCK){
            LocationUpdateHandler.currentActivity = this;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        synchronized (GCMMessageHandler.CURRENTACTIVITYLOCK){
            GCMMessageHandler.currentActivity = null;
        }
        synchronized (LocationUpdateHandler.CURRENTACTIVITYLOCK){
            LocationUpdateHandler.currentActivity = null;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (id){
            case R.id.nav_profile:
                fragmentTransaction.replace(R.id.main_container, fragmentMyProfile, FragmentMyProfile.TAG);
                fragmentTransaction.commit();
                break;
            case R.id.nav_people:
                fragmentTransaction.replace(R.id.main_container, fragmentPeopleNearMe, FragmentPeopleNearMe.TAG);
                fragmentTransaction.commit();
                break;
            case R.id.nav_messages:
                fragmentTransaction.replace(R.id.main_container, fragmentMessages, FragmentMessages.TAG);
                fragmentTransaction.commit();
                break;
            case R.id.nav_about:
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                //fragmentTransaction.replace(R.id.main_container, fragmentAbout, FragmentAbout.TAG);
                //fragmentTransaction.commit();
                break;

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
