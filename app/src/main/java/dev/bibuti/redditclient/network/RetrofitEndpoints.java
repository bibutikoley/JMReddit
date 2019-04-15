package dev.bibuti.redditclient.network;

import java.util.List;

import dev.bibuti.redditclient.network.models.frontpageresponse.RedditResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetrofitEndpoints {

    String JSON = "/.json";

    @GET(JSON)
    Observable<RedditResponse> getAllRecentPost();

    @GET(JSON)
    Observable<RedditResponse> getNextPage(@Query("after") String after);

    @GET("/search" + JSON)
    Observable<RedditResponse> search(@Query("q") String query);

    @GET("/{value}" + JSON)
    Observable<RedditResponse> otherReddits(@Path("value") String sub_reddit);

    @GET("/{value}" + JSON)
    Observable<RedditResponse> otherNextReddits(@Path("value") String sub_reddit, @Query("after") String after);

    @GET()
    Observable<List<RedditResponse>> getComments(@Url String permalink);

}
