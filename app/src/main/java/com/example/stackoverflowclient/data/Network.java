package com.example.stackoverflowclient.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static Network mNetwork;
    private static final String BASE_URL = "https://api.stackexchange.com";
    private Retrofit mRetrofit;

    private Network(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Network getInstance(){
        if (mNetwork == null) {
            mNetwork = new Network();
        }
        return mNetwork;
    }

    public JSONStackoverflowApi getJSONApi(){
        return mRetrofit.create(JSONStackoverflowApi.class);
    }

}
