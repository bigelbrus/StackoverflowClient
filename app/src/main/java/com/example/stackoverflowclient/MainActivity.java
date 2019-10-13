package com.example.stackoverflowclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.stackoverflowclient.data.ListWrapper;
import com.example.stackoverflowclient.data.Network;
import com.example.stackoverflowclient.data.Owner;
import com.example.stackoverflowclient.data.Question;
import com.example.stackoverflowclient.utility.DateUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements QuestionListAdapter.ListItemClickListener {

    private EditText mSearchFieldEditText;
    private ProgressBar mProgressBar;
    private TextView mErrorMessageTextView;
    private RecyclerView mQuestionRecyclerView;
    private List<Question> mData;
    private SaveFragment mSaveFragment;

    public final static String EXTRA_QUESTION_TITLE = "question_title";
    public final static String EXTRA_AVATAR_ADDRESS = "avatar_address";
    public final static String EXTRA_QUESTION_DATE = "question_date";
    public final static String EXTRA_QUESTION_ID = "question_id";
    public final static String EXTRA_QUESTION_NAME = "question_name";
    public final static String EXTRA_QUESTION_ANSWERS_COUNT = "question_answers";
    public final static String EXTRA_QUESTION_FULL_TEXT = "question_full_text";
    public final static String EXTRA_QUESTION_OWNER_LINK = "question_owner_link";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchFieldEditText = findViewById(R.id.et_question);
        mProgressBar = findViewById(R.id.pb_fetch_data);
        mErrorMessageTextView = findViewById(R.id.tv_error);
        mQuestionRecyclerView = findViewById(R.id.rv_questions_results);

        mQuestionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mQuestionRecyclerView.setHasFixedSize(true);

        mSaveFragment = (SaveFragment) getSupportFragmentManager().findFragmentByTag("SAVE_FRAGMENT");

        if (mSaveFragment != null) {
            mData = mSaveFragment.getmData();
            mQuestionRecyclerView.setAdapter(new QuestionListAdapter(mData, MainActivity.this,MainActivity.this));
        } else {mData = new ArrayList<>();
            mSaveFragment = new SaveFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(mSaveFragment, "SAVE_FRAGMENT")
                    .commit();
        }

    }

    @Override
    protected void onPause() {
        mSaveFragment.setmData(mData);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                hideData();
                mProgressBar.setVisibility(View.VISIBLE);
                makeSearch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showData() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mQuestionRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
    }

    public void showError() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mQuestionRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    public void hideData() {
        mQuestionRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void makeSearch() {
        Network.getInstance()
                .getJSONApi()
                .getQuestionsWithTextInTitle(mSearchFieldEditText.getText().toString())
                .enqueue(new Callback<ListWrapper<Question>>() {

                    @Override
                    public void onResponse(Call<ListWrapper<Question>> call, Response<ListWrapper<Question>> response) {
                        if (response.isSuccessful()) {
                            showData();
                            List<Question> data = new ArrayList<>();
                            data.addAll(response.body().items);
                            mData = data;
                            mQuestionRecyclerView.setAdapter(new QuestionListAdapter(data, MainActivity.this,MainActivity.this));
                        } else {
                            showError();
                        }
                    }

                    @Override
                    public void onFailure(Call<ListWrapper<Question>> call, Throwable t) {
                        showError();
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent detailActivityIntent = new Intent(this,DetailActivity.class);
        Question currentQuestion = mData.get(clickedItemIndex);
        Owner questionOwner = currentQuestion.getOwner();

        detailActivityIntent.putExtra(EXTRA_QUESTION_TITLE,currentQuestion.getTitle());
        detailActivityIntent.putExtra(EXTRA_QUESTION_NAME,questionOwner.getDisplayName());
        detailActivityIntent.putExtra(EXTRA_QUESTION_DATE,
                DateUtil.toNormalDate(currentQuestion.getCreationDate()));
        detailActivityIntent.putExtra(EXTRA_QUESTION_FULL_TEXT,currentQuestion.getBody());
        detailActivityIntent.putExtra(EXTRA_AVATAR_ADDRESS,questionOwner.getProfileImage());
        detailActivityIntent.putExtra(EXTRA_QUESTION_ANSWERS_COUNT,currentQuestion.getAnswerCount());
        detailActivityIntent.putExtra(EXTRA_QUESTION_ID,currentQuestion.getQuestionId());
        detailActivityIntent.putExtra(EXTRA_QUESTION_OWNER_LINK,questionOwner.getLink());

        startActivity(detailActivityIntent);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);


    }
}
