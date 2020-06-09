package com.doit.doitappfin.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.doit.doitappfin.R;
import com.doit.doitappfin.login.GoogleLoginActivity;
import com.doit.doitappfin.utils.ProfileData;
import com.doit.doitappfin.utils.UserDetails;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.provider.Settings;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Calendar;

public class proflie extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText name,city,address;
    TextView phone,email;
    Button b1,home;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    de.hdodenhof.circleimageview.CircleImageView photoTV;
    DatabaseReference db;
    TextView heademail;
    GoogleSignInAccount acct;
   private String omail="",oname="",odate="",onum="",ocity="",oaddr="",osex="",oiscomp="",ophoto="", nmail="",nname="",ndate="",nnum="",ncity="",naddr="",nsex="",niscomp="",nphoto="";
    SharedPreferences sharedpreferences;
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    GoogleSignInClient mGoogleSignInClient;
    EditText date1;
    DatePickerDialog datePickerDialog;

    private RadioGroup radioGroup;
    private RadioButton male, female, other;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proflie);



        radioGroup = findViewById(R.id.myRadioGroup);
        name= findViewById(R.id.editTextName);
        phone= findViewById(R.id.editphone);
        city= findViewById(R.id.editcity);
        address= findViewById(R.id.editaddress);
        email = findViewById(R.id.editemail);
        b1= findViewById(R.id.buttonUpdate);



       phone.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               View parentLayout = findViewById(android.R.id.content);
               Snackbar.make(parentLayout, "Phone is Inevitable", Snackbar.LENGTH_LONG)
                       .setAction("CLOSE", new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {

                           }
                       })
                       .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                       .show();
           }
       });

       email.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               View parentLayout = findViewById(android.R.id.content);
               Snackbar.make(parentLayout, "Email is Inevitable", Snackbar.LENGTH_LONG)
                       .setAction("CLOSE", new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {

                           }
                       })
                       .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                       .show();

           }
       });

        home = findViewById(R.id.buttonHome);

        male= findViewById(R.id.radioButtonMale);
        female= findViewById(R.id.radioButtonFemale);
        other= findViewById(R.id.radioButtonOthers);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toolbar.setTitle(" ");
        toggle.syncState();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        if(mAuth.getCurrentUser() == null){


            mGoogleSignInClient = GoogleSignIn.getClient(this,gso);}
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
//
        photoTV = header.findViewById(R.id.imageView123);
        heademail = header.findViewById(R.id.textView1234);

        acct = GoogleSignIn.getLastSignedInAccount(this);


        date1=findViewById(R.id.date);
        date1.setInputType(InputType.TYPE_NULL);
        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(proflie.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date1.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        if(connectedToNetwork()){
            volley();
        }else{ NoInternetAlertDialog(); }



         //   sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    //
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(connectedToNetwork()){
                        getData();
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        if(selectedId == male.getId()) {
                            nsex="Male";
                        } else if(selectedId == female.getId()) {
                            nsex="Female";
                        } else if(selectedId == other.getId()) {
                            nsex="Other";
                        }

                        if(isnotempty())
                        {

                            DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("ProfileData").child(omail.replace(".","_"));

                            db.child("name").setValue(nname.split("\n")[0]);
                            db.child("number").setValue(nnum.split("\n")[0]);
                            db.child("date").setValue(ndate.split("\n")[0]);
                            db.child("sex").setValue(nsex.split("\n")[0]);
                            db.child("addr").setValue(naddr.split("\n")[0]);
                            db.child("mail").setValue(nmail.split("\n")[0]);
                            db.child("city").setValue(ncity.split("\n")[0]);
                            db.child("iscomplete").setValue("YES").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        proflie.super.onBackPressed();
                                        //startActivity(new Intent(proflie.this,MainWorkActivity.class));
                                        Toast.makeText(proflie.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            if(acct!=null){
                                db.child("image").setValue( acct.getPhotoUrl().toString());


                            }

                        }





                    }else{ NoInternetAlertDialog(); }






    //
    //                SharedPreferences.Editor editor = sharedpreferences.edit();
    //
    //                editor.putString(Name, n);
    //                editor.putString(Phone, ph);
    //                editor.putString(Email, em);
    //                editor.putString(Dob,date);
    //                editor.putString(City,cit);
    //                editor.putString(Year,clas);
    //                editor.putString(Degree,deg);
    //                editor.putString(Face,dep);
    //                editor.putString(School,sch);
    //
    //
    //                editor.commit();

                }
            });

    }

    private void setData() {

        if(!oname.equals("NO"))
   name.setText(oname);
        if(!oaddr.equals("NO"))
            address.setText(oaddr);
        if(!onum.equals("NO"))
   phone.setText(onum);
        if(!ocity.equals("NO"))
   city.setText(ocity);
        if(!omail.equals("NO"))
   email.setText(omail);
        if(!odate.equals("NO"))
            date1.setText(odate);


        if(osex.equals("Male"))
        {
            male.toggle();
        }else if(osex.equals("Female"))
        {
            female.toggle();
        }
        else
        {
            other.toggle();
        }

   System.out.println(odate);
    }

    private boolean isnotempty() {

        if(nname.length()<=3)
        {
            name.setError("Enter a valid data");
            return false;

        }
        if(nnum.length()!=10)
        {
            phone.setError("Enter a valid data");
            return false;
        }

        if(ncity.length()<=3)
        {
            city.setError("Enter a valid data");
            return false;
        }

        if(naddr.length()<=3)
        {
            address.setError("Enter a valid data");
            return false;
        }

        if(ndate.length()<=3)
        {
            date1.setError("Enter a valid data");
            return false;
        }
        if(nsex.length()<=3)
        {

            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(nmail).matches())
        {
            email.setError("Enter a valid data");
            return false;
        }



        return true;

    }

    private void getData() {

        nname  = name.getText().toString();
        nnum= phone.getText().toString();
        ncity  = city.getText().toString();
        naddr  = address.getText().toString();
        nmail = email.getText().toString();
        ndate= date1.getText().toString();


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
     //   getMenuInflater().inflate(R.menu.proflie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
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

        }
        else if(id == R.id.nav_about_us){
startActivity(new Intent(proflie.this,AboutUsActivity.class));
        }
        else if (id == R.id.nav_signout) {
            SharedPreferences sp = getApplicationContext().getSharedPreferences("com.doitAppfin.PRIVATEDATA", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("number","___");
            editor.apply();
            if(mAuth!=null){
                mAuth.signOut();
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                            }
                        });


                Intent i=new Intent(this, GoogleLoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                if(connectedToNetwork()){
                    startActivity(i);}
            else {NoInternetAlertDialog();}
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

        if(acct!= null) {
            UserDetails.ProfilePhoto = acct.getPhotoUrl();
            UserDetails.name = acct.getDisplayName();
            Glide.with(this).load(acct.getPhotoUrl()).into(photoTV);
            heademail.setText(acct.getDisplayName());

            omail=acct.getEmail().replace(".","_");


            System.out.println( omail);

            FirebaseDatabase.getInstance().getReference().child("ProfileData").child(omail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ProfileData obj=dataSnapshot.getValue(ProfileData.class);
                    if(obj.getName()!=null)
                        oname=obj.getName();

                    if(obj.getAddr()!=null)
                        oaddr=(obj.getAddr());

                    if(obj.getSex()!=null)
                        osex=(obj.getSex());

                    if(obj.getCity()!=null)
                        ocity=(obj.getCity());

                    if(obj.getDate()!=null)
                        odate=(obj.getDate());

                    if(obj.getEmail()!=null)
                        omail=(obj.getEmail());

                    if(obj.getNumber()!=null)
                        onum=obj.getNumber();

                    if(obj.getIscomplete()!=null)
                        oiscomp=(obj.getIscomplete());

                    if(obj.getImage()!=null)
                        ophoto=(obj.getImage());

                    setData();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });







        }else{
            //onum=mAuth.getCurrentUser().getPhoneNumber();
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
                                        setData();

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
                });
            }


        }

    }

}
