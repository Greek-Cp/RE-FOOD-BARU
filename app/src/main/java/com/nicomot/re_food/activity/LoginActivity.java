package com.nicomot.re_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nicomot.re_food.R;
import com.nicomot.re_food.model.Account;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    TextView btnRegister;
    TextInputEditText editTextUsername, editTextPassword;
    Button btnMasuk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        hiddenActionBar();

        btnRegister = findViewById(R.id.id_btn_register);
        editTextUsername = findViewById(R.id.id_login_username);
        editTextPassword = findViewById(R.id.id_login_password);
        btnMasuk = findViewById(R.id.id_btn_login_masuk);
        btnMasuk.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }
    void switchToActivityRegister(){
        Intent inten = new Intent(this,RegisterActivity.class);
        startActivity(inten);
    }
    void hiddenActionBar(){
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
    void checkLogin(){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://re-food-7fc1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference().child("Akun");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Account> accountList = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Account account = dataSnapshot1.getValue(Account.class);
                    System.out.println("Username = " + account.getUsernameAccount());
                    System.out.println("Password = " + account.getPasswordAccount());
                    System.out.println("Username textField = " + editTextUsername.getText().toString());
                    if(editTextUsername.getText().toString().equals(account.getUsernameAccount()) && editTextPassword.getText().toString().equals(account.getPasswordAccount())){
                        if(account.getTypeAccount().equals("DAPUR")){
                            //activity Dapur
                            Toast.makeText(getApplicationContext(),"Login Berhasil Ke Dapur",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),DapurActivity.class);
                            intent.putExtra("KERJA","DAPUR");
                            startActivity(intent);
                        } else if(account.getTypeAccount().equals("KASIR")){
                            Toast.makeText(getApplicationContext(),"Login Berhasil Ke Kasir",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),BaseActivity.class);
                            intent.putExtra("KERJA","KASIR");
                            startActivity(intent);

                        }
                     } else{

                        editTextUsername.setError("Harap Cek Kembali Username Anda");
                        editTextPassword.setError("Harap Cek Kembali Password Anda");
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_btn_login_masuk:
                //method login
                if(!editTextUsername.getText().toString().isEmpty() && !editTextPassword.getText().toString().isEmpty()){
                    checkLogin();
                } else{
                    editTextUsername.setError("Username Tidak Boleh Kosong");
                    editTextPassword.setError("Password Tidak Boleh Kosong");
                }
                break;
            case R.id.id_btn_register:
                switchToActivityRegister();
                break;
        }
    }
}