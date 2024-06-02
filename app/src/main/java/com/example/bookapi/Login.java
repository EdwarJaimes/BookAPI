package com.example.bookapi;

import android.adservices.topics.GetTopicsResponse;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText email = findViewById(R.id.edtEmail);
        EditText password = findViewById(R.id.edtPassword);
        Button login = findViewById(R.id.btnLogin);

        Gson gson = new Gson();


        //https://timetonic.com/live/api.php?req=createAppkey&appname=api

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeApiCall(email.getText().toString(), password.getText().toString());
            }
        });
    }

    private String getAppName() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getString(stringId);
    }

    private void makeApiCall(String email, String password) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<Post> call = apiService.createAppKey();
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Post appKeyResponse = response.body();
                    if (appKeyResponse != null) {
                        String appkey = appKeyResponse.getAppkey();
                        Toast.makeText(Login.this, "App Key: " + appkey, Toast.LENGTH_SHORT).show();
                        makeOauthKeyCall(email, password, appkey);
                    } else
                        // Haz algo con la respuesta
                        Toast.makeText(Login.this, "App Key: " + appKeyResponse.getAppkey(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Login.this, "Error en la respuesta.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(Login.this, "Fallo en la petición: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void makeOauthKeyCall(String email, String pass, String appkey) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<OauthKeyResponse> call = apiService.createOauthKey(email, pass, appkey);
        call.enqueue(new Callback<OauthKeyResponse>() {
            @Override
            public void onResponse(Call<OauthKeyResponse> call, Response<OauthKeyResponse> response) {
                if (response.isSuccessful()) {
                    OauthKeyResponse oauthKeyResponse = response.body();
                    String oauthkey = oauthKeyResponse.getOauthkey();
                    String o_u = oauthKeyResponse.getO_u();
                    Toast.makeText(Login.this, "Oauth Key: " + oauthKeyResponse.getOauthkey(), Toast.LENGTH_SHORT).show();

                    makeSesskeyCall(o_u, oauthkey);
                } else {
                    Toast.makeText(Login.this, "Error en la respuesta del oauthkey.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OauthKeyResponse> call, Throwable t) {
                Toast.makeText(Login.this, "Fallo en la petición del oauthkey: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeSesskeyCall(String o_u, String oauthkey) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<SesskeyResponse> call = apiService.createSessKey(o_u, oauthkey, "=");
        call.enqueue(new Callback<SesskeyResponse>() {
            @Override
            public void onResponse(Call<SesskeyResponse> call, Response<SesskeyResponse> response) {
                if (response.isSuccessful()) {
                    SesskeyResponse sesskeyResponse = response.body();
                    String sesskey = sesskeyResponse.getSesskey();

                    //makeGetAllBooks(o_u, sesskey);

                    Intent intent = new Intent(Login.this, landingPage.class);

                    intent.putExtra("oauthkey", oauthkey);
                    intent.putExtra("o_u", o_u);
                    intent.putExtra("sesskey", sesskey);
                    startActivity(intent);

                    Toast.makeText(Login.this, "Sesskey: " + sesskeyResponse.getSesskey(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "Error en la respuesta del sesskey.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SesskeyResponse> call, Throwable t) {
                Toast.makeText(Login.this, "Fallo en la petición del oauthkey: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

                    Toast.makeText(Login.this, "GetBOOKS: " , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "Error en la respuesta del getallbooks.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetBooksResponse> call, Throwable t) {
                Toast.makeText(Login.this, "Fallo en la petición del getallbooks: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}