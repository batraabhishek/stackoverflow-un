package in.abhishek.stackoverflowunofficial.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.abhishek.stackoverflowunofficial.R;
import in.abhishek.stackoverflowunofficial.models.Question;

/**
 * Created by abhishek on 16/09/17
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private ArrayList<Question> mQuestions;

    public QuestionAdapter(ArrayList<Question> questions) {
        mQuestions = questions;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_question, parent, false);

        return new QuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        Question question = mQuestions.get(position);

        holder.mQuestionTitle.setText(question.getTitle());
        holder.mTags.setText(question.getTags());
        holder.mAnswerCount.setText(String.valueOf(question.getAnsweredCount()));
        holder.mUpVoteCount.setText(String.valueOf(question.getUpVoteCount()));
    }


    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView mQuestionTitle;
        TextView mTags;
        TextView mUpVoteCount;
        TextView mAnswerCount;

        QuestionViewHolder(View itemView) {
            super(itemView);
            mQuestionTitle = itemView.findViewById(R.id.textview_title);
            mTags = itemView.findViewById(R.id.textview_tags);
            mUpVoteCount = itemView.findViewById(R.id.textview_upvote_count);
            mAnswerCount = itemView.findViewById(R.id.textview_answer_count);

        }
    }
}
