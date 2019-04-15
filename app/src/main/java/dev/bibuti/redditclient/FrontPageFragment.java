package dev.bibuti.redditclient;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import dev.bibuti.redditclient.adapters.RedditAdapter;
import dev.bibuti.redditclient.network.RetrofitClient;
import dev.bibuti.redditclient.network.models.frontpageresponse.Children;
import dev.bibuti.redditclient.network.models.frontpageresponse.RedditResponse;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class FrontPageFragment extends Fragment
        implements RedditAdapter.OnRedditAdapterItemClickListener {

    private static final String TAG = "FrontPageFragment";
    @BindView(R.id.frontPageRV)
    RecyclerView frontPageRV;
    @BindView(R.id.toolbarSearchIV)
    ImageView toolbarSearchIV;
    @BindView(R.id.frontPageSR)
    SwipeRefreshLayout frontPageSR;
    @BindView(R.id.frontLoaderLL)
    LinearLayout frontLoaderLL;
    @BindView(R.id.fb_shimmer)
    ShimmerFrameLayout shimmerFrameLayout;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RedditAdapter mRedditAdapter = new RedditAdapter();

    private LinearLayoutManager linearLayoutManager;
    private String mAfterPage;
    private boolean isLoadMoreCalled = false;
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (linearLayoutManager != null) {
                int totalItemInAdapter = linearLayoutManager.getItemCount();
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                if ((lastVisibleItemPosition == totalItemInAdapter - 1) && (!isLoadMoreCalled) && (mAfterPage != null)) {
                    fetchNextPage(mAfterPage);
                }

            }

        }
    };

    public FrontPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_front_page, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        frontPageRV.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(requireContext());
        frontPageRV.setLayoutManager(linearLayoutManager);
        mRedditAdapter.setRedditListener(FrontPageFragment.this);
        frontPageRV.setAdapter(mRedditAdapter);

        frontPageSR.setOnRefreshListener(() -> {
            frontPageSR.setRefreshing(false);
            fetchInitialData();
        });

        fetchInitialData();

        toolbarSearchIV.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_frontPageFragment_to_searchPageFragment));

        frontPageRV.addOnScrollListener(onScrollListener);

    }

    private void fetchInitialData() {

        Observer<RedditResponse> observer = new Observer<RedditResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                frontPageRV.setVisibility(View.INVISIBLE);
                frontLoaderLL.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNext(RedditResponse redditResponse) {
                mAfterPage = redditResponse.getData().getAfter();
                mRedditAdapter.updateList(redditResponse.getData().getChildrenList());
                if (mAfterPage == null) {
                    mRedditAdapter.removeLoader();
                }
            }

            @Override
            public void onError(Throwable e) {
                RedditHelper.d(TAG, "onError() called with: e = [" + e + "]");
                Toast.makeText(requireContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                frontPageRV.setVisibility(View.INVISIBLE);
                frontLoaderLL.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {
                frontPageRV.setVisibility(View.VISIBLE);
                frontLoaderLL.setVisibility(View.INVISIBLE);
                shimmerFrameLayout.stopShimmer();
            }
        };

        RetrofitClient.retrofitEndpoints.getAllRecentPost()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    private void fetchNextPage(String after) {
        RedditHelper.d(TAG, "fetchNextPage() called with: after = [" + after + "]");

        Observer<RedditResponse> observer = new Observer<RedditResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                isLoadMoreCalled = true;
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(RedditResponse redditResponse) {
                isLoadMoreCalled = false;
                mAfterPage = redditResponse.getData().getAfter();
                mRedditAdapter.addMore(redditResponse.getData().getChildrenList());
                if (mAfterPage == null) {
                    mRedditAdapter.removeLoader();
                }
            }

            @Override
            public void onError(Throwable e) {
                isLoadMoreCalled = false;
                Toast.makeText(requireContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                isLoadMoreCalled = false;
            }
        };

        RetrofitClient.retrofitEndpoints.getNextPage(after)
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
    public void onRedditAdapterItemClicked(Children children) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", children);
        Navigation.findNavController(requireView()).navigate(R.id.action_frontPageFragment_to_redditFragment, bundle);
    }

    @Override
    public void onRedditAdapterShareButtonClicked(Children children) {
        String uri = "https://reddit.com" + children.getChildrenData().getPermalink();
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share Reddit via"));
    }
}
