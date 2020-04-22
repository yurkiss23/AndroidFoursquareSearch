package com.example.hw1;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VenueDTOService {
    private static VenueDTOService mInstance;
    private static final String BASE_URL = "https://api.foursquare.com/v2/venues/";
    private Retrofit mRetrofit;

    private VenueDTOService(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static VenueDTOService getInstance(){
        if (mInstance == null){
            mInstance = new VenueDTOService();
        }
        return mInstance;
    }

    public VenueDTOHolderApi getApi(){return mRetrofit.create(VenueDTOHolderApi.class);}
}
