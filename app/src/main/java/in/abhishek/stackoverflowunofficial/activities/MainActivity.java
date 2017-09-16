package in.abhishek.stackoverflowunofficial.activities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.abhishek.stackoverflowunofficial.R;
import in.abhishek.stackoverflowunofficial.adapters.QuestionAdapter;
import in.abhishek.stackoverflowunofficial.data.DbHelper;
import in.abhishek.stackoverflowunofficial.fragments.NetworkFragment;
import in.abhishek.stackoverflowunofficial.interfaces.NetworkCallback;
import in.abhishek.stackoverflowunofficial.models.Question;
import in.abhishek.stackoverflowunofficial.views.DividerItemDecoration;

public class MainActivity extends AppCompatActivity implements NetworkCallback {


    private static final String TAG = "MainActivity";
    private NetworkFragment mNetworkFragment;
    private DbHelper mDbHelper;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new DbHelper(getApplicationContext());

        EditText editText = (EditText) findViewById(R.id.editText_search);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mNetworkFragment.fetchResult(v.getText().toString());
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });

        mNetworkFragment = NetworkFragment.getInstance(getFragmentManager());


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_questions);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider_home));

    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    @Override
    public void onError(int errorCode, String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResult(String result) {
        ArrayList<Question> questions = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            if(jsonArray.length() == 0) {
                Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show();
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject questionObj = jsonArray.getJSONObject(i);
                    String title = questionObj.getString(Question.COLUMN_NAME_TITLE);
                    JSONArray tagArray = questionObj.getJSONArray(Question.COLUMN_NAME_TAGS);

                    Log.d(TAG, questionObj.toString());

                    String tags = "";
                    for (int j = 0; j < tagArray.length(); j++) {
                        tags += tagArray.getString(j) + " ";
                    }

                    int answerCount = questionObj.getInt(Question.COLUMN_NAME_ANSWER_COUNT);
                    int upVoteCount = questionObj.getInt(Question.COLUMN_NAME_UP_VOTE_COUNT);

                    // Sometimes API doesn't return question_id
                    long id;
                    try {
                        id = questionObj.getLong("question_id");
                    } catch (JSONException e) {
                        id = -1;
                    }

                    Question question = new Question(id, title, tags, upVoteCount, answerCount);
                    questions.add(question);

                    if (id != -1 && Question.getQuestionById(db, id) == null) {
                        long newId = question.insertIntoDb(db);
                        Log.d(TAG, "Inserting in DB: " + id + " new id: " + newId);
                    } else {
                        Log.d(TAG, "Found in DB: " + id);
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "i: " + i);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        QuestionAdapter questionAdapter = new QuestionAdapter(questions);
        mRecyclerView.setAdapter(questionAdapter);
    }

    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }
}
