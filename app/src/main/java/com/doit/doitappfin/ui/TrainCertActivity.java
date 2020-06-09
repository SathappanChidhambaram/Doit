package com.doit.doitappfin.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.view.View;

import java.util.ArrayList;


import com.doit.doitappfin.R;
import com.doit.doitappfin.utils.certModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Handler;

import android.view.Menu;

import android.app.SearchManager;
import android.widget.EditText;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.Spanned;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class TrainCertActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private int[] imagestc = {R.drawable.apple, R.drawable.blue, R.drawable.mango, R.drawable.orange, R.drawable.apple, R.drawable.blue, R.drawable.mango, R.drawable.orange, R.drawable.apple, R.drawable.blue, R.drawable.mango, R.drawable.orange};
  private  String[]  fruitstc={"apple","grapes","mango","orange","apple","grapes","mango","orange","apple","grapes","mango","orange"};
    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;

    //@BindView(R.id.toolbar)


    private Intent i;
    int flag=0;
    //Toolbar toolbar;
    private Toolbar toolbar;

    // @BindView(R.id.swipe_refresh_recycler_list)
    // SwipeRefreshLayout swipeRefreshRecyclerList;

    private SwipeRefreshLayout swipeRefreshRecyclerList;
    private RecyclerViewAdapterTrainCert mAdapter;

private ArrayList<certModel> recycleViewModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_cert);
        findViews();


        i=getIntent();
        initToolbar(i.getStringExtra("val"));
        // ButterKnife.bind(this);




        swipeRefreshRecyclerList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Do your stuff on refresh
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (swipeRefreshRecyclerList.isRefreshing())
                            swipeRefreshRecyclerList.setRefreshing(false);
                    }
                }, 5000);

            }
        });
        if(connectedToNetwork()){
            volley();
        }else{ NoInternetAlertDialog(); }
    }

    private void volley() {
        i=getIntent();
        initToolbar(i.getStringExtra("val"));
        recycleViewModelList=new ArrayList<certModel>();



        if(i.getStringExtra("val").equals("Certification"))
        {
            DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("MainData").child("FinalCertification");
            recycleViewModelList.clear();
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot d1:dataSnapshot.getChildren())
                    {
                        certModel obj = d1.getValue(certModel.class);
                        recycleViewModelList.add(obj);
                        System.out.println("in act  "+obj.getImage());

                    }
                    mAdapter = new RecyclerViewAdapterTrainCert(TrainCertActivity.this, recycleViewModelList);
                    //   System.out.println("in act  "+recycleViewModelList.get(1).getPic());

                    setAdapter();

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        else {



            DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("MainTrainingData").child("CourseList");
            recycleViewModelList.clear();
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot d1:dataSnapshot.getChildren())
                    {
                        certModel obj = d1.getValue(certModel.class);


                        recycleViewModelList.add(obj);
                        System.out.println(obj.getTitle());
                    }
                    mAdapter = new RecyclerViewAdapterTrainCert(TrainCertActivity.this, recycleViewModelList);
                    //   System.out.println("in act  "+recycleViewModelList.get(1).getPic());

                    setAdapter();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });






        }
        setAdapter();



    }

    private void findViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshRecyclerList = findViewById(R.id.swipe_refresh_recycler_list);
    }

    public void initToolbar(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_search, menu);


        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));

        SearchManager searchManager = (SearchManager) this.getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

        //changing edittext color
        EditText searchEdit = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEdit.setTextColor(Color.WHITE);
        searchEdit.setHintTextColor(Color.WHITE);
        searchEdit.setBackgroundColor(Color.TRANSPARENT);
        searchEdit.setHint("Search");

        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(40);
        fArray[1] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {

                    if (!Character.isLetterOrDigit(source.charAt(i)))
                        return "";
                }


                return null;


            }
        };
        searchEdit.setFilters(fArray);
        View v = searchView.findViewById(androidx.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                ArrayList<certModel> filterList = new ArrayList<certModel>();
                if (s.length() > 0 ) {
                    for (int i = 0; i < recycleViewModelList.size(); i++) {
                        if (recycleViewModelList.get(i).getSearch().toLowerCase().contains(s.toLowerCase())) {
                            filterList.add(recycleViewModelList.get(i));
                            mAdapter.updateList(filterList);
                        }
                        else {
                            if(s.length()>5 && flag==0 ) {
                                showdial();
                                flag=1;
                            }
                        }
                    }

                } else {

                    mAdapter.updateList(recycleViewModelList);
                }
                return false;
            }
        });


        return true;
    }

    private void showdial() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("We couldn't find your query");
        builder.setPositiveButton("Inquire", new DialogInterface.OnClickListener() {
            @SuppressLint("MissingPermission")
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:9790718545"));

                startActivity(i);

            }
        });
        builder.setNegativeButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                flag=0;


            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


    private void setAdapter() {


        for (int i =0;i<imagestc.length;i++)
        {

        }



        mAdapter = new RecyclerViewAdapterTrainCert(TrainCertActivity.this, recycleViewModelList);



        final GridLayoutManager layoutManager = new GridLayoutManager(TrainCertActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new RecyclerViewAdapterTrainCert.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, certModel model) {

                //handle item click events here
               // Intent i =new Intent(TrainCertActivity.this, MapsActivity.class);

                //i.putExtra("title",model.getTitle());
                //i.putExtra("desc",model.getDesc());
                //startActivity(i);

                if(i.getStringExtra("val").equals("Certification"))
                {
                    if (model.getAddetails().equals("single")) {
                        Intent inten = (new Intent(TrainCertActivity.this, SingleActivity.class));
                        inten.putExtra("title", model.getTitle());
                        inten.putExtra("desc", model.getDesc());
                        inten.putExtra("image", model.getImage());
                        inten.putExtra("price", model.getPrice());

                        inten.putExtra("id", model.getId());
                        if(connectedToNetwork()){
                            startActivity(inten);
                        }
                        else{ NoInternetAlertDialog(); }

                    } else if (model.getAddetails().equals("list")) {
                        Intent inten = (new Intent(TrainCertActivity.this, ListDispActivity.class));
                        inten.putExtra("title", model.getTitle());
                        inten.putExtra("desc", model.getDesc());
                        inten.putExtra("image", model.getImage());
                        inten.putExtra("price", model.getPrice());

                        if(connectedToNetwork()){
                            startActivity(inten);
                        }
                        else{ NoInternetAlertDialog(); }
                    } else if (model.getAddetails().equals("box")) {

                        final Intent intent = (new Intent(TrainCertActivity.this, BoxActivity.class));
                        intent.putExtra("fromcert", model.getTitle());
                        intent.putExtra("price", model.getPrice());

                        if(connectedToNetwork()){
                            startActivity(intent);
                        }
                        else{ NoInternetAlertDialog(); }


                    }
                }
                else
                {
                    Intent inten=(new Intent(TrainCertActivity.this,MapsActivity.class));
                    inten.putExtra("title",model.getTitle());
                    inten.putExtra("desc",model.getDesc());
                    inten.putExtra("from","train");
                    inten.putExtra("image",model.getImage());
                    inten.putExtra("id",model.getId());
                    System.out.println("model id"+model.getId());
                    if(connectedToNetwork()){
                        startActivity(inten);

                    }
                    else{ NoInternetAlertDialog(); }

                }





            }
        });


    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

    public boolean connectedToNetwork() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isConnected();
        }

        return false;

    }




}
