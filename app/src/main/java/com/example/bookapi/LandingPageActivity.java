package com.example.bookapi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapi.viewModel.LandingPageViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingPageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String o_u;
    String sesskey;
    String oauthkey;
    public LandingPageViewModel landingPageViewModel;
    private ImageAdapter adapter;
    Toolbar toolbar;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing_page);


        toolbar = findViewById(R.id.toolbar);
        ImageButton bntMenu;
        bntMenu = findViewById(R.id.btnMenu);
        TextView txtTitle;
        txtTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        o_u = intent.getStringExtra("o_u");
        sesskey = intent.getStringExtra("sesskey");
        oauthkey = intent.getStringExtra("oauthkey");

        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                 return (T) new LandingPageViewModel(o_u, sesskey);
            }
        };

        landingPageViewModel = new ViewModelProvider(this, factory).get(LandingPageViewModel.class);

        recyclerView = findViewById(R.id.recyclerViewLanding);
        adapter = new ImageAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        txtTitle.setText(": "+ o_u);

        //bntMenu.setOnClickListener(view -> dropAllSessions(o_u, sesskey));

        landingPageViewModel.getBookListState().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {

                    adapter.adicionarListaLibro((ArrayList<Book>) books);

            }
        });


        //makeGetAllBooks(o_u, sesskey);
    }

//    private void makeGetAllBooks(String o_u, String oauthkey) {
//        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
//        Call<GetBooksResponse> call = apiService.getAllBooks(o_u, o_u, oauthkey);
//        call.enqueue(new Callback<GetBooksResponse>() {
//            @Override
//            public void onResponse(Call<GetBooksResponse> call, Response<GetBooksResponse> response) {
//                if (response.isSuccessful()) {
//                    GetBooksResponse getBooksResponse = response.body();
//
//                    List<Book> books = getBooksResponse.allBooks.books;
//
//                    adapter.adicionarListaLibro((ArrayList<Book>) books);
//
//                } else {
//                    Toast.makeText(landingPage.this, "Error en la respuesta del getallbooks.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetBooksResponse> call, Throwable t) {
//                Toast.makeText(landingPage.this, "Fallo en la petición del getallbooks: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    private void dropAllSessions(String o_u, String sesskey) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<DropAllSessions> call = apiService.dropAllSessions(o_u, sesskey);
        call.enqueue(new Callback<DropAllSessions>() {
            @Override
            public void onResponse(Call<DropAllSessions> call, Response<DropAllSessions> response) {
                if (response.isSuccessful()) {
                    DropAllSessions dropAllSessions = response.body();

                    String status = dropAllSessions.status;
                    Toast.makeText(LandingPageActivity.this, "Status: " + status, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(LandingPageActivity.this, "Error en la respuesta del getallbooks.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DropAllSessions> call, Throwable t) {
                Toast.makeText(LandingPageActivity.this, "Fallo en la petición del getallbooks: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}