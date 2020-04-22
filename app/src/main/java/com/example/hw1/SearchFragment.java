package com.example.hw1;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private Spinner spinCategories;
    private ArrayList<CategoryEntry> categoriesList;
    private ArrayList<String> catSelectList;
    private ArrayList<VenueEntry> venuesList;
    private Button btnSearch;
    private EditText txtQuery, txtLocation;
    private RecyclerView recyclerView;
    private VenueRecyclerViewAdapter venueAdapter;
    private String CategoryId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        setupViews(view);

        loadCategories();
        setItemSelectedListener();
        setButtonSearchListener();

        return view;
    }

    private void setupViews(View view){
        spinCategories = view.findViewById(R.id.spin_categories);
        btnSearch = view.findViewById(R.id.btn_search);
        txtQuery = view.findViewById(R.id.txt_query);
        txtLocation = view.findViewById(R.id.txt_location);
        recyclerView = view.findViewById(R.id.recycler_view);
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

                            categoriesList = new ArrayList<>();
                            catSelectList = new ArrayList<>();
                            if (response.body() != null){
                                JsonObject jsonResponse = response.body().getAsJsonObject("response");
                                JsonArray jsonCategories = jsonResponse.getAsJsonArray("categories");
                                for (JsonElement category : jsonCategories){
                                    JsonObject catName = category.getAsJsonObject();
                                    CategoryEntry ce = new CategoryEntry(catName.get("id").getAsString(),
                                            catName.get("name").getAsString());
                                    categoriesList.add(ce);
                                    catSelectList.add(catName.get("name").getAsString());
                                }
                                ArrayAdapter<String>adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, catSelectList);
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

    private void setItemSelectedListener(){
        spinCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinCategories.getSelectedItem() != null){
                    for (CategoryEntry ce : categoriesList){
                        if (ce.name == spinCategories.getSelectedItem()){
                            CategoryId = ce.id;
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setButtonSearchListener(){
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Пошук: " + txtQuery.getText().toString(), Toast.LENGTH_LONG).show();
                setRecyclerView();
                loadVenueSearchList();
            }
        });
    }

    private void setRecyclerView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),
                1,
                GridLayoutManager.VERTICAL,
                false));
        venuesList = new ArrayList<>();
        venueAdapter = new VenueRecyclerViewAdapter(venuesList, getContext());
        recyclerView.setAdapter(venueAdapter);
        int largePadding = 10;
        int smallPadding = 4;
        recyclerView.addItemDecoration(new VenueGridItemDecoration(largePadding, smallPadding));
    }

    private void loadVenueSearchList(){

        VenueDTOService.getInstance()
                .getApi()
                .getVenue(txtLocation.getText().toString(),
                        txtQuery.getText().toString(),
                        CategoryId
                )
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()){
                            Log.d("TAG", "--Responce OK--");

                            if (response.body() != null){
                                venuesList.clear();
                                JsonObject jsonResponse = response.body().getAsJsonObject("response");
                                JsonArray jsonVenues = jsonResponse.getAsJsonArray("venues");
                                for (JsonElement je : jsonVenues){
                                    JsonObject jo = je.getAsJsonObject();
                                    VenueEntry ve = new VenueEntry(jo.get("id").getAsString(),
                                            jo.get("name").getAsString());
                                    venuesList.add(ve);
                                }
                                Log.d("TAG", Integer.toString(venuesList.size()));
                                venueAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d("TAG", "--Responce error--"+t);
                    }
                });
    }
}
