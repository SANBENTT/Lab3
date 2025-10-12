package com.example.lab3.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.lab3.MenuNavegable;
import com.example.lab3.databinding.ActivityLoginMainBinding;

public class LoginMainActivity extends AppCompatActivity {

    private LoginMainActivityViewModel vm;
    private ActivityLoginMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(LoginMainActivityViewModel.class);


        binding = ActivityLoginMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        vm.getLogin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loginExitoso) {
                if (loginExitoso) {
                    Intent i = new Intent(getApplicationContext(), MenuNavegable.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.Ingresar(binding.etUsuario.getText().toString(), binding.etContra.getText().toString());
            }
        });
    }
}