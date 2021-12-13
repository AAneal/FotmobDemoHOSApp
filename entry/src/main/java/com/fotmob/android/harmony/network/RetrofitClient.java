package com.fotmob.android.harmony.network;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    //private static Retrofit retrofitForMatches;
    //private static Retrofit retrofitForMatchFacts;

    public static synchronized Retrofit createRetrofitClientForMatches() {

        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(new XmlInterceptor()).build();
        // had to add interceptor
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }


    /*public static synchronized Retrofit createRetrofitClientForMatchFacts() {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60L, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build();
        return retrofit;

    }*/
}
