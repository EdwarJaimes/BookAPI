package com.example.bookapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookapi.viewModel.LoginPageViewModel;
import com.example.bookapi.viewModel.LoginState;

public class LoginPageActivity extends AppCompatActivity {
    LoginPageViewModel loginPageViewModel;
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


        //login.setOnClickListener(view -> makeApiCall(email.getText().toString(), password.getText().toString()));
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @Override
            public <T extends ViewModel> T create(Class<T> modelClass) {
                return (T) new LoginPageViewModel(email.getText().toString(), password.getText().toString());
            }

        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loginPageViewModel = new ViewModelProvider(this, factory).get(LoginPageViewModel.class);
                loginPageViewModel = new ViewModelProvider(LoginPageActivity.this, factory).get(LoginPageViewModel.class);

                loginPageViewModel.getLoginState().observe(LoginPageActivity.this, new Observer<LoginState>() {
                    @Override
                    public void onChanged(LoginState loginState) {
                        LoginState.Success successState = (LoginState.Success) loginState;

                        String o_u = successState.getO_u();
                        String sesskey = successState.getSesskey();

                        Intent intent = new Intent(LoginPageActivity.this, LandingPageActivity.class);
                        intent.putExtra("o_u", o_u);
                        intent.putExtra("sesskey", sesskey);
                        startActivity(intent);

                    }
                });
            }
        });




/*
        private String getAppName () {
            ApplicationInfo applicationInfo = getApplicationInfo();
            int stringId = applicationInfo.labelRes;
            return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getString(stringId);
        }

        private void makeApiCall (String email, String password){
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

            Call<ApiKey> call = apiService.createAppKey();
            call.enqueue(new Callback<ApiKey>() {
                @Override
                public void onResponse(Call<ApiKey> call, Response<ApiKey> response) {
                    if (response.isSuccessful()) {
                        ApiKey appKeyResponse = response.body();
                        if (appKeyResponse != null) {
                            String appkey = appKeyResponse.getAppkey();
                            //Toast.makeText(Login.this, "App Key: " + appkey, Toast.LENGTH_SHORT).show();
                            makeOauthKeyCall(email, password, appkey);
                        } else {
                            Log.e("Respuesta", "Respuesta nula");
                        }

                    } else {
                        Toast.makeText(LoginPageActivity.this, "Error en la respuesta.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiKey> call, Throwable t) {
                    Toast.makeText(LoginPageActivity.this, "Fallo en la petición: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        private void makeOauthKeyCall (String email, String pass, String appkey){
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<OauthKeyResponse> call = apiService.createOauthKey(email, pass, appkey);
            call.enqueue(new Callback<OauthKeyResponse>() {
                @Override
                public void onResponse(Call<OauthKeyResponse> call, Response<OauthKeyResponse> response) {
                    if (response.isSuccessful()) {
                        OauthKeyResponse oauthKeyResponse = response.body();
                        String oauthkey = oauthKeyResponse.getOauthkey();
                        String o_u = oauthKeyResponse.getO_u();

                        makeSesskeyCall(o_u, oauthkey);
                    } else {
                        Toast.makeText(LoginPageActivity.this, "Error en la respuesta del oauthkey.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<OauthKeyResponse> call, Throwable t) {
                    Toast.makeText(LoginPageActivity.this, "Fallo en la petición del oauthkey: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void makeSesskeyCall (String o_u, String oauthkey){
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<SesskeyResponse> call = apiService.createSessKey(o_u, oauthkey, "=");
            call.enqueue(new Callback<SesskeyResponse>() {
                @Override
                public void onResponse(Call<SesskeyResponse> call, Response<SesskeyResponse> response) {
                    if (response.isSuccessful()) {
                        SesskeyResponse sesskeyResponse = response.body();
                        String sesskey = sesskeyResponse.getSesskey();

                        Intent intent = new Intent(LoginPageActivity.this, LandingPageActivity.class);

                        intent.putExtra("oauthkey", oauthkey);
                        intent.putExtra("o_u", o_u);
                        intent.putExtra("sesskey", sesskey);
                        startActivity(intent);

                        //Toast.makeText(Login.this, "Sesskey: " + sesskeyResponse.getSesskey(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginPageActivity.this, "Error en la respuesta del sesskey.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SesskeyResponse> call, Throwable t) {
                    Toast.makeText(LoginPageActivity.this, "Fallo en la petición del oauthkey: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        */
    }
}
