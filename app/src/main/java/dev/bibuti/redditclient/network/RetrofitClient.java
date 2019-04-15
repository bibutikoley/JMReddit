package dev.bibuti.redditclient.network;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import dev.bibuti.redditclient.BuildConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String URL = "https://reddit.com";

    private static volatile Retrofit retrofitInstance;

    public static RetrofitEndpoints retrofitEndpoints = getNetworkInstance().create(RetrofitEndpoints.class);

    private static Retrofit getNetworkInstance() {
        if (retrofitInstance == null) {
            synchronized (RetrofitClient.class) {
                if (retrofitInstance == null) {
                    retrofitInstance = new Retrofit.Builder()
                            .baseUrl(URL)
                            .client(customHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofitInstance;
    }

    private static OkHttpClient customHttpClient() {
        OkHttpClient okHttpClient;
        if (BuildConfig.DEBUG) {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .addInterceptor(new LoggingInterceptor.Builder().setLevel(Level.BASIC).loggable(true).build())
                    .build();
        } else {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .build();
        }
        return okHttpClient;
    }

}
