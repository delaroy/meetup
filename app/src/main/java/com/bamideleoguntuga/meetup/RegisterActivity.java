package com.bamideleoguntuga.meetup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bamideleoguntuga.meetup.model.Create;
import com.bamideleoguntuga.meetup.model.CreateResponse;
import com.bamideleoguntuga.meetup.networking.api.Service;
import com.bamideleoguntuga.meetup.networking.generator.DataGenerator;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bamideleoguntuga.meetup.utils.Constants.BASE_URL;
import static com.bamideleoguntuga.meetup.utils.Constants.SUCCESS;
import static com.bamideleoguntuga.meetup.utils.Constants.SUCCESS_REGISTER;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.name)
    TextInputLayout name;

    @BindView(R.id.phone)
    TextInputLayout phone;

    @BindView(R.id.pin)
    TextInputLayout pin;

    @BindView(R.id.repeatpin)
    TextInputLayout repeatpin;

    @BindView(R.id.name_text)
    EditText name_text;

    @BindView(R.id.phone_text)
    EditText phone_text;

    @BindView(R.id.pin_text)
    EditText pin_text;

    @BindView(R.id.repeatpin_text)
    EditText repeat_pin;

    @BindView(R.id.checkBox)
    CheckBox check_box;

    @BindView(R.id.register)
    ImageButton register;

    @BindView(R.id.signin)
    TextView signin;

    @BindView(R.id.progress)
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        //initialize butterknife
        ButterKnife.bind(this);

        register.setOnClickListener(this);
        signin.setOnClickListener(this);

    }

    public void verifyData() {
        name.setError(null);
        phone.setError(null);
        pin.setError(null);
        repeatpin.setError(null);

        if (name_text.length() == 0) {

            name.setError(getString(R.string.error_name));

        } else if (phone_text.length() == 0) {

            phone.setError(getString(R.string.error_phone_number));

        } else if (pin_text.length() == 0) {

            pin.setError(getString(R.string.error_pintxt));

        } else if (repeat_pin.length() == 0) {

            repeatpin.setError(getString(R.string.repeatpin));

        } else if (!(pin_text.getText().toString().equals(repeat_pin.getText().toString()))) {

            repeatpin.setError(getString(R.string.pinmatch));

        } else if (!check_box.isChecked()) {

            Toast.makeText(this, "Please accept terms & conditions", Toast.LENGTH_SHORT).show();

        } else {
            String name = name_text.getText().toString().trim();
            String phone = phone_text.getText().toString().trim();
            String pin = pin_text.getText().toString().trim();

            register(name, phone, pin);
        }
    }

    public void register(String name, String phone, String pin) {
        try {
            progress.setVisibility(View.VISIBLE);
            Service service = DataGenerator.createService(Service.class, BASE_URL);
            Call<CreateResponse> createResponseCall = service.create(new Create(name, phone, pin));

            createResponseCall.enqueue(new Callback<CreateResponse>() {
                @Override
                public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            CreateResponse createResponse = response.body();

                            progress.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "successfully register", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra(SUCCESS_REGISTER, SUCCESS);
                            startActivity(intent);

                        }
                    }
                }

                @Override
                public void onFailure(Call<CreateResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "error registering", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {
            Toast.makeText(RegisterActivity.this, "error registering", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                verifyData();
                break;
            case R.id.signin:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);

                break;
        }
    }
}
