package in.abhishek.stackoverflowunofficial.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by abhishek on 16/09/17
 */

public class Question implements BaseColumns {


    public static final String TABLE_NAME = "questions";
    public static final String COLUMN_NAME_QUESTION_ID = "question_id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_TAGS = "tags";
    public static final String COLUMN_NAME_UP_VOTE_COUNT = "up_vote_count";
    public static final String COLUMN_NAME_ANSWER_COUNT = "answer_count";

    private long mId;
    private long mQuestionId;
    private String mTitle;
    private String mTags;
    private int mUpVoteCount;
    private int mAnsweredCount;

    public Question(long id, String title, String tags, int upVoteCount, int answeredCount) {
        mQuestionId = id;
        mTitle = title;
        mTags = tags;
        mUpVoteCount = upVoteCount;
        mAnsweredCount = answeredCount;
    }

    public long getQuestionId() {
        return mQuestionId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getTags() {
        return mTags;
    }

    public int getUpVoteCount() {
        return mUpVoteCount;
    }

    public int getAnsweredCount() {
        return mAnsweredCount;
    }


    private ContentValues getValueMap() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_QUESTION_ID, mQuestionId);
        values.put(COLUMN_NAME_TITLE, mTitle);
        values.put(COLUMN_NAME_TAGS, mTags);
        values.put(COLUMN_NAME_UP_VOTE_COUNT, mUpVoteCount);
        values.put(COLUMN_NAME_ANSWER_COUNT, mAnsweredCount);
        return values;
    }

    public long insertIntoDb(SQLiteDatabase db) {
        return db.insert(TABLE_NAME, null, getValueMap());
    }

    public static Question getQuestionById(SQLiteDatabase db, long id) {

        Question question = null;
        String selection = COLUMN_NAME_QUESTION_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            long rowId = cursor.getLong(cursor.getColumnIndexOrThrow(_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_TITLE));
            String tags = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_TAGS));
            int upVoteCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_UP_VOTE_COUNT));
            int mAnsweredCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_ANSWER_COUNT));

            question = new Question(rowId, title, tags, upVoteCount, mAnsweredCount);
        }

        cursor.close();
        return question;
    }
}
