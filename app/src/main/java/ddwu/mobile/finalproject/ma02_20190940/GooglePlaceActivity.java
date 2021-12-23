package ddwu.mobile.finalproject.ma02_20190940;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class GooglePlaceActivity extends AppCompatActivity implements OnMapReadyCallback{

    final static String TAG = "GooglePlaceActivity";
    final static int PERMISSION_REQ_CODE = 100;

    private GoogleMap mGoogleMap;
    private MarkerOptions markerOptions;
    private LocationManager locationManager;

    private PlacesClient placesClient;

    Location location = null;
    LatLng currentLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_place);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (checkPermission())
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            currentLoc = new LatLng(37.604094, 127.042463);
        } else {
            currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
        }

        mapLoad();

        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_key));
        placesClient = Places.createClient(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
//                if (etKeyword.getText().toString().equals("cafe")) {
//                    searchStart(PlaceType.CAFE);
//                } else if (etKeyword.getText().toString().equals("restaurant")) {
//                    searchStart(PlaceType.RESTAURANT);
//                }
                searchStart(PlaceType.FLORIST, currentLoc);
                break;
        }
    }


    /*입력된 유형의 주변 정보를 검색*/
    private void searchStart(String type, LatLng latLng) {
        new NRPlaces.Builder().listener(placesListener)
                .key(getResources().getString(R.string.api_key))
                .latlng(latLng.latitude, latLng.longitude)
                .radius(2000)
                .type(type)
                .build()
                .execute();
    }


    /*Place ID 의 장소에 대한 세부정보 획득*/
    private void getPlaceDetail(String placeId) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.PHONE_NUMBER, Place.Field.ADDRESS);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener(
                new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse response) {
                        Place place = response.getPlace();

                        String detail = "상호명: " + place.getName();
                        detail += "\n\n전화번호: " + place.getPhoneNumber();
                        detail += "\n\n주소: " + place.getAddress();

                        AlertDialog.Builder oDialog = new AlertDialog.Builder(GooglePlaceActivity.this, R.style.MyAlertDialogStyle);

                        Typeface tf = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            tf = getResources().getFont(R.font.gmarketsans_ttf_medium);
                        }

                        TextView title = new TextView(GooglePlaceActivity.this);
                        title.setText("상세 정보");
                        title.setWidth(0);
                        title.setHeight(150);
                        title.setGravity(Gravity.CENTER);
                        title.setTextColor(Color.WHITE);
                        title.setTextSize(20);
                        title.setTypeface(tf);
                        title.setBackgroundColor(Color.rgb(63, 81, 181));

                        oDialog.setCustomTitle(title)
                                .setMessage(detail)
                                .setPositiveButton("확인", null)
                                .show();
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            Log.e(TAG, "Place not found: " + statusCode + " " + e.getMessage());
                            Toast.makeText(GooglePlaceActivity.this, "반경 2km 이내 화원을 찾지 못 했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }

    PlacesListener placesListener = new PlacesListener() {

        @Override
        public void onPlacesFailure(PlacesException e) {
        }

        @Override
        public void onPlacesStart() {
        }

        @Override
        public void onPlacesSuccess(final List<noman.googleplaces.Place> places) {
            Log.d(TAG, "Adding Markers");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (noman.googleplaces.Place place : places) {
                        markerOptions.title(place.getName());
                        markerOptions.position(new LatLng(place.getLatitude(), place.getLongitude()));
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_RED
                        ));
                        Marker newMarker = mGoogleMap.addMarker(markerOptions);
                        newMarker.setTag(place.getPlaceId());
                        Log.d(TAG, "ID:" + place.getPlaceId());
                    }
                }
            });
        }

        @Override
        public void onPlacesFinished() {
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        markerOptions = new MarkerOptions();
        Log.d(TAG, "Map ready");

        if (checkPermission()) {
            mGoogleMap.setMyLocationEnabled(true);
        }

        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Toast.makeText(GooglePlaceActivity.this, "내 위치 클릭", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mGoogleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                Toast.makeText(GooglePlaceActivity.this,
                        String.format("현재위치: (%f, %f)", location.getLatitude(), location.getLongitude()),
                        Toast.LENGTH_SHORT).show();
            }
        });

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String placeId = marker.getTag().toString();
                getPlaceDetail(placeId);
            }
        });
    }


    /*구글맵을 멤버변수로 로딩*/
    private void mapLoad() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(GooglePlaceActivity.this);
    }



    /* 필요 permission 요청 */
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 퍼미션을 획득하였을 경우 맵 로딩 실행
                mapLoad();
            } else {
                // 퍼미션 미획득 시 액티비티 종료
                Toast.makeText(this, "앱 실행을 위해 권한 허용이 필요함", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
