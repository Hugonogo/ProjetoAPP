package com.example.appform.retrofitUtils;

import com.example.appform.model.ModelResponse;
import com.example.appform.model.ModeloAtividadeFisica;
import com.example.appform.model.ModeloBodyPostAtividade;
import com.example.appform.model.ModeloRespostaAtividadeFisica;
import com.example.appform.model.ModeloRespsotaListaAtividades;
import com.example.appform.model.ModeloUsuarioKarine;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceApi {

    @GET("api/v1/profiles")
    Call<ModelResponse> getUsuarios ();

    @GET("/api/v1/exercise_metrics")
    Call<ModeloRespsotaListaAtividades> getAtividades (@Query("user_id") int id);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("/api/v1/exercise_metrics")
    Call<ModeloRespostaAtividadeFisica> criarAtividade (@Query("user_id") int id, @Body ModeloAtividadeFisica.Exercise modeloBodyPostAtividade);
}
