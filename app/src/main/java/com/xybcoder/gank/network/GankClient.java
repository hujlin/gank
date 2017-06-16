package com.xybcoder.gank.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 *
 */
public class GankClient {
    public static final String HOST = "http://gank.io/api/";
    private static ApiService gankRetrofit;
    protected static final Object monitor = new Object();
    private static Retrofit retrofit;

    private GankClient(){

    }

    static {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(genericClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    //添加请求头
    public static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .addHeader("Accept-Encoding", "gzip, deflate")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Accept", "*/*")
                                .addHeader("Cookie", "add cookies here")
                                .addHeader("header1", "header1")
                                .addHeader("header2", "header2")
                                .addHeader("header3", "header3")
                                .addHeader("header4", "header4")
                                .build();
                        return chain.proceed(request);
                    }

                })
                .build();

        return httpClient;
    }

    public static ApiService getGankRetrofitInstance() {
        synchronized (monitor) {
            if (gankRetrofit == null) {
                gankRetrofit = retrofit.create(ApiService.class);
            }
            return gankRetrofit;
        }
    }
}
