package com.dev.alltrack.services;

import com.dev.alltrack.models.dataResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface trackSerivce {

    @GET("/")
    Call<String> activate();

    @GET("/api/v1.0/track/{barcode}")
    Call<dataResponse> get(@Path("barcode") String barCode);

}
