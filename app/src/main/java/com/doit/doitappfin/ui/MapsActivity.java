package com.doit.doitappfin.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.doit.doitappfin.R;
import com.doit.doitappfin.utils.MyRecyclerViewAdapter;
import com.doit.doitappfin.utils.trainLocationObj;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,MyRecyclerViewAdapter.ItemClickListener {
    private GoogleMap mMap;

    private MyRecyclerViewAdapter adapter1;
    private  ArrayList< trainLocationObj> locobjList;
  
    ArrayList<Float> dist;
    private Button proceed;


    int a = 0;
    String p="",l="",li="",d="",image="",id="";

    private TextView ttitle, tdesc;

    private RecyclerView recyclerView;
    Geocoder geocoder;
    private FusedLocationProviderClient fusedLocationClient;
    private final int REQUEST_LOCATION_PERMISSION = 1;
HashMap<Integer, Marker> haMap;

    private String stitle = "", sdec = "";
    LocationManager locationManager;
Location locat;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        requestLocationPermission();
proceed=findViewById(R.id.button4);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }



    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }


    /**
     *
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney,Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     *  installed Google Play services and returned to the app.
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //
            //  TODO: Consider calling
            //   requestPermissions
            //


            return;
        }


        geocoder = new Geocoder(this, Locale.getDefault());
      // adapter1 = new MyRecyclerViewAdapter(MapsActivity.this, allar, dist);
      //adapter1.setClickListener(this);



        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            LatLng syd = new LatLng(location.getLatitude(), location.getLongitude());
                            locat=location;
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(syd,13f));

                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(syd)      // Sets the center of the map to Mountain View
                                    .zoom(13f)                   // Sets the zoom
                                    .build();
                        //    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            float f = 8;

                         //   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(syd, f));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(syd));
                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    Activity#requestPermissions

                                return;
                            }
                            mMap.setMyLocationEnabled(true);

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));






                        }
                    }





                });

        mMap.getUiSettings().setMapToolbarEnabled(false);



    }


    @SuppressWarnings("deprecation")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onLocationChanged(Location location) {


//

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions

            return;
        }
        mMap.setMyLocationEnabled(true);


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
    }

    @Override
    protected void onStart() {
        if(connectedToNetwork()){
            volley();
        }else{ NoInternetAlertDialog(); }
        super.onStart();

    }



    public boolean connectedToNetwork() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {

            return activeNetworkInfo.isConnected();
        }

        return false;

    }


    public void NoInternetAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You are not connected to the internet. ");
        builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(connectedToNetwork()){
                    volley();
                }else{ NoInternetAlertDialog(); }
            }
        });
        builder.setNegativeButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent openSettings = new Intent();
                openSettings.setAction(Settings.ACTION_WIRELESS_SETTINGS);
                openSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(openSettings);
            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void volley() {

        Intent i = getIntent();
        haMap=new HashMap<>();

        ttitle = findViewById(R.id.titl);
        tdesc = findViewById(R.id.decs);

        stitle = i.getStringExtra("title");
        sdec = i.getStringExtra("desc");
        image=i.getStringExtra("image");
        id=i.getStringExtra("id");

        ttitle.setText(stitle);
        tdesc.setText(sdec);
        dist = new ArrayList<Float>();
        recyclerView = findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


            final HashMap<String,String> hashMapMainTr=new HashMap<>();
            final DatabaseReference mm= FirebaseDatabase.getInstance().getReference().child("MainTrainingData").child("CoursePrice").child(stitle);
            mm.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   // System.out.println(dataSnapshot.getKey()+"price"+dataSnapshot.getValue().toString());

                    int i=0;
                    for(DataSnapshot D1:dataSnapshot.getChildren())
                    {

                              hashMapMainTr.put(D1.getKey(), D1.getValue().toString().trim());
                        System.out.println("hi " +hashMapMainTr.get(D1.getKey()));



                    }
                    GetLocationdata(hashMapMainTr);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });





    }

    private void GetLocationdata(HashMap<String, String> hashMapMainTr) {

        final DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("MainTrainingData").child("InstituteLocation");

        final HashMap<String,String> hashMapMainTrPriceCenter=new HashMap<>();
        final HashMap<String,String> hashMapMainTrDiscountCenter=new HashMap<>();

        Set<String> keys=hashMapMainTr.keySet();

        for (String i :keys)
        {

            if(!hashMapMainTr.get(i).equals("0"))
            {
                if(!i.contains("Discount")) {
                    hashMapMainTrPriceCenter.put(i, hashMapMainTr.get(i));
                    hashMapMainTrDiscountCenter.put(i, getdiscount(i, hashMapMainTr));

                }
            }

        }


        SOP(hashMapMainTrPriceCenter,hashMapMainTrDiscountCenter);








    }

    private void SOP(final HashMap<String, String> hashMapMainTrPriceCenter, final HashMap<String, String> hashMapMainTrDiscountCenter) {
        final Set<String> allkeys=hashMapMainTrPriceCenter.keySet();

        final ArrayList<trainLocationObj> LocobjHashmap =new ArrayList<>();
       DatabaseReference dbref= FirebaseDatabase.getInstance().getReference().child("MainTrainingData").child("InstituteLocation");
        adapter1 = new MyRecyclerViewAdapter(MapsActivity.this, LocobjHashmap);
adapter1.setClickListener(this);

       dbref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               for (String i :allkeys)
               {

                   DataSnapshot newdata=dataSnapshot.child(i);

                   if(newdata.exists())
                   {
                   //    System.out.println("exist" +i);

                       String latlng=newdata.child("latlng").getValue().toString();
                       String locid=newdata.child("locid").getValue()+"";
                       String name=newdata.child("name").getValue().toString();
                       String location=newdata.child("location").getValue().toString();
                       String price=hashMapMainTrPriceCenter.get(i);
                       String discont=hashMapMainTrDiscountCenter.get(i);
                       trainLocationObj obj=new trainLocationObj(location,name,latlng,locid,price,discont,  0.0);
                       LocobjHashmap.add(obj);
                   }
                   else
                   {
                     //  System.out.println("not exist" +i);

                   }


                   // System.out.println(i+" "+hashMapMainTrPriceCenter.get(i)+" "+hashMapMainTrDiscountCenter.get(i));
               }

               Sort(LocobjHashmap);



           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });





    }

    private void Sort(ArrayList<trainLocationObj> locobjHashmap) {

        ArrayList<String> latlng =new ArrayList<>();
        ArrayList<Float> distacnce =new ArrayList<>();
for(int i=0;i<locobjHashmap.size();i++) {
try {


    String sp[]=locobjHashmap.get(i).getLatlng().split("_");

   // System.out.println("herrr "+locobjHashmap.get(i).getLatlng()+"  "+locat.getLatitude()+"  "+locat.getLongitude());

    Double lt=Double.parseDouble(sp[0]+"");
    Double ln=Double.parseDouble(sp[1]+"");

    Location la=new Location("t"+i);
    la.setLatitude(lt);
    la.setLongitude(ln);

    float B = locat.distanceTo(la) / 1000;
    locobjHashmap.get(i).setY((double) B);

    distacnce.add(B);

}catch (IndexOutOfBoundsException e)
{
    System.out.println(e);
}


}



        for(int i=0;i<locobjHashmap.size();i++) {
            for(int j=i+1;j<locobjHashmap.size();j++) {
                if(distacnce.get(i)>distacnce.get(j))
                {
                    Float a=distacnce.get(i);
                    distacnce.set(i,distacnce.get(j));
                    distacnce.set(j,a);

                    trainLocationObj obj1=locobjHashmap.get(i);
                    locobjHashmap.set(i,locobjHashmap.get(j));
                    locobjHashmap.set(j,obj1);

                }

            }
        }


        for(int i=0;i<distacnce.size();i++) {

            String dis=locobjHashmap.get(i).getLatlng();
            String dp[]=dis.split("_");
            Marker m =mMap.addMarker((new MarkerOptions().position(new LatLng(Double.parseDouble(dp[0]), Double.parseDouble(dp[1])))).title(locobjHashmap.get(i).getLocation()+" "+locobjHashmap.get(i).getLocid())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

            haMap.put(i,m);
//            System.out.println("bla "+locobjHashmap.get(i).getLocation()+"  "+i+" "+locobjHashmap.get(i).getY());
        }


        DisplayListLoc(locobjHashmap);

    }

    private void DisplayListLoc(ArrayList< trainLocationObj> locobjHashmap) {

        for(int i=0;i<locobjHashmap.size();i++)
            System.out.println(locobjHashmap.get(i).getLocation());

        adapter1 = new MyRecyclerViewAdapter(MapsActivity.this, locobjHashmap);
        locobjList=locobjHashmap;

        recyclerView.setAdapter(adapter1);

        LatLng syd = new LatLng(locat.getLatitude(), locat.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(syd,13f));

        adapter1.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Marker m = haMap.get(position);
                if (m != null) {
                    m.showInfoWindow();
                    proceed.setVisibility(View.VISIBLE);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 15));

                    final trainLocationObj obj=locobjList.get(position);
                    proceed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent =new Intent(MapsActivity.this,PaymentActivity.class);
                            intent.putExtra("price",obj.getPrice());
                            intent.putExtra("name",stitle);
                            intent.putExtra("discount",obj.getDiscount());
                            intent.putExtra("location",obj.getLocation()+" DOIT-"+obj.getLocid());
                            intent.putExtra("area",obj.getLocation());
                            intent.putExtra("image","");
                            intent.putExtra("id",id);
                            startActivity(intent);
                        }
                    });





                }
            }
        });



    }

    private String getdiscount(String s, HashMap<String, String> hashMapMainTr) {

        Set<String> keys=hashMapMainTr.keySet();

        for(String i:keys)
        {

            if(i.contains(s) && i.contains("Discount"))
            {
               // System.out.println("baa"+i+" "+s);
                return hashMapMainTr.get(i);
            }
        }


        return "0";
    }

    @Override
    public void onItemClick(View view, int position) {
        Marker m= haMap.get(position);
        m.showInfoWindow();
       // System.out.println(m.getPosition().latitude);
        //System.out.println(m.getPosition().longitude);



/*
p=price.get(latmap.get(allat.get(position)));
d=stitle;
l=allarea.get(position);
li=latmap.get(allat.get(position));

        System.out.println(price.get(latmap.get(allat.get(position))));
        System.out.println(stitle);
        System.out.println(latmap.get(allat.get(position)));
        System.out.println(allarea.get(position));
*/




        //       mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(),15));


    }
}