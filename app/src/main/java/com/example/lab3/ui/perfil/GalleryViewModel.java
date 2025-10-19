package com.example.lab3.ui.perfil;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lab3.R;
import com.example.lab3.modal.Propietario;
import com.example.lab3.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryViewModel extends AndroidViewModel {

    private MutableLiveData<Propietario> mp= new MutableLiveData<>();
    private MutableLiveData<Boolean> mActivar= new MutableLiveData<>();
    private MutableLiveData<String> mNombre=new MutableLiveData<>();
    public LiveData<String> getmNombre() {
        return mNombre;
    }


    public LiveData<Boolean> getmActivar() {
        return mActivar;
    }

    public LiveData<Propietario> getMp (){

        return mp;
    }

    public GalleryViewModel(@NonNull Application application) {
        super(application);
        mActivar.setValue(false);
        mNombre.setValue("Editar");
    }




    public void guardar(String btEditar,String nom, String ape, String dni, String tel, String mail){

        if(btEditar.equalsIgnoreCase("Editar")){

            mActivar.setValue(true);

            mNombre.setValue("Guardar");

        }else{
            if (!validarCampos(nom, ape, dni, tel, mail)) {
                return;
            }
            Propietario p=new Propietario();
            p.setIdPropietario(mp.getValue().getIdPropietario());
            p.setNombre(nom);
            p.setApellido(ape);
            p.setDni(dni);
            p.setTelefono(tel);
            p.setEmail(mail);

            mNombre.setValue("Guardar");
            mActivar.setValue(false);


            String token= ApiClient.leerToken(getApplication());
            Call<Propietario>call=ApiClient.getApiInmobiliaria().actualizarPropietario("Bearer " +token, p);
            call.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                    if (response.isSuccessful()){
                        mp.postValue(response.body());
                        mActivar.setValue(false);
                        mNombre.setValue("Editar");
                        Toast.makeText(getApplication(),"Propietario Actualizado",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplication(),"Error al actualizar",Toast.LENGTH_LONG).show();
                        Log.d("error",response.message());
                    }
                }

                @Override
                public void onFailure(Call<Propietario> call, Throwable t) {
                    Toast.makeText(getApplication(),"Error de servidor",Toast.LENGTH_LONG).show();
                    Log.d("error",t.getMessage());
                }
            });

        }

    }

    private boolean validarCampos(String nombre, String apellido, String dni, String telefono, String email) {
        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            Toast.makeText(getApplication(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            Toast.makeText(getApplication(), "El nombre solo puede contener letras", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            Toast.makeText(getApplication(), "El apellido solo puede contener letras", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!dni.matches("\\d{7,8}")) {
            Toast.makeText(getApplication(), "El DNI debe tener 7 u 8 digitos numericos", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!telefono.matches("\\d{10,15}")) {
            Toast.makeText(getApplication(), "El telefono debe tener entre 10 y 15 digitos", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplication(), "Ingrese un email valido", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public void leerPropietario(){
        String token = ApiClient.leerToken(getApplication());

        Call<Propietario> call = ApiClient.getApiInmobiliaria().obtenerPropietario("Bearer " + token);
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()){
                    Propietario propietario = response.body();
                    if (propietario != null) {
                        mp.postValue(propietario);
                    } else {
                        Toast.makeText(getApplication(), "Datos del propietario vacíos", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplication(), "Error al cargar datos: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Toast.makeText(getApplication(), "Error de conexión", Toast.LENGTH_LONG).show();
            }
        });
    }
}