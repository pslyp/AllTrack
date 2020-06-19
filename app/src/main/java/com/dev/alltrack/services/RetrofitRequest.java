package com.dev.alltrack.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {

    private static Retrofit retrofit;
    private static RetrofitRequest client;

    private static final String BASR_URL = "https://ems-track.herokuapp.com/api/v1.0/track/";

    private RetrofitRequest() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASR_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitRequest getInstance() {
        if(client == null) {
            client = new RetrofitRequest();
        }
        return client;
    }

    public trackSerivce api() {
        return retrofit.create(trackSerivce.class);
    }

}
