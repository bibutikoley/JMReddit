package dev.bibuti.redditclient;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.chip.Chip;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import dev.bibuti.redditclient.adapters.CommentsAdapter;
import dev.bibuti.redditclient.network.RetrofitClient;
import dev.bibuti.redditclient.network.models.frontpageresponse.Children;
import dev.bibuti.redditclient.network.models.frontpageresponse.ChildrenData;
import dev.bibuti.redditclient.network.models.frontpageresponse.RedditResponse;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class RedditFragment extends Fragment
        implements CommentsAdapter.OnCommentsAdapterItemClickListener {

    private static final String TAG = "RedditFragment";
    @BindView(R.id.redditIV)
    ImageView redditIV;
    @BindView(R.id.redditWV)
    WebView redditWV;
    @BindView(R.id.redditMediaFL)
    FrameLayout redditMediaFL;
    @BindView(R.id.redditTitleTV)
    TextView redditTitleTV;
    @BindView(R.id.redditShareTV)
    TextView redditShareTV;
    @BindView(R.id.redditVotesTV)
    TextView redditVotesTV;
    @BindView(R.id.chip)
    Chip redditTypeChip;
    @BindView(R.id.redditCommentsTV)
    TextView redditCommentsTV;
    @BindView(R.id.redditPostedByTV)
    TextView redditPostedByTV;
    @BindView(R.id.redditCommentsRV)
    RecyclerView redditCommentsRV;
    @BindView(R.id.redditCommentsLoaderPB)
    ProgressBar redditCommentsLoaderPB;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Children children;

    public RedditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            children = getArguments().getParcelable("data");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reddit, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint({"SetJavaScriptEnabled", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (children == null) {
            RedditHelper.d(TAG, "Children is Null");
        } else {

            //MultiCheck Video, Gifs, Images..
            if (children.getChildrenData().getSecureMedia() != null) {
                //Check If it's video or gif..
                if (children.getChildrenData().getSecureMedia().getRedditVideo() != null) {

                    redditMediaFL.setVisibility(View.VISIBLE);

                    redditIV.setVisibility(View.GONE);
                    redditWV.setVisibility(View.VISIBLE);

                    redditWV.getSettings().setJavaScriptEnabled(true);
                    redditWV.loadUrl(children.getChildrenData().getSecureMedia().getRedditVideo().getScrubberMediaUrl());

                } else {

                    //No Videos or Gif
                    if (children.getChildrenData().getUrl() != null) {
                        if ((children.getChildrenData().getUrl().contains(".png")) ||
                                (children.getChildrenData().getUrl().contains(".jpg")) ||
                                (children.getChildrenData().getUrl().contains(".gif"))) {

                            redditMediaFL.setVisibility(View.VISIBLE);

                            redditIV.setVisibility(View.VISIBLE);
                            redditWV.setVisibility(View.GONE);

                            Glide.with(this)
                                    .load(children.getChildrenData().getUrl())
                                    .apply(new RequestOptions().placeholder(R.drawable.ic_loader).diskCacheStrategy(DiskCacheStrategy.ALL))
                                    .into(redditIV);

                        } else {
                            //Hide View..
                            redditMediaFL.setVisibility(View.GONE);
                        }
                    } else {
                        //Hide View..
                        redditMediaFL.setVisibility(View.GONE);
                    }

                }

            } else {

                //So no Videos here..
                if (children.getChildrenData().getUrl() != null) {
                    if ((children.getChildrenData().getUrl().contains(".png")) ||
                            (children.getChildrenData().getUrl().contains(".jpg")) ||
                            (children.getChildrenData().getUrl().contains(".gif"))) {

                        redditMediaFL.setVisibility(View.VISIBLE);

                        redditIV.setVisibility(View.VISIBLE);
                        redditWV.setVisibility(View.GONE);

                        Glide.with(this)
                                .load(children.getChildrenData().getUrl())
                                .apply(new RequestOptions().placeholder(R.drawable.ic_loader).diskCacheStrategy(DiskCacheStrategy.ALL))
                                .into(redditIV);

                    } else {
                        //Hide View..
                        redditMediaFL.setVisibility(View.GONE);
                    }
                } else {
                    //Hide View..
                    redditMediaFL.setVisibility(View.GONE);
                }

            }
            ////////////////////////////////

            //redditTitleTV.setText("" + children.getChildrenData().getTitle());
            if (children.getChildrenData().getTitle() != null) {
                redditTitleTV.setText("" + children.getChildrenData().getTitle());
            } else if (children.getChildrenData().getCommentBody() != null) {
                redditTitleTV.setText("" + HtmlCompat.fromHtml(children.getChildrenData().getCommentBody(), HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS));
            } else {
                redditTitleTV.setText("");
            }

            //Posted By..
            SpannableString spannableString = new SpannableString("Posted By - " + children.getChildrenData().getAuthor() + " at " + RedditHelper.convertUnixToDate(children.getChildrenData().getCreated()));
            ForegroundColorSpan fcs = new ForegroundColorSpan(requireContext().getResources().getColor(R.color.colorAccent));
            spannableString.setSpan(fcs, 12, 12 + children.getChildrenData().getAuthor().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            redditPostedByTV.setText(spannableString);

            redditCommentsTV.setText(RedditHelper.numberWithSuffix(Long.valueOf(children.getChildrenData().getNumComments())) + " Comments");
            redditVotesTV.setText("" + RedditHelper.numberWithSuffix(Long.valueOf(children.getChildrenData().getScore())));

            redditTypeChip.setText("" + children.getChildrenData().getSubredditNamePrefixed());

            redditShareTV.setOnClickListener(v -> {

                String uri = "https://reddit.com" + children.getChildrenData().getPermalink();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, uri);
                startActivity(Intent.createChooser(sharingIntent, "Share Reddit via"));

            });

            redditPostedByTV.setOnClickListener(v -> onCommentUserNameClicked(children.getChildrenData()));

            redditTypeChip.setOnClickListener(v -> {

                Bundle bundle = new Bundle();
                bundle.putString("query", redditTypeChip.getText().toString());
                bundle.putBoolean("special", true);
                Navigation.findNavController(requireView()).navigate(R.id.action_redditFragment_to_searchPageFragment, bundle);

            });

            redditCommentsRV.setHasFixedSize(true);
            redditCommentsRV.setLayoutManager(new LinearLayoutManager(requireContext()));
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
            redditCommentsRV.addItemDecoration(dividerItemDecoration);

            fetchCommentsData(children);

        }

    }

    private void fetchCommentsData(Children children) {

        Observer<List<RedditResponse>> observer = new Observer<List<RedditResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                redditCommentsRV.setVisibility(View.INVISIBLE);
                redditCommentsLoaderPB.setVisibility(View.VISIBLE);
                RedditHelper.d(TAG, "onSubscribe() called with: d = [" + d + "]");
            }

            @Override
            public void onNext(List<RedditResponse> redditResponses) {

                CommentsAdapter commentsAdapter = new CommentsAdapter(redditResponses, RedditFragment.this);
                redditCommentsRV.setAdapter(commentsAdapter);

                RedditHelper.d(TAG, "onNext() called with: redditResponses = [" + redditResponses + "]");

            }

            @Override
            public void onError(Throwable e) {
                RedditHelper.d(TAG, "onError() called with: e = [" + e + "]");
                Toast.makeText(requireContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                redditCommentsRV.setVisibility(View.INVISIBLE);
                redditCommentsLoaderPB.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {
                redditCommentsRV.setVisibility(View.VISIBLE);
                redditCommentsLoaderPB.setVisibility(View.INVISIBLE);
                RedditHelper.d(TAG, "onComplete() called");
            }
        };

        RetrofitClient.retrofitEndpoints.getComments(RetrofitClient.URL + children.getChildrenData().getPermalink() + ".json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    public void onCommentUserNameClicked(ChildrenData childrenData) {

        String userName = "user/" + childrenData.getAuthor();

        Bundle bundle = new Bundle();
        bundle.putString("query", userName);
        bundle.putBoolean("special", true);
        Navigation.findNavController(requireView()).navigate(R.id.action_redditFragment_to_searchPageFragment, bundle);

    }
}
