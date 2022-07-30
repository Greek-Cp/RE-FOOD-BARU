package com.nicomot.re_food.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nicomot.re_food.R;
import com.nicomot.re_food.model.Account;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    TextView btnLogin;
    TextInputEditText editTextUsername, editTextPassword;
    Button btnRegister;
    Spinner spinnerKerjaan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        hiddenActionBar();

        btnRegister = findViewById(R.id.id_btn_register_account);
        editTextUsername = findViewById(R.id.id_login_username);
        editTextPassword = findViewById(R.id.id_login_password);
        spinnerKerjaan = findViewById(R.id.id_spsinner_menu_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.list_role, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKerjaan.setAdapter(adapter);
        spinnerKerjaan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String tmp = adapter.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnLogin = findViewById(R.id.id_btn_login_activity);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(keyCode, event);
            return true;
        }
        return false;
    }

    void hiddenActionBar(){
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
    void registerToDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://re-food-7fc1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("Akun").child(editTextUsername.getText().toString());
            Account account = new Account(editTextUsername.getText().toString(),editTextPassword.getText().toString(),spinnerKerjaan.getSelectedItem().toString());
        myRef.setValue(account);
        Toast.makeText(getApplicationContext(),"Register Berhasil ",Toast.LENGTH_SHORT).show();
    }
    void switchActivityToLogin(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_btn_register_account:
                if(!editTextUsername.getText().toString().isEmpty() && !editTextPassword.getText().toString().isEmpty() &&
                editTextUsername.getText().toString().length() >= 4 && editTextPassword.getText().toString().length() >= 4
              &&   editTextUsername.getText().toString().length() <= 8 && editTextPassword.getText().toString().length() <= 8
                ){
                    registerToDatabase();
                } else if(editTextUsername.getText().toString().length() <= 4
                        && editTextPassword.getText().toString().length() <= 4){
                    editTextUsername.setError("Username Minimal 4 Karakter Dan Maximal 8 Karakter");
                    editTextPassword.setError("Password Minimal 4 Karakter Dan Maximal 8 Karakter");
                }
                break;
            case R.id.id_btn_login_activity:
                switchActivityToLogin();
                break;
        }
    }
}