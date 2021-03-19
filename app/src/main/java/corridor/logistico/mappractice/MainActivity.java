package corridor.logistico.mappractice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int PlayServiceCode = 10;
    private final int GPSRequestCode = 101;
    private FusedLocationProviderClient location_client;
    private GoogleMap mMap;
    private MapView mapView;
    private TextView textView_camera;
    private TextView textView_click;


    // when activity created...
    @SuppressLint("VisibleForTests")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initMap();
        mapView =  findViewById(R.id.map_fragment);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        location_client = new FusedLocationProviderClient(this
                .getApplicationContext());

    }

    private void init() {
        textView_camera = findViewById(R.id.latlngText);
        textView_click = findViewById(R.id.latlngText2);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);
        getLocation();
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCameraIdle() {
                //get latlng at the center by calling
                LatLng midLatLng = mMap.getCameraPosition().target;
                textView_camera.setText(getResources().getResourceName(R.string.lat) + midLatLng.latitude + "\n" +getResources().getResourceName(R.string.lng)+ midLatLng.longitude);
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onMapClick(LatLng latLng) {
                //get R.string.lat text  from string file
                textView_click.setText(getResources().getResourceName(R.string.lat) + latLng.latitude + "\n" +getResources().getResourceName(R.string.lng)+ latLng.longitude);
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location_client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    try {
                        Location location = task.getResult();
                        LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraPosition cameraPosition3_ = new CameraPosition.Builder().target(l).zoom(13).build();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, cameraPosition3_.bearing));
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition3_));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("TAG", "onComplete: " + e.getMessage());
                    }
                }
            }
        });
    }

    private void initMap() {
        if (isServiceOk()) {
            if (isCheckPermission()) {
                if (isGpsEnable()) {

                }
            } else {
                requestPermission();
            }
        } else {
            Toast.makeText(this, "Please Allow to permission to show your location", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean isServiceOk() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApiAvailability.isUserResolvableError(result)) {
            Dialog dialog = googleApiAvailability.getErrorDialog(this, result, PlayServiceCode, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface Task) {
                    Toast.makeText(MainActivity.this, "update cancel by user", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        } else {
            Toast.makeText(this, "this application require playservice", Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }
    }

    private boolean isCheckPermission() {
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<>();
//            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            Toast.makeText(this, "OnPermission", Toast.LENGTH_SHORT).show();
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for both permissions
//                perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
//                        &&
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    initMap();
//                        finish();
                    //else any one or both the permissions are not granted
                } else {
                    Toast.makeText(MainActivity.this, "you denie your permission", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                            .setMessage("Service Permission are required for this app")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermission();
                                }
                            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
                }
            }
        }
    }

    private boolean isGpsEnable() {
        LocationManager locationManager = null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            boolean providerEnable = Objects.requireNonNull(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (providerEnable) {
                return true;
            } else {
        new android.app.AlertDialog.Builder(this).setTitle("GPS Permission").setMessage("GPS Is Required For This APPlication..Please Enable Your GPS.").setPositiveButton("YEs", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(i, GPSRequestCode);
                    }
                }).setCancelable(false).show();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}

