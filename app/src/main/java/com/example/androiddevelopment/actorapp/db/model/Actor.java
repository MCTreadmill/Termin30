package com.example.androiddevelopment.actorapp.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.List;

/**
 * Created by androiddevelopment on 20.11.17..
 */

@DatabaseTable(tableName = Actor.TABLE_NAME)
public class Actor {

    public static final String TABLE_NAME = "actors";

    public static final String FIELD_NAME_ID     = "id";
    public static final String FIELD_NAME_NAME   = "name";
    public static final String FIELD_NAME_DESCRIPTION   = "description";
    public static final String FIELD_NAME_RATING   = "rating";
    public static final String FIELD_NAME_DATE   = "date";
    public static final String FIELD_NAME_MOVIES = "movies";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_NAME_NAME)
    private String mName;

    @DatabaseField(columnName = FIELD_NAME_DESCRIPTION)
    private String mDescription;

    @DatabaseField(columnName = FIELD_NAME_RATING)
    private Float mRating;

    @DatabaseField(columnName = FIELD_NAME_DATE)
    private String mDate;

    //Don't forget to changed from databasefield to foreigncollectionfield in order
    //for ormlight to understand where to store field movie
    @ForeignCollectionField(columnName = FIELD_NAME_MOVIES, eager = true)
    private ForeignCollection<Movie> movies;

    public Actor(){

    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Float getmRating() {
        return mRating;
    }

    public void setmRating(Float mRating) {
        this.mRating = mRating;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public ForeignCollection<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ForeignCollection<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public String toString() {
        return mName;
    }
}
