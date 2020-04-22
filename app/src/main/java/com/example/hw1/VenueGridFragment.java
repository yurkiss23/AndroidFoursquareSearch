package com.example.hw1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VenueGridFragment extends Fragment {

    private Spinner spinCategories;
    private ArrayList<String> categories;
    private Button btnSearch;
    private EditText txtQuery, txtLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_venue_grid, container, false);

        setupViews(view);

        loadCategories();

        return view;
    }

    private void setupViews(View view){
        spinCategories = view.findViewById(R.id.spin_categories);
        btnSearch = view.findViewById(R.id.btn_search);

        txtQuery = view.findViewById(R.id.txt_query);
        txtLocation = view.findViewById(R.id.txt_location);
    }

    private void loadCategories(){
        VenueDTOService.getInstance()
                .getApi()
                .getCategories()
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                        if (response.isSuccessful()){
                            Log.d("TAG", "--Responce OK--");

                            categories = new ArrayList<>();
                            if (response.body() != null){
                                JsonObject jsonResponse = response.body().getAsJsonObject("response");
                                JsonArray jsonCategories = jsonResponse.getAsJsonArray("categories");
                                for (JsonElement category : jsonCategories){
                                    JsonObject catName = category.getAsJsonObject();
                                    categories.add(catName.get("name").getAsString());
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinCategories.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                        Log.d("TAG", "--Responce error--"+t);
                    }
                });
    }
}
