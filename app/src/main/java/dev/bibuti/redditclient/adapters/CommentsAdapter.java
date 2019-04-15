package dev.bibuti.redditclient.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import dev.bibuti.redditclient.R;
import dev.bibuti.redditclient.RedditHelper;
import dev.bibuti.redditclient.network.models.frontpageresponse.ChildrenData;
import dev.bibuti.redditclient.network.models.frontpageresponse.RedditResponse;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsVH> {

    private List<RedditResponse> redditResponseList;
    private OnCommentsAdapterItemClickListener onCommentsAdapterItemClickListener;

    public CommentsAdapter(List<RedditResponse> redditResponseList, OnCommentsAdapterItemClickListener onCommentsAdapterItemClickListener) {
        this.redditResponseList = redditResponseList;
        this.onCommentsAdapterItemClickListener = onCommentsAdapterItemClickListener;
    }

    @NonNull
    @Override
    public CommentsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_comment_layout, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CommentsVH holder, int position) {

        if (redditResponseList.size() >= 2) {

            ChildrenData commentsResponse = redditResponseList.get(1).getData().getChildrenList().get(position).getChildrenData();

            holder.itemView.setVisibility(View.VISIBLE);

            holder.commentAuthorCV.setText("" + commentsResponse.getAuthor());
            holder.commentBodyTV.setText("" + HtmlCompat.fromHtml(commentsResponse.getCommentBody(), HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS));
            holder.commentsVotesTV.setText("" + RedditHelper.numberWithSuffix(Long.valueOf(commentsResponse.getScore())));

            holder.commentAuthorCV.setOnClickListener(v -> onCommentsAdapterItemClickListener.onCommentUserNameClicked(commentsResponse));

        } else {

            holder.itemView.setVisibility(View.GONE);
            RedditHelper.log("No Comments");

        }

    }

    @Override
    public int getItemCount() {

        if (redditResponseList.size() >= 2)
            return redditResponseList.get(1).getData().getChildrenList().size();
        else
            return 0;

    }

    public interface OnCommentsAdapterItemClickListener {
        void onCommentUserNameClicked(ChildrenData childrenData);
    }

    class CommentsVH extends RecyclerView.ViewHolder {

        @BindView(R.id.commentAuthorCV)
        Chip commentAuthorCV;
        @BindView(R.id.commentBodyTV)
        TextView commentBodyTV;
        @BindView(R.id.commentsVotesTV)
        TextView commentsVotesTV;

        CommentsVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
