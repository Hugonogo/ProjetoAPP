package com.example.appform.retrofitUtils;

import com.example.appform.model.ModelResponse;
import com.example.appform.model.ModeloUsuarioKarine;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceApi {

    @GET("api/v1/profiles")
    Call<ModelResponse> getUsuarios ();
}
