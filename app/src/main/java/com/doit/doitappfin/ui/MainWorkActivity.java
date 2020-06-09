package com.doit.doitappfin.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.doit.doitappfin.R;
import com.doit.doitappfin.login.GoogleLoginActivity;
import com.doit.doitappfin.utils.MyCustomPagerAdapter;
import com.doit.doitappfin.utils.ProfileData;
import com.doit.doitappfin.utils.RecyclerViewAdapter;
import com.doit.doitappfin.utils.UserDetails;
import com.doit.doitappfin.utils.adboxmodel;
import com.doit.doitappfin.utils.certModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainWorkActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout indicator;
    private ArrayList<adboxmodel> arrayListAdbox;
    Button test;

    private ViewPager viewPager;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    GoogleSignInClient mGoogleInCilients;
    de.hdodenhof.circleimageview.CircleImageView photoTV;
    private final int REQUEST_LOCATION_PERMISSION = 1;

    TextView heademail,t3;
    RecyclerView recyclerView, recyclerView1,recycleron;
    RelativeLayout relativeLayout;
    GoogleSignInAccount acct;
    RecyclerViewAdapter recyclerViewAdapterTrain, recyclerViewAdapterCert,recyclerViewAdapterOnlineTrain;
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    private ArrayList<certModel> cert, train,trainon;
    private String omail = "", oname = "", odate = "", onum = "", ocity = "", oaddr = "", osex = "", oiscomp = "", ophoto = "";
    RecyclerView.LayoutManager recylerViewLayoutManager;
    int[] images = {R.drawable.combo, R.drawable.place, R.drawable.online};
    MyCustomPagerAdapter myCustomPagerAdapter;
    String[] fruits = {"apple", "grapes", "mango", "orange"};
    String[] subjects = {"ANDROID", "PHP", "BLOGGER", "WORDPRESS", "JOOMLA", "ASP.NET", "JAVA", "C++", "MATHS", "HINDI", "ENGLISH"};
    private int[] myImageList = new int[]{R.drawable.apple, R.drawable.mango, R.drawable.straw, R.drawable.pineapple, R.drawable.orange, R.drawable.blue, R.drawable.water};
    private String[] myImageNameList = new String[]{"Apple", "Mango", "Strawberry", "Pineapple", "Orange", "Blueberry", "Watermelon"};

    private TextView tv1, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_work);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requestLocationPermission();

        test=findViewById(R.id.test);

        t3=findViewById(R.id.t3);
        recycleron=findViewById(R.id.recyclerViewon);


        mGoogleInCilients = GoogleSignIn.getClient(this, gso);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        photoTV = header.findViewById(R.id.imageView123);
        heademail = header.findViewById(R.id.textView123);


        tv1 = findViewById(R.id.t1);
        tv2 = findViewById(R.id.t2);

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainWorkActivity.this, TrainCertActivity.class);
                i.putExtra("val", "Certification");
                if (connectedToNetwork()) {
                    startActivity(i);
                } else {
                    NoInternetAlertDialog();
                }
            }
        });


        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainWorkActivity.this, TrainCertActivity.class);
                i.putExtra("val", "Training");
                if (connectedToNetwork()) {
                    startActivity(i);
                } else {
                    NoInternetAlertDialog();
                }

            }
        });

        t3.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {
                                      Intent i=new Intent(MainWorkActivity.this,online_training.class);
                                      i.putExtra("title","");
                                      startActivity(i);

                                  }
                              });

                test.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainWorkActivity.this, online_training.class);
                        startActivity(i);
                    }
                });


        indicator = findViewById(R.id.indicator);
        viewPager = findViewById(R.id.viewPager);

        indicator.setupWithViewPager(viewPager, true);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView1 = findViewById(R.id.recyclerView1);


        if (connectedToNetwork()) {
            volley();
        } else {
            NoInternetAlertDialog();
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MainWorkActivity.SliderTimer(), 6000, 7000);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_work, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            Intent i = (new Intent(getApplicationContext(), proflie.class));

            if (connectedToNetwork()) {
                startActivity(i);
            } else {
                NoInternetAlertDialog();
            }

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {


            Intent i = (new Intent(getApplicationContext(), MyCourseActivity.class));

            if (connectedToNetwork()) {
                startActivity(i);
            } else {
                NoInternetAlertDialog();
            }

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_combos) {

        } else if (id == R.id.nav_call) {

            Intent i = new Intent(Intent.ACTION_CALL);
            i.setData(Uri.parse("tel:9790718545"));
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return true;
            }
            startActivity(i);
        }
        else if(id == R.id.nav_about_us){
            startActivity(new Intent(MainWorkActivity.this,AboutUsActivity.class));

        }
        else if (id == R.id.nav_signout) {
            SharedPreferences sp = getApplicationContext().getSharedPreferences("com.doitAppfin.PRIVATEDATA", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("number","___");
            editor.apply();

            if(mAuth!=null){

                mAuth.signOut();

                mGoogleInCilients.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                            }
                        });




                finish();
                Intent i=new Intent(this, GoogleLoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                if(connectedToNetwork()){
                    startActivity(i);}else {
                    NoInternetAlertDialog();
                }            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            MainWorkActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < images.length -1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    private void setAdapter() {


        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        recyclerView1.setAdapter(recyclerViewAdapterCert);
        recyclerView.setAdapter(recyclerViewAdapterTrain);


        recycleron.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recycleron.setAdapter(recyclerViewAdapterOnlineTrain);




        recyclerViewAdapterCert.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, certModel model) {


                if (model.getAddetails().equals("single")) {
                    Intent inten = (new Intent(MainWorkActivity.this, SingleActivity.class));
                    inten.putExtra("title", model.getTitle());
                    inten.putExtra("desc", model.getDesc());
                    inten.putExtra("image", model.getImage());
                    inten.putExtra("price", model.getPrice());
                    inten.putExtra("id",model.getId());

                    if(connectedToNetwork()){
                        startActivity(inten);
                    }
                    else{ NoInternetAlertDialog(); }

                } else if (model.getAddetails().equals("list")) {
                    Intent inten = (new Intent(MainWorkActivity.this, ListDispActivity.class));
                    inten.putExtra("title", model.getTitle());
                    inten.putExtra("desc", model.getDesc());
                    inten.putExtra("price", model.getPrice());
                    inten.putExtra("image", model.getImage());
                    if(connectedToNetwork()){
                        startActivity(inten);
                    }
                    else{ NoInternetAlertDialog(); }

                } else if (model.getAddetails().equals("box")) {
                    final Intent intent = (new Intent(MainWorkActivity.this, BoxActivity.class));
                    intent.putExtra("fromcert", model.getTitle());
                    intent.putExtra("price", model.getPrice());

                    if(connectedToNetwork()){
                        startActivity(intent);
                    }
                    else{ NoInternetAlertDialog(); }

                }

            }
        });

        recyclerViewAdapterTrain.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, certModel model) {


                Intent inten=(new Intent(MainWorkActivity.this,MapsActivity.class));
                inten.putExtra("title",model.getTitle());
                inten.putExtra("desc",model.getDesc());
                inten.putExtra("from","main");
                inten.putExtra("image",model.getImage());
                inten.putExtra("id",model.getId());
                System.out.println("model tr "+model.getId());
                if(connectedToNetwork()){
                    startActivity(inten);
                }
                else{ NoInternetAlertDialog(); }

            }
        });


        recyclerViewAdapterOnlineTrain.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, certModel model) {
                Intent inten=(new Intent(MainWorkActivity.this,online_training.class));
                inten.putExtra("title",model.getTitle());
                if(connectedToNetwork())
                {
                    startActivity(inten);
                }
                else
                {
                    NoInternetAlertDialog();
                }
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE};
        if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
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
        cert=new ArrayList<certModel>();
        train=new ArrayList<certModel>();
        trainon=new ArrayList<>();

        arrayListAdbox=new ArrayList<>();

        acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!= null) {
            UserDetails.ProfilePhoto = acct.getPhotoUrl();
            UserDetails.name = acct.getDisplayName();
            Glide.with(this).load(acct.getPhotoUrl()).into(photoTV);
            FirebaseDatabase.getInstance().getReference().child("ProfileData").child(acct.getEmail().replace(".","_")).child("image").setValue(acct.getPhotoUrl().toString());
            heademail.setText(acct.getDisplayName());
        }
        else {

            SharedPreferences sp = getApplicationContext().getSharedPreferences("com.doitAppfin.PRIVATEDATA", Context.MODE_PRIVATE);

            onum=sp.getString("number","");
            // System.out.println(omail+" "+onum);

            if(onum.length()==10)
            {


                FirebaseDatabase.getInstance().getReference().child("LoginData").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot d1:dataSnapshot.getChildren())
                        {
                            if((d1.getValue().toString()).equals(onum))
                            {
                                // System.out.println(d1.getValue().toString()+" "+onum);
                                omail= d1.getKey().trim();

                                FirebaseDatabase.getInstance().getReference().child("ProfileData").child(omail).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        ProfileData obj=dataSnapshot.getValue(ProfileData.class);
                                        oname=obj.getName();
                                        oaddr=(obj.getAddr());
                                        osex=(obj.getSex());
                                        ocity=(obj.getCity());
                                        odate=(obj.getDate());
                                        omail=(obj.getEmail());
                                        onum=obj.getNumber();
                                        oiscomp=(obj.getIscomplete());
                                        ophoto=(obj.getImage());

                                        Glide.with(getApplicationContext()).load(ophoto).into(photoTV);
                                        heademail.setText(oname);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });}

        }




        FirebaseDatabase.getInstance().getReference().child("PopularData").child("Training").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                trainon.clear();

                for(DataSnapshot d1:dataSnapshot.getChildren())
                {
                    certModel obj=d1.getValue(certModel.class);
                    trainon.add(obj);
                }
                recyclerViewAdapterOnlineTrain=new RecyclerViewAdapter(MainWorkActivity.this,trainon);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("MainTrainingData").child("CourseList");
    //    recycleViewModelList.clear();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                train.clear();
                for(DataSnapshot d1:dataSnapshot.getChildren())
                {
                    certModel obj = d1.getValue(certModel.class);


                    train.add(obj);
                    //                  recycleViewModelList.add(obj);
                    System.out.println(obj.getTitle());
                }
                ArrayList<certModel> arrayList=new ArrayList<>();
                arrayList.addAll(train.subList(0,6));

                recyclerViewAdapterTrain = new RecyclerViewAdapter(MainWorkActivity.this, arrayList);
             //   mAdapter = new RecyclerViewAdapterTrainCert(TrainCertActivity.this, recycleViewModelList);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        FirebaseDatabase.getInstance().getReference().child("Adbox").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot d1:dataSnapshot.getChildren())
                {
                    adboxmodel adboxmode=d1.getValue(adboxmodel.class);
                    arrayListAdbox.add(adboxmode);
                }

                myCustomPagerAdapter = new MyCustomPagerAdapter(MainWorkActivity.this, arrayListAdbox);
                viewPager.setAdapter(myCustomPagerAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("PopularData").child("Certification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                cert.clear();

                for(DataSnapshot d1:dataSnapshot.getChildren())
                {
                    certModel obj=d1.getValue(certModel.class);
                    cert.add(obj);

                    System.out.println(obj);
                }
                recyclerViewAdapterCert = new RecyclerViewAdapter(MainWorkActivity.this, cert);
                setAdapter();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}
