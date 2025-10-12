package com.example.lab3.ui.login;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lab3.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginMainActivityViewModel extends AndroidViewModel {

    private Context context;
    public MutableLiveData<Boolean> mLogin = new MutableLiveData<>();

    public LoginMainActivityViewModel(@NonNull Application application) {
        super(application);
        context = getApplication();
    }

    public LiveData<Boolean> getLogin() { return mLogin; }

    public void Ingresar(String usuario, String pass) {
        if (usuario.isEmpty() || pass.isEmpty()) {
            Toast.makeText(context, "Debe ingresar usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            ApiClient.InmobiliariaServicios api = ApiClient.getApiInmobiliaria();
            Call<String> llamada = api.login(usuario, pass);

            llamada.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        String token = response.body();
                        ApiClient.guardarToken(context, token);
                        mLogin.postValue(true);
                        Toast.makeText(context, "Bienvenido!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context, "Error en el servicio: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex) {
            Toast.makeText(context, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
