package com.think42lab.arangam;


import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.think42lab.arangam.Adapters.ScheduleAdapter;
import com.think42lab.arangam.model.Segment;
import com.think42lab.arangam.model.Venues;
import com.think42lab.arangam.util.Mixp;
import com.think42lab.arangam.util.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NotificationListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
//    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    Context context;
    private ViewPager mViewPager;
    public ArrayList<Venues> VenueList = new ArrayList<>();

    private ImageView calendar, ticket, profile, artists, favourites;
    ScheduleAdapter customAdapter;
    private TextView txvFavoriteStatus;

    private static int favUnFav = 0;

    public static String projectToken = "0d8458a9f052bab55de68853fa787cfd"; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad"


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calendar = (ImageView) findViewById(R.id.calendar);
        //music = (ImageView) findViewById(R.id.music);
        favourites = (ImageView) findViewById(R.id.favourites);
        //  ticket = (ImageView) findViewById(R.id.ticket);
        artists = (ImageView) findViewById(R.id.artists);
        //  profile = (ImageView) findViewById(R.id.profile);
        txvFavoriteStatus = (TextView) findViewById(R.id.txvFavoritesStatus);
        calendar.setOnClickListener(this);
        favourites.setOnClickListener(this);
        //  ticket.setOnClickListener(this);
        artists.setOnClickListener(this);
       /* profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                profile.setImageDrawable(getDrawable(R.mipmap.profile_active));
                startActivity(intent);
            }
        });*/

        callFragment("Schedule");

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("customfavoriteunfavorite"));
    }

    Fragment fragment = null;

    private void callFragment(String value) {


        favourites.setImageDrawable(getDrawable(R.mipmap.favorites_inactive));
        // profile.setImageDrawable(getDrawable(R.mipmap.profile_inactive));
        // ticket.setImageDrawable(getDrawable(R.mipmap.ticket));
        artists.setImageDrawable(getDrawable(R.mipmap.artistes_inactive));
        calendar.setImageDrawable(getDrawable(R.mipmap.schedule_inactive));
        switch (value) {
            /*case "Tickets":
                fragment = new BookedFragment();
                //ticket.setColorFilter(getResources().getColor(R.color.buttonPress))
                ticket.setImageDrawable(getDrawable(R.mipmap.ticket_selected));

                break;*/
            case "Schedule":
                fragment = new ScheduleFragment();
                calendar.setImageDrawable(getDrawable(R.mipmap.schedule_active));
                //calendar.setColorFilter(getResources().getColor(R.color.buttonPress));
                try {
                    JSONObject props = new JSONObject();
                    props.put("Segments_Menu_Visited", "Segments_Menu_Visited");
//            props.put("Logged in", false);
                    MixpanelAPI.getInstance(this, MainActivity.projectToken).track("Segments_Menu_Visited", props);
                } catch (JSONException e) {
                    Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                }
                break;
            case "Favourites":
                txvFavoriteStatus.setText(Integer.toString(0));
                txvFavoriteStatus.setVisibility(View.INVISIBLE);
                fragment = new FavouriteFragment();
                favourites.setImageDrawable(getDrawable(R.mipmap.favorites_active));
                favUnFav = 0;
                txvFavoriteStatus.setText(Integer.toString(0));
                try {
                    JSONObject props = new JSONObject();
                    props.put("Favorites_Menu_Visited", "Favorites_Menu_Visited");
//            props.put("Logged in", false);
                    MixpanelAPI.getInstance(this, MainActivity.projectToken).track("Favorites_Menu_Visited", props);
                } catch (JSONException e) {
                    Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                }
                break;
            case "Artistes":
                fragment = new ArtistFragment();
                artists.setImageDrawable(getDrawable(R.mipmap.artistes_active));
                try {
                    JSONObject props = new JSONObject();
                    props.put("Artsistes_Menu_Selected", "Artsistes_Menu_Selected");
//            props.put("Logged in", false);
                    MixpanelAPI.getInstance(this, MainActivity.projectToken).track("Artsistes_Menu_Selected", props);
                } catch (JSONException e) {
                    Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                }
                //artists.setColorFilter(getResources().getColor(R.color.buttonPress));
        }

        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle(value.toString());
            getSupportActionBar().setTitle(value.toString().toUpperCase());
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

        try {
            JSONObject props = new JSONObject();
            props.put("Artsistes_Menu_Selected", "Artsistes_Menu_Selected");
//            props.put("Logged in", false);
            MixpanelAPI.getInstance(this, MainActivity.projectToken).track("Artsistes_Menu_Selected", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    // Our handler for received Intents. This will be called whenever an Intent
// with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            final String action = intent.getAction();

            if (action != null && action.equals("customfavoriteunfavorite")) {

                String message = intent.getStringExtra("message");
                Log.d("receiver", "Got message: " + message);

                if (message.equalsIgnoreCase("favorite")) {
                    notifyFavorites(1);
                } else if (message.equalsIgnoreCase("unfavorite")) {
                    notifyFavorites(-1);
                }

            }


//            Toast.makeText(MainActivity.this, "Main Called", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.share:
//                Intent t = new Intent(Intent.ACTION_SEND);
//                t.setType("text/plain");
//                t.putExtra(Intent.EXTRA_SUBJECT, "Arangam");
//                String sarangam = "\n HI,\n" +
//                        "\n" +
//                        "I have use Arangam for booking carnatic music concerts.\n" +
//                        "It's free download from...\n";
//                sarangam = sarangam + "";
//                t.putExtra(Intent.EXTRA_TEXT, sarangam);
//                startActivity(Intent.createChooser(t, "choose one"));
//                return true;
//            case R.id.rateus:
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.arangam")));
//                } catch (ActivityNotFoundException e) {
//
//                }
//
//                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.calendar:
                callFragment("Schedule");
                break;
           /* case R.id.ticket:
                callFragment("Tickets");
                break;*/
            case R.id.artists:
                callFragment("Artistes");
                break;
            case R.id.favourites:
                favUnFav = 0;
                callFragment("Favourites");
                break;
        }

    }

    @Override
    public void notifyFavorites(int count) {

        if (favUnFav + count >= 0) {
            favUnFav = favUnFav + count; //
        }

        if (favUnFav <= 0) {
            txvFavoriteStatus.setVisibility(View.INVISIBLE);
            favUnFav = 0;
        } else {
            txvFavoriteStatus.setVisibility(View.VISIBLE);

            txvFavoriteStatus.setText(Integer.toString(favUnFav));
        }
//        Toast.makeText(MainActivity.this, "" + favUnFav + "    " + count, Toast.LENGTH_SHORT).show();
    }


}
