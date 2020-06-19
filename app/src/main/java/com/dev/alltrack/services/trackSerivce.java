package com.dev.alltrack.services;

import com.dev.alltrack.models.dataResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface trackSerivce {

    @GET("{barcode}")
    Call<dataResponse> get(@Path("barcode") String barCode);

}
