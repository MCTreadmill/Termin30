package com.example.androiddevelopment.actorapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.example.androiddevelopment.actorapp.R;
import com.example.androiddevelopment.actorapp.db.ORMLightHelper;
import com.example.androiddevelopment.actorapp.db.model.Actor;
import com.example.androiddevelopment.actorapp.dialogs.AboutDialog;
import com.example.androiddevelopment.actorapp.preferences.PreferenceClass;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by androiddevelopment on 20.11.17..
 */

public class ActorActivity extends AppCompatActivity{

    private ORMLightHelper databaseHelper;
    public static String ACTOR_KEY = "ACTOR_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

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
                            //boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
                            //boolean status = prefs.getBoolean(NOTIF_STATUS, false);
                            /*if (toast){
                                Toast.makeText(PripremaListActivity.this, "Added new actor", Toast.LENGTH_SHORT).show();
                            }

                            if (status){
                                showStatusMesage("Added new actor");
                            }*/

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
