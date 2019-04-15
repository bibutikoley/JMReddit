package dev.bibuti.redditclient;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
public class SearchPageFragment extends Fragment
        implements RedditAdapter.OnRedditAdapterItemClickListener {

    private static final String TAG = "SearchPageFragment";

    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.searchRV)
    RecyclerView searchRV;

    @BindView(R.id.searchLoaderLL)
    LinearLayout searchLoaderLL;
    @BindView(R.id.fb_shimmer)
    ShimmerFrameLayout shimmerFrameLayout;

    private RedditAdapter mRedditAdapter = new RedditAdapter();
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoadMoreCalled = false;
    private String mAfterPage;
    private String searchQuery;
    private Boolean isSpecial = false;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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

    public SearchPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSpecial = false;
        if (getArguments() != null) {
            searchQuery = getArguments().getString("query");
            isSpecial = getArguments().getBoolean("special");
        }
        RedditHelper.log("Query - " + searchQuery + " Is Special - " + isSpecial);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_page, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayoutManager = new LinearLayoutManager(requireContext());

        searchRV.setHasFixedSize(true);
        searchRV.setLayoutManager(linearLayoutManager);
        mRedditAdapter.setRedditListener(SearchPageFragment.this);
        searchRV.setAdapter(mRedditAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query != null) {
                    if (!query.trim().isEmpty()) {
                        isSpecial = false;
                        fetchSearchResults(query);
                    }
                    if (query.trim().isEmpty()) {
                        searchLoaderLL.setVisibility(View.INVISIBLE);
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchRV.addOnScrollListener(onScrollListener);

        if (searchQuery != null) {
            searchView.setQuery(searchQuery, false);
            fetchSearchResults(searchQuery);
        }

    }

    private void fetchSearchResults(String q) {

        Observer<RedditResponse> observer = new Observer<RedditResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
                searchRV.setVisibility(View.INVISIBLE);
                searchLoaderLL.setVisibility(View.VISIBLE);
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
                Toast.makeText(requireContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                searchRV.setVisibility(View.INVISIBLE);
                searchLoaderLL.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {
                searchRV.setVisibility(View.VISIBLE);
                searchLoaderLL.setVisibility(View.INVISIBLE);
                shimmerFrameLayout.stopShimmer();
            }
        };

        if (isSpecial) {

            RetrofitClient.retrofitEndpoints.otherReddits(q)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);

        } else {

            RetrofitClient.retrofitEndpoints.search(q)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);

        }

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

        if (isSpecial) {
            RetrofitClient.retrofitEndpoints.otherNextReddits(searchQuery, after)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } else {
            RetrofitClient.retrofitEndpoints.getNextPage(after)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }

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
        Navigation.findNavController(requireView()).navigate(R.id.action_searchPageFragment_to_redditFragment, bundle);
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
