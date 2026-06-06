package com.azan.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.batoulapps.adhan.*;
import com.batoulapps.adhan.data.DateComponents;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView tvFajr, tvDhuhr, tvAsr, tvMaghrib, tvIsha;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvFajr = findViewById(R.id.tvFajr);
        tvDhuhr = findViewById(R.id.tvDhuhr);
        tvAsr = findViewById(R.id.tvAsr);
        tvMaghrib = findViewById(R.id.tvMaghrib);
        tvIsha = findViewById(R.id.tvIsha);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getLocation();
        }
    }

    void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return;
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) showPrayerTimes(location);
        });
    }

    void showPrayerTimes(Location location) {
        Coordinates coordinates = new Coordinates(
                location.getLatitude(), location.getLongitude());
        DateComponents date = DateComponents.from(new Date());
        CalculationParameters params = CalculationMethod.MUSLIM_WORLD_LEAGUE.get();
        PrayerTimes times = new PrayerTimes(coordinates, date, params);

        tvFajr.setText("الفجر: " + times.fajr);
        tvDhuhr.setText("الظهر: " + times.dhuhr);
        tvAsr.setText("العصر: " + times.asr);
        tvMaghrib.setText("المغرب: " + times.maghrib);
        tvIsha.setText("العشاء: " + times.isha);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
    }
}
