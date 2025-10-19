package com.example.lab3.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeViewModel extends AndroidViewModel {

    private static final LatLng INMOBILIARIA = new LatLng(-33.280576, -66.332482);

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void configureMap(GoogleMap map) {
        if (map != null) {
            map.addMarker(new MarkerOptions()
                    .position(INMOBILIARIA)
                    .title("Inmobiliaria"));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(INMOBILIARIA, 15f));
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setCompassEnabled(true);
        }
    }
}