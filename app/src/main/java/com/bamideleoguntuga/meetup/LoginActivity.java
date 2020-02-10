package com.bamideleoguntuga.meetup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bamideleoguntuga.meetup.model.Create;
import com.bamideleoguntuga.meetup.model.CreateResponse;
import com.bamideleoguntuga.meetup.model.Login;
import com.bamideleoguntuga.meetup.model.LoginResponse;
import com.bamideleoguntuga.meetup.networking.api.Service;
import com.bamideleoguntuga.meetup.networking.generator.DataGenerator;
import com.bamideleoguntuga.meetup.utils.PreferenceUtils;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bamideleoguntuga.meetup.utils.Constants.BASE_URL;
import static com.bamideleoguntuga.meetup.utils.Constants.SUCCESS;
import static com.bamideleoguntuga.meetup.utils.Constants.SUCCESS_REGISTER;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.phone)
    TextInputLayout phone;

    @BindView(R.id.phone_text)
    EditText phone_text;

    @BindView(R.id.pin)
    TextInputLayout pin;

    @BindView(R.id.pin_text)
    EditText pin_text;

    @BindView(R.id.login)
    ImageButton login;

    @BindView(R.id.hello)
    TextView hello;

    @BindView(R.id.instruction)
    TextView instruction;

    @BindView(R.id.signup)
    TextView signup;

    @BindView(R.id.progress)
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        //initialize butterknife
        ButterKnife.bind(this);

        //listen to intent that calls this activity specifically after successful registration
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(SUCCESS_REGISTER)) {
            hello.setText(getString(R.string.congrats));
            instruction.setText(getString(R.string.success_reg));
        }

        login.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    public void verifyData() {
        phone.setError(null);
        pin.setError(null);

        if (phone_text.length() == 0) {

            phone.setError(getString(R.string.error_phone_number));

        } else if (pin_text.length() == 0) {

            pin.setError(getString(R.string.error_pintxt));

        } else {
            String phone = phone_text.getText().toString().trim();
            String pin = pin_text.getText().toString().trim();

            login(phone, pin);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                verifyData();
                break;
            case R.id.signup:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);

                break;
        }
    }

    public void login(String phone, String m_pin){
        try {
            progress.setVisibility(View.VISIBLE);
            Service service = DataGenerator.createService(Service.class, BASE_URL);
            Call<LoginResponse> call = service.login(new Login(phone, m_pin));

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.getToken() != null) {
                                String token = loginResponse.getToken();

                                //Save token to shared preference for persistence
                                PreferenceUtils.saveToken(token, getApplicationContext());

                                progress.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "successfully logged in", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, Guide.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    } else if (response.code() == 400){
                        progress.setVisibility(View.GONE);
                        pin.setError(getString(R.string.wrong_pin));
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "error login", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "error login", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.GONE);
        }
    }
}
