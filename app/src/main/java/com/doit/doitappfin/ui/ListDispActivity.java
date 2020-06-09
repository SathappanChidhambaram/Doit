package com.doit.doitappfin.ui;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.doit.doitappfin.R;
import com.doit.doitappfin.utils.certModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ListDispActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    ImageView imviewlist;
    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;

    //@BindView(R.id.toolbar)
    //Toolbar toolbar;
    private Toolbar toolbar;

    // @BindView(R.id.swipe_refresh_recycler_list)
    // SwipeRefreshLayout swipeRefreshRecyclerList;

    private SwipeRefreshLayout swipeRefreshRecyclerList;
    private RecListDispAdapter mAdapter;

    private ArrayList<certModel> modelList = new ArrayList<>();

String stitle="",simage="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_disp);

        // ButterKnife.bind(this);
        findViews();
        setAdapter();


        if(connectedToNetwork()){
            volley();
        }else{ NoInternetAlertDialog(); }    }

    private void findViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshRecyclerList = findViewById(R.id.swipe_refresh_recycler_list);
        imviewlist=findViewById(R.id.imvlist);
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
                if (s.length() > 0) {
                    for (int i = 0; i < modelList.size(); i++) {
                        if (modelList.get(i).getTitle().toLowerCase().contains(s.toLowerCase())) {
                            filterList.add(modelList.get(i));
                            mAdapter.updateList(filterList);
                        }
                    }

                } else {
                    mAdapter.updateList(modelList);
                }
                return false;
            }
        });


        return true;
    }


    private void setAdapter() {


        mAdapter = new RecListDispAdapter(ListDispActivity.this, modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(ListDispActivity.this, R.drawable.divider_recyclerview));
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new RecListDispAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, certModel model) {
                Intent inten=(new Intent(ListDispActivity.this,SingleActivity.class));
                String  s=model.getTitle().replace("_p","+");
                  s=s.replace("_b","/");

                inten.putExtra("title",s);
                inten.putExtra("desc",model.getDesc());
                inten.putExtra("image",simage);
                inten.putExtra("price", model.getPrice());
                inten.putExtra("id", model.getId());

                startActivity(inten);

                //handle item click events here


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
        Intent i=getIntent();
        stitle= i.getStringExtra("title");
        simage= i.getStringExtra("image");
        initToolbar(stitle);

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
        System.out.println("in act  "+stitle);
        Glide.with(this)
                .load(simage).fitCenter().override(1000,1000)
                .into(imviewlist);
        DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("MainData").child("FinalCertification").child(stitle).child("DATA");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelList.clear();
                for(DataSnapshot d1:dataSnapshot.getChildren())
                {

                    certModel obj = d1.getValue(certModel.class);
                    modelList.add(obj);
                    System.out.println("in act  "+obj.getTitle());

                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAdapter = new RecListDispAdapter(ListDispActivity.this, modelList);


    }


}
