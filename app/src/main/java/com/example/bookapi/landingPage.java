package com.example.bookapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class landingPage extends AppCompatActivity {

    private Retrofit retrofit;
    private ImageAdapter adapter;
    int offset;

    boolean aptoParaCargar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing_page);

        Intent intent = getIntent();
        String o_u = intent.getStringExtra("o_u");
        String sesskey = intent.getStringExtra("sesskey");
        String oauthkey = intent.getStringExtra("oauthkey");

        RecyclerView recyclerView = findViewById(R.id.recyclerViewLanding);
        adapter = new ImageAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0){

                }
            }
        });

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

                    Toast.makeText(landingPage.this, "GetBOOKS: " , Toast.LENGTH_SHORT).show();
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
}