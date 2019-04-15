package dev.bibuti.redditclient.adapters;

import android.annotation.SuppressLint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import dev.bibuti.redditclient.R;
import dev.bibuti.redditclient.RedditHelper;
import dev.bibuti.redditclient.network.models.frontpageresponse.Children;

public class RedditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int LOADER = R.layout.single_loader_view;
    private final int CONTENT = R.layout.single_reddit_layout;
    private List<Children> childrenList;
    private OnRedditAdapterItemClickListener onRedditAdapterItemClickListener;

    public RedditAdapter() {
        this.childrenList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == CONTENT) {
            return new RedditViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_reddit_layout, parent, false));
        } else {
            return new LoaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_loader_view, parent, false));
        }

    }

    @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder recyclerViewHolder, int position) {

        Children children = childrenList.get(position);
        //recyclerViewHolder.setIsRecyclable(false);

        switch (recyclerViewHolder.getItemViewType()) {
            case CONTENT:
                RedditViewHolder holder = (RedditViewHolder) recyclerViewHolder;

                //Title..
                if (children.getChildrenData().getTitle() != null) {
                    holder.singleTitleTV.setText("" + children.getChildrenData().getTitle());
                } else if (children.getChildrenData().getCommentBody() != null) {
                    holder.singleTitleTV.setText("" + HtmlCompat.fromHtml(children.getChildrenData().getCommentBody(), HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS));
                } else {
                    holder.singleTitleTV.setText("");
                }

                //Posted By..
                SpannableString spannableString = new SpannableString("Posted By - " + children.getChildrenData().getAuthor() + " at " + RedditHelper.convertUnixToDate(children.getChildrenData().getCreated()));
                ForegroundColorSpan fcs = new ForegroundColorSpan(holder.itemView.getContext().getResources().getColor(R.color.colorAccent));
                spannableString.setSpan(fcs, 12, 12 + children.getChildrenData().getAuthor().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                holder.singlePostedByTV.setText(spannableString);

                //Multi-Check for Video, Gif, Image or Nothing..
                if (children.getChildrenData().getSecureMedia() != null) {
                    //Check If it's video or gif..
                    if (children.getChildrenData().getSecureMedia().getRedditVideo() != null) {

                        holder.singleMediaCV.setVisibility(View.VISIBLE);

                        holder.singlePostIV.setVisibility(View.GONE);
                        holder.singlePostWV.setVisibility(View.VISIBLE);

                        holder.singlePostWV.getSettings().setJavaScriptEnabled(true);
                        holder.singlePostWV.loadUrl(children.getChildrenData().getSecureMedia().getRedditVideo().getScrubberMediaUrl());

                    } else {

                        //No Videos or Gif
                        if (children.getChildrenData().getUrl() != null) {
                            if ((children.getChildrenData().getUrl().contains(".png")) ||
                                    (children.getChildrenData().getUrl().contains(".jpg")) ||
                                    (children.getChildrenData().getUrl().contains(".gif"))) {

                                holder.singleMediaCV.setVisibility(View.VISIBLE);

                                holder.singlePostIV.setVisibility(View.VISIBLE);
                                holder.singlePostWV.setVisibility(View.GONE);

                                Glide.with(holder.itemView.getContext())
                                        .load(children.getChildrenData().getUrl())
                                        .apply(new RequestOptions().placeholder(R.drawable.ic_loader).diskCacheStrategy(DiskCacheStrategy.ALL))
                                        .into(holder.singlePostIV);

                            } else {
                                //Hide View..
                                holder.singleMediaCV.setVisibility(View.GONE);
                            }
                        } else {
                            //Hide View..
                            holder.singleMediaCV.setVisibility(View.GONE);
                        }

                    }

                } else {

                    //So no Videos here..
                    if (children.getChildrenData().getUrl() != null) {
                        if ((children.getChildrenData().getUrl().contains(".png")) ||
                                (children.getChildrenData().getUrl().contains(".jpg")) ||
                                (children.getChildrenData().getUrl().contains(".gif"))) {

                            holder.singleMediaCV.setVisibility(View.VISIBLE);

                            holder.singlePostIV.setVisibility(View.VISIBLE);
                            holder.singlePostWV.setVisibility(View.GONE);

                            Glide.with(holder.itemView.getContext())
                                    .load(children.getChildrenData().getUrl())
                                    .apply(new RequestOptions().placeholder(R.drawable.ic_loader).diskCacheStrategy(DiskCacheStrategy.ALL))
                                    .into(holder.singlePostIV);

                        } else {
                            //Hide View..
                            holder.singleMediaCV.setVisibility(View.GONE);
                        }
                    } else {
                        //Hide View..
                        holder.singleMediaCV.setVisibility(View.GONE);
                    }

                }

                //Share, Comments and Votes
                holder.singleCommentTV.setText("" + RedditHelper.numberWithSuffix(Long.valueOf(children.getChildrenData().getNumComments())));
                holder.singleVotesTV.setText("" + RedditHelper.numberWithSuffix(Long.valueOf(children.getChildrenData().getScore())));

                //Handle Click Listeners
                holder.singleShareTV.setOnClickListener(v -> {

                    if (onRedditAdapterItemClickListener == null) {
                        RedditHelper.log("Listener not Attached.");
                    } else {
                        onRedditAdapterItemClickListener.onRedditAdapterShareButtonClicked(children);
                    }

                });
                holder.singleTitleTV.setOnClickListener(v -> {
                    if (onRedditAdapterItemClickListener == null) {
                        RedditHelper.log("Listener not Attached.");
                    } else {
                        onRedditAdapterItemClickListener.onRedditAdapterItemClicked(children);
                    }
                });
                holder.singlePostedByTV.setOnClickListener(v -> {
                    if (onRedditAdapterItemClickListener == null) {
                        RedditHelper.log("Listener not Attached.");
                    } else {
                        onRedditAdapterItemClickListener.onRedditAdapterItemClicked(children);
                    }
                });
                holder.singleCommentTV.setOnClickListener(v -> {
                    if (onRedditAdapterItemClickListener == null) {
                        RedditHelper.log("Listener not Attached.");
                    } else {
                        onRedditAdapterItemClickListener.onRedditAdapterItemClicked(children);
                    }
                });
                holder.singleVotesTV.setOnClickListener(v -> {
                    if (onRedditAdapterItemClickListener == null) {
                        RedditHelper.log("Listener not Attached.");
                    } else {
                        onRedditAdapterItemClickListener.onRedditAdapterItemClicked(children);
                    }
                });
                holder.singlePostIV.setOnClickListener(v -> {
                    if (onRedditAdapterItemClickListener == null) {
                        RedditHelper.log("Listener not Attached.");
                    } else {
                        onRedditAdapterItemClickListener.onRedditAdapterItemClicked(children);
                    }
                });
                holder.singlePostWV.setOnClickListener(v -> {
                    if (onRedditAdapterItemClickListener == null) {
                        RedditHelper.log("Listener not Attached.");
                    } else {
                        onRedditAdapterItemClickListener.onRedditAdapterItemClicked(children);
                    }
                });

                break;

            default:
                //Default is Loader
                break;
        }

    }

    @Override
    public int getItemCount() {
        return childrenList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return childrenList.get(position) == null ? LOADER : CONTENT;
    }

    public void updateList(List<Children> updatedList) {
        childrenList = updatedList;
        childrenList.add(null); // For Loader..
        notifyDataSetChanged();
    }

    public void addMore(List<Children> updatedList) {

        //First Remove the Loader..
        if (childrenList.get(childrenList.size() - 1) == null) {
            childrenList.remove(childrenList.get(childrenList.size() - 1));
        }
        childrenList.addAll(updatedList);
        childrenList.add(null);
        notifyDataSetChanged();
    }

    public void removeLoader() {
        if (childrenList.isEmpty()) {
            return;
        } else if (childrenList.get(childrenList.size() - 1) == null) {
            childrenList.remove(childrenList.get(childrenList.size() - 1));
        }
        notifyDataSetChanged();
    }

    public void setRedditListener(OnRedditAdapterItemClickListener listenerImplementation) {
        onRedditAdapterItemClickListener = listenerImplementation;
    }

    public interface OnRedditAdapterItemClickListener {
        void onRedditAdapterItemClicked(Children children);

        void onRedditAdapterShareButtonClicked(Children children);
    }

    static class RedditViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.singleTitleTV)
        TextView singleTitleTV;
        @BindView(R.id.singlePostedByTV)
        TextView singlePostedByTV;
        @BindView(R.id.singleShareTV)
        TextView singleShareTV;
        @BindView(R.id.singleCommentTV)
        TextView singleCommentTV;
        @BindView(R.id.singleVotesTV)
        TextView singleVotesTV;
        @BindView(R.id.singlePostIV)
        ImageView singlePostIV;
        @BindView(R.id.singlePostWV)
        WebView singlePostWV;
        @BindView(R.id.singleMediaCV)
        MaterialCardView singleMediaCV;

        RedditViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
