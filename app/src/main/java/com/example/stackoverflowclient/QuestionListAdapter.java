package com.example.stackoverflowclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stackoverflowclient.utility.DateUtil;
import com.example.stackoverflowclient.data.Owner;
import com.example.stackoverflowclient.data.Question;

import org.jsoup.Jsoup;

import java.util.List;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.QuestionViewHolder> {

    final private ListItemClickListener mOnItemClickListener;
    private List<Question> data;
    private Context context;

    public QuestionListAdapter(List<Question> data, ListItemClickListener listItemClickListener,Context context) {
        mOnItemClickListener = listItemClickListener;
        this.data = data;
        this.context = context;

    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.question_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        QuestionViewHolder viewHolder = new QuestionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question currentQuestion = data.get(position);
        Owner questionOwner = currentQuestion.getOwner();
        String profileImage = questionOwner.getProfileImage();
        holder.titleQuestionText.setText(Jsoup.parse(currentQuestion.getTitle()).text());
        holder.viewCounterText.setText(String.valueOf(currentQuestion.getViewCount()));
        Glide.with(context)
                .load(profileImage)
                .into(holder.avatarView);
        holder.dateText.setText(DateUtil.toNormalDate(currentQuestion.getCreationDate()));
        holder.nameText.setText(Jsoup.parse(questionOwner.getDisplayName()).text());
        holder.answersCountText.setText(String.valueOf(currentQuestion.getAnswerCount()));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView avatarView;
        TextView titleQuestionText;
        TextView viewCounterText;
        TextView dateText;
        TextView nameText;
        TextView answersCountText;

        public QuestionViewHolder(View itemView) {
            super(itemView);

            avatarView = itemView.findViewById(R.id.iv_avatar_item);
            titleQuestionText = itemView.findViewById(R.id.tv_question_item);
            viewCounterText = itemView.findViewById(R.id.tv_counter_item);
            dateText = itemView.findViewById(R.id.tv_date_item);
            nameText = itemView.findViewById(R.id.tv_name_item);
            answersCountText = itemView.findViewById(R.id.tv_answers_count_item);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onListItemClick(getAdapterPosition());
        }

    }
}
