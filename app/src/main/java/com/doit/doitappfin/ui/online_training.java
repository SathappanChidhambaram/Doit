package com.doit.doitappfin.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.doit.doitappfin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class online_training extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton normal,fastrack;
    private Spinner cat;
    ArrayList<String>  catdata=new ArrayList<>();
    ArrayAdapter<String> adapter;
    Button next;
    String title;
    String email="info.doit5@gmail.com",subject="Query regarding Online Training",body="Hi sir, \n I have queries regarding "  ,chooserTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_training);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Online Training");

        Intent i=getIntent();
        title=i.getStringExtra("title").toUpperCase();
        Log.e("title online",title);


        cat=findViewById(R.id.cat);
        next=findViewById(R.id.next);

        radioGroup=findViewById(R.id.RadioGroup);
        normal=findViewById(R.id.normal);
        fastrack=findViewById(R.id.fastrack);

        FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d1:dataSnapshot.getChildren())
                {
                    String obj=d1.getKey().toUpperCase();
                    // train.add(obj);

                    catdata.add(obj);
                    // Log.e("msg",obj);

                }


                catdata.add(title);

                adapter = new ArrayAdapter<String>
                        (online_training.this,android.R.layout.simple_spinner_dropdown_item,catdata);
                cat.setAdapter(adapter);

                cat.setSelection(catdata.indexOf(title));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(!title.equals(null) && !title.equals("")) {
//            int pos = cat.g
//            cat.setSelection(pos);
           // cat.setSelection(((ArrayAdapter<String>)cat.getAdapter()).getPosition(title));

        }

        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String duration,train;
                RadioButton rb;
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if(selectedId==-1)
                {
                    Toast.makeText(online_training.this,"Please Select Course Duration",Toast.LENGTH_SHORT).show();
                }
                else {
                    rb = findViewById(selectedId);
                    duration = rb.getText().toString();
                    train = cat.getSelectedItem().toString();
//                if(selectedId == normal.getId()) {
//                    duration="";
//                } else if(selectedId == fastrack.getId()) {
//                    nsex = "Female";
//                }
                    Log.e("duration", duration);
                    Log.e("title online", train);

                   // Intent i = new Intent(Intent.ACTION_CALL);
                   // i.setData(Uri.parse("tel:9790718545"));

                    body=body+" "+cat.getSelectedItem().toString()+" "+ duration +" Certification course";

                    String uriText="mailto:"+email+"?subject="+Uri.encode(subject)+"&body="+Uri.encode(body);
                    Uri uri=Uri.parse(uriText);
                    Intent emailIntent=new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(uri);
                    if (emailIntent.resolveActivity(getPackageManager())!=null) {
                        startActivity(Intent.createChooser(emailIntent,"send message"));
                    }

                  /*  emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
                    emailIntent.putExtra(Intent.EXTRA_TEXT,body);
                  //  emailIntent.setType("message/rfc822");
                    try {
                        startActivity(emailIntent);

                    }catch (android.content.ActivityNotFoundException e)
                    {
                        System.out.println(e);
                    }
*/

                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                    }
                    startActivity(emailIntent);
                }
            }
        });
    }


}
