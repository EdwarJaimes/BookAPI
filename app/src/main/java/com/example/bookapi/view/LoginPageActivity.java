package com.example.bookapi.view;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.bookapi.R;
import com.example.bookapi.viewModel.LoginPageViewModel;
import com.example.bookapi.LoginState;

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

        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @Override
            public <T extends ViewModel> T create(Class<T> modelClass) {
                return (T) new LoginPageViewModel(email.getText().toString(), password.getText().toString());
            }

        };
        login.setOnClickListener(view -> {
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
        });
    }
}
