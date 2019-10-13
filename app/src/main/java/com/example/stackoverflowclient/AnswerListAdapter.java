package com.example.stackoverflowclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stackoverflowclient.data.Answer;
import com.example.stackoverflowclient.utility.DateUtil;

import org.jsoup.Jsoup;

import java.util.List;

public class AnswerListAdapter extends RecyclerView.Adapter<AnswerListAdapter.AnswerViewHolder> {

    private List<Answer> mData;

    public AnswerListAdapter (List<Answer> list) {
        mData = list;
    }


    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.answer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        AnswerViewHolder answerViewHolder = new AnswerViewHolder(view);
        return answerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Answer currentAnswer = mData.get(position);

        holder.answerScore.setText(String.valueOf(currentAnswer.getScore()));
        holder.answerName.setText(currentAnswer.getOwner().getDisplayName());
        holder.answerDate.setText(DateUtil.toNormalDate(currentAnswer.getCreationDate()));
        holder.answerBody.setText(Jsoup.parse(currentAnswer.getBody()).text());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class AnswerViewHolder extends RecyclerView.ViewHolder{
        TextView answerBody;
        TextView answerDate;
        TextView answerName;
        TextView answerScore;

        AnswerViewHolder(View view) {
            super(view);
            answerBody = view.findViewById(R.id.tv_answer_body_item);
            answerDate = view.findViewById(R.id.tv_date_answer_item);
            answerName = view.findViewById(R.id.tv_name_answer_item);
            answerScore = view.findViewById(R.id.tv_scrore_item);
        }

    }
}
