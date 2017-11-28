package com.example.androiddevelopment.actorapp.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by androiddevelopment on 20.11.17..
 */

@DatabaseTable(tableName = Movie.TABLE_NAME)
public class Movie {

    public static final String TABLE_NAME = "movies";

    public static final String FIELD_NAME_ID     = "id";
    public static final String FIELD_NAME_NAME   = "name";
    public static final String FIELD_NAME_ACTOR  = "actor";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_NAME_NAME)
    private String mName;

    @DatabaseField(columnName = FIELD_NAME_ACTOR, foreign = true, foreignAutoRefresh = true)
    private Actor mActor;

    public Movie() {

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

    public Actor getmActor() {
        return mActor;
    }

    public void setmActor(Actor mActor) {
        this.mActor = mActor;
    }

    @Override
    public String toString() {
        return mName;
    }
}
