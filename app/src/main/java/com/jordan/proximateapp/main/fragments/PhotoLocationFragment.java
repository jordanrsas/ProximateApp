package com.jordan.proximateapp.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jordan.proximateapp.R;
import com.jordan.proximateapp.main.controllers.GenericFragment;
import com.jordan.proximateapp.utils.SharedPreferencesKeys;
import com.jordan.proximateapp.utils.SharedPrefsManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jordan on 07/02/2018.
 */

public class PhotoLocationFragment extends GenericFragment implements OnMapReadyCallback {

    @BindView(R.id.textMap)
    TextView textMap;

    private GoogleMap mMap;

    public static PhotoLocationFragment newInstance() {
        PhotoLocationFragment fragment = new PhotoLocationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.location_photo_fragment, container, false);
        initViews();
        return rootview;
    }


    @Override
    public void initViews() {
        ButterKnife.bind(this, rootview);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String lat = SharedPrefsManager.getInstance().getString(SharedPreferencesKeys.LATITUDE);
        String lon = SharedPrefsManager.getInstance().getString(SharedPreferencesKeys.LONGITUDE);

        if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lon)) {
            Double latitude = Double.parseDouble(lat);
            Double longitude = Double.parseDouble(lon);
            textMap.setVisibility(View.GONE);
            LatLng sydney = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(sydney)
                    .title("Ultima foto tomada/nLatitude:" + lat + " Longitude:" + lon));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        } else {
            textMap.setVisibility(View.VISIBLE);
        }
    }
}
