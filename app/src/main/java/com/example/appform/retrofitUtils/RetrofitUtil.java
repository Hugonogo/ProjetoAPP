package com.example.appform.retrofitUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    public static Retrofit createRetrofit(){
        return new Retrofit.Builder().baseUrl(
                "https://balance-dxhn.onrender.com/"
        ).addConverterFactory(
                GsonConverterFactory.create()
        ).build();
    }

    public static ServiceApi createService(Retrofit retrofit){
        return retrofit.create(ServiceApi.class);
    }
}
