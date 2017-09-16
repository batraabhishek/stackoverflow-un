package in.abhishek.stackoverflowunofficial.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import in.abhishek.stackoverflowunofficial.models.Question;

/**
 * Created by abhishek on 16/09/17
 */

public class DbHelper extends SQLiteOpenHelper {

    private static class QuestionEntry implements BaseColumns {
        private static final String TABLE_NAME = "questions";
        private static final String COLUMN_NAME_TITLE = "title";
        private static final String COLUMN_NAME_TAGS = "tags";
        private static final String COLUMN_NAME_UP_VOTE_COUNT = "up_vote_count";
        private static final String COLUMN_NAME_ANSWER_COUNT = "answer_count";
        private static final String COLUMN_NAME_IS_ANSWERED = "is_answered";
    }


    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "question.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE questions (" +
                    Question._ID + " INTEGER PRIMARY KEY," +
                    Question.COLUMN_NAME_QUESTION_ID + " INTEGER UNIQUE," +
                    Question.COLUMN_NAME_TITLE + " TEXT," +
                    Question.COLUMN_NAME_TAGS + " TEXT," +
                    Question.COLUMN_NAME_UP_VOTE_COUNT + " INTEGER," +
                    Question.COLUMN_NAME_ANSWER_COUNT + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + QuestionEntry.TABLE_NAME;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
