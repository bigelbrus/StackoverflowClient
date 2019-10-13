package com.example.stackoverflowclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.stackoverflowclient.data.Answer;
import com.example.stackoverflowclient.data.ListWrapper;
import com.example.stackoverflowclient.data.Network;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private TextView mQuestionTitleTextView;
    private TextView mFullQuestionTextView;
    private TextView mDateQuestionTextView;
    private TextView mNameQuestionTextView;
    private ImageView mAvatarQuestionImageView;
    private RecyclerView mAnswersRecyclerView;
    private TextView mAnswersCountTextView;
    private int mQuestionId;
    private String mAvatarAddress;
    private String mOwnerQuestionLink;
    private SaveFragment mSaveFragment;
    private List<Answer> mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mQuestionTitleTextView = findViewById(R.id.tv_question_detail);
        mFullQuestionTextView = findViewById(R.id.tv_full_question_detail);
        mDateQuestionTextView = findViewById(R.id.tv_date_question_detail);
        mNameQuestionTextView = findViewById(R.id.tv_name_question_detail);
        mAvatarQuestionImageView = findViewById(R.id.iv_avatar_question_detail);
        mAnswersRecyclerView = findViewById(R.id.rv_answers_result);
        mAnswersCountTextView = findViewById(R.id.tv_answers_count_detail);

        mAnswersRecyclerView.setHasFixedSize(true);
        mAnswersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intentWithExtras = getIntent();

        mQuestionTitleTextView.setText(Jsoup.parse(intentWithExtras.getStringExtra(MainActivity.EXTRA_QUESTION_TITLE)).text());
        mFullQuestionTextView.setText(Jsoup.parse(intentWithExtras.getStringExtra(MainActivity.EXTRA_QUESTION_FULL_TEXT)).text());
        mDateQuestionTextView.setText(intentWithExtras.getStringExtra(MainActivity.EXTRA_QUESTION_DATE));
        mNameQuestionTextView.setText(intentWithExtras.getStringExtra(MainActivity.EXTRA_QUESTION_NAME));
        mAnswersCountTextView.setText(String.valueOf(intentWithExtras.getIntExtra(MainActivity.EXTRA_QUESTION_ANSWERS_COUNT, 0)));
        mQuestionId = intentWithExtras.getIntExtra(MainActivity.EXTRA_QUESTION_ID, 0);
        mAvatarAddress = intentWithExtras.getStringExtra(MainActivity.EXTRA_AVATAR_ADDRESS);
        mOwnerQuestionLink = intentWithExtras.getStringExtra(MainActivity.EXTRA_QUESTION_OWNER_LINK);
    }

    @Override
    protected void onResume() {
        Glide.with(this)
                .load(mAvatarAddress)
                .into(mAvatarQuestionImageView);
        mSaveFragment = (SaveFragment) getSupportFragmentManager().findFragmentByTag("SAVE_FRAGMENT");

        if (mSaveFragment != null) {
            mData = mSaveFragment.getmData();

            mAnswersRecyclerView.setAdapter(new AnswerListAdapter(mData));
        } else {mData = new ArrayList<>();
            mSaveFragment = new SaveFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(mSaveFragment, "SAVE_FRAGMENT")
                    .commit();

            Network.getInstance()
                    .getJSONApi()
                    .getAnswersToQuestion(mQuestionId)
                    .enqueue(new Callback<ListWrapper<Answer>>() {

                        @Override
                        public void onResponse(Call<ListWrapper<Answer>> call, Response<ListWrapper<Answer>> response) {
                            if (response.isSuccessful()) {
                                List<Answer> data = new ArrayList<>();
                                data.addAll(response.body().items);
                                mData = data;
                                mAnswersRecyclerView.setAdapter(new AnswerListAdapter(mData));
                            } else {

                            }


                        }

                        @Override
                        public void onFailure(Call<ListWrapper<Answer>> call, Throwable t) {

                            t.printStackTrace();
                        }
                    });
        }

        super.onResume();
    }

    public void openProfileOnWeb(View view){
        Uri webpage = Uri.parse(mOwnerQuestionLink);
        Intent intent = new Intent(Intent.ACTION_VIEW,webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        mSaveFragment.setmData(mData);
        super.onPause();
    }
}
