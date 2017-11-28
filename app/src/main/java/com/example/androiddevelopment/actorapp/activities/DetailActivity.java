package com.example.androiddevelopment.actorapp.activities;

import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddevelopment.actorapp.R;
import com.example.androiddevelopment.actorapp.db.ORMLightHelper;
import com.example.androiddevelopment.actorapp.db.model.Actor;
import com.example.androiddevelopment.actorapp.db.model.Movie;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by androiddevelopment on 20.11.17..
 */

public class DetailActivity extends AppCompatActivity {

    private ORMLightHelper databaseHelper;
    private Actor actor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        int key = getIntent().getExtras().getInt(ActorActivity.ACTOR_KEY);

        try {
            actor = getDatabaseHelper().getActorDao().queryForId(key);

            TextView name = (TextView) findViewById(R.id.tv_detail_name);
            TextView description = (TextView) findViewById(R.id.tv_detail_description);
            TextView dob = (TextView) findViewById(R.id.tv_detail_dob);
            RatingBar rating = (RatingBar) findViewById(R.id.rb_detail_rating);

            name.setText(actor.getmName());
            description.setText(actor.getmDescription());
            dob.setText(actor.getmDate());
            rating.setRating(actor.getmRating());


        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listView = (ListView) findViewById(R.id.lv_activity_detail);

        try {
            List<Movie> movieList = getDatabaseHelper().getMovieDao().queryBuilder()
                    .where()
                    .eq(Movie.FIELD_NAME_ACTOR, actor.getmId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, movieList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie movie = (Movie) listView.getItemAtPosition(position);
                    Toast.makeText(DetailActivity.this, movie.getmName(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Method which communicates with the database
    public ORMLightHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(DetailActivity.this, ORMLightHelper.class);
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

    private void refresh() {
        ListView listView = (ListView) findViewById(R.id.lv_activity_detail);

        if (listView != null) {
            ArrayAdapter<Movie> adapter = (ArrayAdapter<Movie>) listView.getAdapter();

            if (adapter != null) {
                try {
                    adapter.clear();

                    List<Movie> movieList = getDatabaseHelper().getMovieDao().queryBuilder()
                            .where()
                            .eq(Movie.FIELD_NAME_ACTOR, actor.getmId())
                            .query();

                    adapter.addAll(movieList);
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_movie:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_movie_dialog);

                Button add = (Button) dialog.findViewById(R.id.btn_dialog_movie_add);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) dialog.findViewById(R.id.et_dialog_movie_name);

                        Movie movie = new Movie();
                        movie.setmName(name.getText().toString());
                        movie.setmActor(actor);

                        try {
                            getDatabaseHelper().getMovieDao().create(movie);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        refresh();

                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;

            case R.id.edit_actor:
                final Dialog actorDialog = new Dialog(this);
                actorDialog.setContentView(R.layout.edit_actor_dialog);

                Button editActor = (Button) actorDialog.findViewById(R.id.btn_edit_dialog_actor_add);
                editActor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) actorDialog.findViewById(R.id.et_edit_dialog_actor_name);
                        EditText description = (EditText) actorDialog.findViewById(R.id.et_edit_dialog_actor_description);
                        RatingBar rating = (RatingBar) actorDialog.findViewById(R.id.rb_edit_dialog_actor_rating);
                        EditText dob = (EditText) actorDialog.findViewById(R.id.et_edit_dialog_actor_date);

                        actor.setmName(name.getText().toString());
                        actor.setmDescription(description.getText().toString());
                        actor.setmRating(rating.getRating());
                        actor.setmDate(dob.getText().toString());

                        try {
                            getDatabaseHelper().getActorDao().update(actor);

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
                        actorDialog.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                });
                actorDialog.show();

                break;
            case R.id.remove_actor:
                try {
                    getDatabaseHelper().getActorDao().delete(actor);
                    finish();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    /*
                break;
            case R.id.priprema_remove:
                try {
                    getDatabaseHelper().getActorDao().delete(a);

                    showMessage("Actor deleted");

                    finish(); //moramo pozvati da bi se vratili na prethodnu aktivnost
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
     */
}
