package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private TextInputEditText email, password;
    private Button login;
    private ProgressBar progressBar;
    private TextView textView, textView1;

    private LoginPresenter presenter;

    @Override
    public void onStart() {
        super.onStart();
        presenter.checkUserLoggedIn();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presenter = new LoginPresenter(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.button_login);
        progressBar = findViewById(R.id.progressbar);
        textView = findViewById(R.id.switchToSignup);
        textView1 = findViewById(R.id.forgotpassword);

        textView.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            finish();
        });

        textView1.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class));
            finish();
        });

        login.setOnClickListener(v -> {
            String loginEmail = String.valueOf(email.getText());
            String loginPassword = String.valueOf(password.getText());
            if (TextUtils.isEmpty(loginEmail)) {
                showToast("Enter email");
                return;
            }
            if (TextUtils.isEmpty(loginPassword)) {
                showToast("Enter password");
                return;
            }
            presenter.performLogin(loginEmail, loginPassword);
        });
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToQuiz() {
        showToast("Transitioning to Quiz");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
