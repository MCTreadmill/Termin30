package com.example.androiddevelopment.actorapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.androiddevelopment.actorapp.R;
import com.example.androiddevelopment.actorapp.adapters.DrawerAdapter;
import com.example.androiddevelopment.actorapp.db.ORMLightHelper;
import com.example.androiddevelopment.actorapp.db.model.Actor;
import com.example.androiddevelopment.actorapp.dialogs.AboutDialog;
import com.example.androiddevelopment.actorapp.model.NavigationItem;
import com.example.androiddevelopment.actorapp.preferences.PreferenceClass;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by androiddevelopment on 20.11.17..
 */

public class ActorActivity extends AppCompatActivity{

    private ORMLightHelper databaseHelper;

    public static String ACTOR_KEY = "ACTOR_KEY";

    private SharedPreferences prefs;

    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_status";

    // Attributes used by NavigationDrawer
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout drawerPane;
    private CharSequence drawerTitle;
    private ArrayList<NavigationItem> drawerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final ListView listView = (ListView) findViewById(R.id.lv_activity_actor);

        try {
            List<Actor> actorList = getDatabaseHelper().getActorDao().queryForAll();

            ListAdapter adapter = new ArrayAdapter<>(ActorActivity.this, R.layout.list_item, actorList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Actor actor = (Actor) listView.getItemAtPosition(position);

                    Intent intent = new Intent(ActorActivity.this, DetailActivity.class);
                    intent.putExtra(ACTOR_KEY, actor.getmId());
                    startActivity(intent);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Manages NavigationDrawer

        // Populates a list of NavigationDrawer items
        drawerItems.add(new NavigationItem("Settings", "Adjust some Preferences", R.drawable.ic_settings));
        drawerItems.add(new NavigationItem("About", "Learn about the author", R.drawable.ic_about));

        drawerTitle = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerList = (ListView) findViewById(R.id.navList);

        //Populates NavigtionDrawer with options
        drawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        DrawerAdapter adapter = new DrawerAdapter(this, drawerItems);

        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent settings = new Intent(ActorActivity.this, PreferenceClass.class);
                    startActivity(settings);
                }
                if (position == 1) {
                    android.support.v7.app.AlertDialog alertDialog = new AboutDialog(ActorActivity.this).prepareDialog();
                    alertDialog.show();
                }
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();    // Creates call to onPrepareOptionsMenu()
            }
        };
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }


    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }

    private void refresh() {
        ListView listView = (ListView) findViewById(R.id.lv_activity_actor);

        if (listView != null) {
            ArrayAdapter<Actor> adapter = (ArrayAdapter<Actor>) listView.getAdapter();

            if (adapter != null) {
                try {
                    adapter.clear();

                    List<Actor> actorList = getDatabaseHelper().getActorDao().queryForAll();

                    adapter.addAll(actorList);
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showStatusMesage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_notification_small);
        mBuilder.setContentTitle("Pripremni test");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_big);

        mBuilder.setLargeIcon(bm);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_new_actor:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_actor_dialog);

                Button add = (Button) dialog.findViewById(R.id.btn_dialog_actor_add);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) dialog.findViewById(R.id.et_dialog_actor_name);
                        EditText description = (EditText) dialog.findViewById(R.id.et_dialog_actor_description);
                        RatingBar rating = (RatingBar) dialog.findViewById(R.id.rb_dialog_actor_rating);
                        EditText dob = (EditText) dialog.findViewById(R.id.et_dialog_actor_date);

                        Actor actor = new Actor();
                        actor.setmName(name.getText().toString());
                        actor.setmDescription(description.getText().toString());
                        actor.setmRating(rating.getRating());
                        actor.setmDate(dob.getText().toString());

                        try {
                            getDatabaseHelper().getActorDao().create(actor);

                            //checking preferences
                            boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
                            boolean status = prefs.getBoolean(NOTIF_STATUS, false);
                            if (toast){
                                Toast.makeText(ActorActivity.this, "Added new actor", Toast.LENGTH_SHORT).show();
                            }

                            if (status){
                                showStatusMesage("Added new actor");
                            }

                            //REFRESH
                            refresh();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;
            case R.id.about:
                android.support.v7.app.AlertDialog alertDialog = new AboutDialog(this).prepareDialog();
                alertDialog.show();
                break;
            case R.id.preferences:
                startActivity(new Intent(ActorActivity.this, PreferenceClass.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    //Method which communicates with the database
    public ORMLightHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(ActorActivity.this, ORMLightHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Free resources after done with database work
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
