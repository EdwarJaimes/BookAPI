package com.example.bookapi.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapi.LoginState;
import com.example.bookapi.model.ApiKey;
import com.example.bookapi.ApiService;
import com.example.bookapi.model.OauthKeyResponse;
import com.example.bookapi.RetrofitClient;
import com.example.bookapi.model.SesskeyResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPageViewModel extends ViewModel {

    private MutableLiveData<LoginState> loginState = new MutableLiveData<>();

    public LoginPageViewModel(String email, String password) {
        makeApiCall(email, password);
    }
    public LiveData<LoginState> getLoginState() {
        return loginState;
    }

    private void makeApiCall(String email, String password) {
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
                        System.out.println("-------------------------------------------------------------------------" );
                        System.out.println("App Key: " + appkey);
                    } else {
                        Log.e("Respuesta", "Respuesta nula");
                    }

                } else {
                    //Toast.makeText(LoginPageViewModel.this, "Error en la respuesta.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiKey> call, Throwable t) {
                Log.e("Error", t.getMessage());
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
                    System.out.println("Oauth Key: " + oauthkey);
                    System.out.println("O_u: " + o_u);
                    makeSesskeyCall(o_u, oauthkey);
                } else {
                   // Toast.makeText(LoginPageActivity.this, "Error en la respuesta del oauthkey.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OauthKeyResponse> call, Throwable t) {
                //Toast.makeText(LoginPageActivity.this, "Fallo en la petición del oauthkey: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    System.out.println("Sesskey: " + sesskey);
                    System.out.println("Metodo sesskey..........................................................................");

                    loginState.setValue(new LoginState.Success(o_u, sesskey));

                } else {
                    //Toast.makeText(LoginPageActivity.this, "Error en la respuesta del sesskey.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SesskeyResponse> call, Throwable t) {
                //Toast.makeText(LoginPageActivity.this, "Fallo en la petición del oauthkey: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
