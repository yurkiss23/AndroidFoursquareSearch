package com.example.hw1;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VenueDTOHolderApi {
    @GET("search?client_id=OETEEOAAMUR1W2ZBKLBUVS0PAX0MQLZZJPHCYNISY4V5JNY2&client_secret=ONQYUPTBSTSHFMMKTS5L00UGOIKGPBOEERFHOXJYSWDHVYYN&v=20190425&limit=10")
    public Call<JsonObject> getVenue(@Query("near") String location,
                                     @Query("query") String query,
                                     @Query("categoryId") String categoryId);

    @GET("categories?client_id=OETEEOAAMUR1W2ZBKLBUVS0PAX0MQLZZJPHCYNISY4V5JNY2&client_secret=ONQYUPTBSTSHFMMKTS5L00UGOIKGPBOEERFHOXJYSWDHVYYN&v=20190425")
    public Call<JsonObject> getCategories();
}
