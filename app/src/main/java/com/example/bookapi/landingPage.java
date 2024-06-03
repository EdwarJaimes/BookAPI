package com.example.bookapi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class landingPage extends AppCompatActivity {
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
        String o_u = intent.getStringExtra("o_u");
        String sesskey = intent.getStringExtra("sesskey");
        String oauthkey = intent.getStringExtra("oauthkey");
        txtTitle.setText(": "+ o_u);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewLanding);
        adapter = new ImageAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        bntMenu.setOnClickListener(view -> dropAllSessions(o_u, sesskey));

        makeGetAllBooks(o_u, sesskey);
    }

    private void makeGetAllBooks(String o_u, String oauthkey) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<GetBooksResponse> call = apiService.getAllBooks(o_u, o_u, oauthkey);
        call.enqueue(new Callback<GetBooksResponse>() {
            @Override
            public void onResponse(Call<GetBooksResponse> call, Response<GetBooksResponse> response) {
                if (response.isSuccessful()) {
                    GetBooksResponse getBooksResponse = response.body();

                    List<Book> books = getBooksResponse.allBooks.books;

                    adapter.adicionarListaLibro((ArrayList<Book>) books);

                } else {
                    Toast.makeText(landingPage.this, "Error en la respuesta del getallbooks.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetBooksResponse> call, Throwable t) {
                Toast.makeText(landingPage.this, "Fallo en la petición del getallbooks: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void dropAllSessions(String o_u, String sesskey) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<DropAllSessions> call = apiService.dropAllSessions(o_u, sesskey);
        call.enqueue(new Callback<DropAllSessions>() {
            @Override
            public void onResponse(Call<DropAllSessions> call, Response<DropAllSessions> response) {
                if (response.isSuccessful()) {
                    DropAllSessions dropAllSessions = response.body();

                    String status = dropAllSessions.status;
                    Toast.makeText(landingPage.this, "Status: " + status, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(landingPage.this, "Error en la respuesta del getallbooks.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DropAllSessions> call, Throwable t) {
                Toast.makeText(landingPage.this, "Fallo en la petición del getallbooks: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}