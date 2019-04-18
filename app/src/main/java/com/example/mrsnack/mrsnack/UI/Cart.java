package com.example.mrsnack.mrsnack;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mrsnack.mrsnack.Adapters.CartRecyclerViewAdapter;
import com.example.mrsnack.mrsnack.DataBase.ProductAppDatabase;
import com.example.mrsnack.mrsnack.Modules.Order;
import com.example.mrsnack.mrsnack.Modules.ToSendOrder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {
    public static ProductAppDatabase mDataBase;
    public static double total = 0.0;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    LocationManager locationManager;
    LocationListener locationListener;
    public static String finalOrderText = "";
    private FusedLocationProviderClient mFusedLocationClient;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 20;

    String currentLocation;

    public static Button orderButton;
    public  Button enableLocation;
    public final String ORDER_TABLE = "orders";

    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    protected Location mLastLocation;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mDataBase = Room.databaseBuilder(this, ProductAppDatabase.class, "orders").build();

        recyclerView = (RecyclerView) findViewById(R.id.cartRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        orderButton = (Button) findViewById(R.id.totalBtn);
        enableLocation = (Button) findViewById(R.id.locationbtn);
        fab = (FloatingActionButton) findViewById(R.id.delFab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flushCart();
            }
        });

        enableLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        locationManager  = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        getData();


    }


    @Override
    protected void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermission();
        } else {

            if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                orderButton.setVisibility(View.GONE);
                enableLocation.setVisibility(View.VISIBLE);
            }else{
                orderButton.setVisibility(View.VISIBLE);
                enableLocation.setVisibility(View.GONE);
            }

            getLastLocation();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference ref = database.getReference(ORDER_TABLE);

            orderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


                    if(total < 1 ){
                        Toast.makeText(Cart.this , "Empty Order" , Toast.LENGTH_LONG).show();
                    }else {
                        //Insert Order To DataBase
                        ToSendOrder finalOrder = new ToSendOrder(finalOrderText,
                                currentFirebaseUser.getUid(),
                                total, currentDateTimeString, currentLocation);
                        ref.push().setValue(finalOrder);


                        AlertDialog alertDialog = new AlertDialog.Builder(Cart.this).create();
                        alertDialog.setTitle("Order Sucess");
                        alertDialog.setMessage("Thank you ,Your Order On the Way");

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                total = 0.0;
                                Cart.mDataBase.mDao().flushOrders();


                            }
                        }).start();

                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }

                }
            });


        }

    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i("Location", "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.textwarn, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i("Location", "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i("Location", "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i("Location", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.textwarn, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }


    private void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            Log.d("Location" , "not granted Permission");
        }else{
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLastLocation = task.getResult();

                                currentLocation = "Latitude : " + mLastLocation.getLatitude() +
                                        " | Longitude : " + mLastLocation.getLongitude();
                            } else {
                                Log.w("Location", "getLastLocation:exception", task.getException());

                            }
                        }
                    });
        }

    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(Cart.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    public void getData(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Order> orders = Cart.mDataBase.mDao().getOrders();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bindRecyclerView(new ArrayList<Order>(orders));

                    }
                });


            }
        });
        thread.start();

    }

    public void bindRecyclerView(ArrayList<Order> orders) {

        recyclerView.setAdapter(new CartRecyclerViewAdapter(this, orders));


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    public void flushCart(){

        new AlertDialog.Builder(this)
                .setTitle("Delete All")
                .setMessage("Do you really want to Delete All?")
                .setIcon(android.R.drawable.ic_delete)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                total = 0.0;
                                Cart.mDataBase.mDao().flushOrders();

                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);



                            }}).start();

                    }
                })
                .setNegativeButton(android.R.string.no, null).show();


    }


}


