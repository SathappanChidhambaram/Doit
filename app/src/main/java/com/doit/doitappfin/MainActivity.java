package com.doit.doitappfin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.doit.doitappfin.login.GoogleLoginActivity;
import com.doit.doitappfin.login.Registration;
import com.doit.doitappfin.ui.MainWorkActivity;
import com.doit.doitappfin.utils.certModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    ImageView imv;
    private FirebaseAuth mAuth;
    String mail = "", phone = "";

    Animation fromtop;

    @Override
    protected void onStart() {
        super.onStart();
        if(connectedToNetwork()){ }
        else{ NoInternetAlertDialog(); }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();


        String[] data = {"Microsoft", "Oracle", "CCNA", "CCNP", "CCNA Wireless", "CCNA Security", "CCNP Security", "CCIE", "ISTQB - BCS", "Intermidiate - BCS", "BCS Certified Tester Advanced Level Test Manager (TM12)", "ISTQB - ISQI", "Agile Tester Extension Level", "ISTQB - ITB", "Comptia A+", "Comptia N+", "Comptia S+", "VMWare", "Palo Alto", "SAS", "Pega Systems", "Check Point", "Juniper", "EMC", "Citrix", "ITIL Foundation - People Cert", "ITIL Intermediate - People Cert", "ITIL Practioner - People Cert", "Prince 2 Foundation - People Cert", "Prince 2 Practioner - People Cert", "Cobit 5 Foundation - People Cert", "Prince 2 Agile Foundation - People Cert", "Prince 2 Agile Practioner - People Cert", "Exin Agile Scrum Foundation", "Exin Agile Scrum Master", "Exin DevOps Master", "Exin DevOps Foundation", "DevOps Foundation - DevOps Institute", "Certified Scrum Master - GAQM.org", "Certified Scrum Master - Scrum Alliance", "Professional Scrum Master 1 - Scrum.org", "Google Cloud Associate Cloud Engineer", "Google Cloud Professional Cloud Architect", "AWS Associate - VUE", "AWS Professional - VUE", "Python", "Service Now", "Salesforce", "CEH V10", "ECSA V10", "TOGAF", "Blue Prism"};

        String[] prefix = {"INR", "$", "$", "$", "$", "$", "$", "$", "£", "_", "$", "INR", "INR", "INR", "$", "$", "$", "$", "$", "$", "$", "$", "$", "$", "$", "£", "£", "£", "£", "£", "£", "£", "£", "INR", "INR", "INR", "INR", "INR", "$", "INR", "$", "$", "$", "$", "$", "INR", "$", "$", "$", "$", "$", "$"};

        String[] price = {"5,664.00", "150.00", "383.50", "354.00", "354.00", "354.00", "354.00", "531.00", "98.00", "0", "227.74", "4,750.00", "4,751.00", "5,000.00", "145.14", "211.22", "224.20", "118.00", "188.80", "212.40", "206.50", "295.00", "236.00", "236.00", "354.00", "189.00", "283.00", "389.00", "189.00", "389.00", "189.00", "189.00", "389.00", "15,887.52", "16,980.00", "15,887.52", "15,887.52", "24,000.00", "128.00", "24,997.00", "150.00", "125.00", "200.00", "177.00", "354.00", "4,361.28", "150.00", "200.00", "1,414.82", "1,414.82", "584.10", "70.00"};

        String[] lname = {"dummy", "ORACLE", "CCNA", "CCNP", "CCNA wireless", "CCNA Security", "CCNP Security", "CCIE", "ISTQB", "ISTQB FOUNDATION LEVEL", "ISTQB", "ISQI", "Agile Tester", "ISTQB", "Comptia A+", "Comptia N+", "Comptia S+", "Vmware", "Paloalto", "Sas", "Pega", "Checkpoint", "Juniper", "DELL EMC CP", "CITRIX", "ITIL Foundation", "IITL INTER", "IITL PRACTIONER", "PRINCE2 Foundation", "Prince-2-practitioner", "cobit", "PRINCE AGILE", "PRINCE AGILE", "EXIN FOUNDATION", "EXIN MASTER", "DEVOPS MASTER", "DEVOPS FONDATION", "Devops Institute", "Gaqm", "Scrum Csm", "Scrum Psm", "Google Certified Cloud Manager", "Google Certified Cloud Architech", "Aws Developer Associate", "Aws Professional", "Python", "Service Now", "Salesforce", "Ceh", "ECSA", "Togaf", "BLUEPRISM"};

        String[] ss = {"CCNA", "CCNP", "LINUX", "UI", "PATH", "AWS", "MICROSOFT", "BLUEPRISM", "VMWARE", "PYTHON", "AUTOMATION", "ANYWHERE", "ORACLE", "PEGA", "TOGAF", "DEVOPS", "BIGDATA", "HADOOP", "IOT", "ISTQB", "Comptia", "SAS", "Citrix", "*", "ITIL", "Foundation", "Prince2", "Foundation", "Prince2", "Agile", "Foundation", "Google", "Cloud", "Associate", "Cloud", "Engineer", "Google", "Cloud", "Professional", "Cloud", "Architect", "Sales", "force", "Ethical", "Hacking"};


        DatabaseReference db, test;

        //  test=FirebaseDatabase.getInstance().getReference().child("MainData").child("FinalCertification");


        HashMap<String, certModel> h1 = new HashMap<>();
        String[] vda = {"Oracle", "MICROSOFT", "CISCO", "ISTQB", "Comptia", "Vmware", "Palo Alto", "SAS", "PEGA", "Check point", "Juniper", "Citrix", "Dell Emc", "AWS", "Google", "Prince 2", "Prince 2 Agile", "Cobit 5 Foundation - People Cert", "Service Now", "Salesforce", "CEH V10", "ECSA V10", "TOGAF", "Blue Prism"
        };


        HashMap<String, String> h = new HashMap<>();

        final DatabaseReference dab= FirebaseDatabase.getInstance().getReference().child("MainTrainingData").child("CourseList");

        dab.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot d1:dataSnapshot.getChildren())
                {
                    String sss=d1.child("id").getValue().toString();
                    System.out.println("hello mr "+d1.child("id").getValue());
                    dab.child(d1.getKey()).child("id").setValue(sss);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

/*
       DatabaseReference dbrefer= FirebaseDatabase.getInstance().getReference().child("MainData").child("FinalCertification").child("MICROSOFT").child("DATA");

        String SS[]={
                "AZ-103: Microsoft Azure Administrator",
        "AZ-203: Developing Solutions for Microsoft Azure",
        "AZ-300: Microsoft Azure Architect Technologies",
        "AZ-301: Microsoft Azure Architect Design",
        "AZ-400: Microsoft Azure DevOps Solutions",
        "AZ-500: Microsoft Azure Security Technologies",
        "AZ-900: Microsoft Azure Fundamentals",
        "DP-100: Designing and Implementing a Data Science Solution on Azure",
        "MB-200: Microsoft Power Platform + Dynamics 365 Core",
        "MS-300: Deploying Microsoft 365 Teamwork",
        "MS-100: Microsoft 365 Identity and Services",
        "MS-900: Microsoft 365 Fundamentals", "Microsoft Windows server (2016)","Microsoft Windows server (2012)"
        };


        for (int j=0;j<SS.length;j++)
        {
            HashMap<String,String> obj=new HashMap();
            obj.put("addetails","list");
            obj.put("desc",SS[j]);
            obj.put("id","cr-"+(79+j));
            obj.put("image","data");
            obj.put("price","4800 Rs- 5664 Rs");
            obj.put("title",SS[j]);
        //   dbrefer.child(SS[j]).setValue(obj);

           // System.out.println(SS[j]);

        }




*/

/*
        for(int i=0;i<vda.length;i++)
        {
            h.put(vda[i],"det");
        }

        FirebaseDatabase.getInstance().getReference().child("MainData").child("FinalCertification").setValue(h);


        for(int i=0;i<vda.length;i++)
{
  //  h1.put(vda[i],"det");



    test= FirebaseDatabase.getInstance().getReference().child("MainData").child("FinalCertification").child(vda[i]);

if(i==0)
{
String tr[]={"Java SE 7 Programmer Certified Associate", "Java SE 7 Programmer Certified Professional", "Java SE 6 Programmer Certified Professional","Oracle Database SQL Certified Expert ", "Oracle PL/SQL Developer Certified Associate", "Oracle Database 11g: Administrator Certified Associate", "Oracle Database 11g: Administrator Certified Professional", "Oracle Database 10g: Administrator Certified Associate", "Oracle Database 10g: Administrator Certified Professional", "Oracle Database 12c Administrator Certified Professional"};


for(int j=0;j<tr.length;j++)
{
    tr[j]=tr[j].replace("/","_b");
    certModel model=new certModel(tr[j],"data","data","data","list");


    h1.put(tr[j],model);
}
    test.child("DATA").setValue(h1);
    test.child("image").setValue("link");
    test.child("desc").setValue("desc");
    test.child("addetails").setValue("box");
    test.child("title").setValue(vda[i]);
//test.setValue(h1);


}


    if(i==1)
    {
        String tr[]={"Microsoft Windows", "Microsoft .NET Framework / Visual Studio", "Microsoft Excel", "Microsoft Office 365", "DevOps", "Microsoft Cloud", "Microsoft Office", "Microsoft Azure", "Microsoft Virtualization", "Microsoft SQL Server", "Microsoft Power BI", "Microsoft Windows Server", "Microsoft Skype for Business", "Microsoft Lync Server", "Microsoft SharePoint", "Microsoft System Center Manager", "Microsoft Exchange Server", "Microsoft PowerShell", "Microsoft Internet Information Services (IIS)", "Microsoft Office Communication Server", "Vendor Neutral - Technical", "Data & Analytics", "Developer edX", "Microsoft PowerApps and Flow", "Collaboration edX", "Microsoft Visual Studio Team Foundation Server (TFS )", "Blockchain", "Python", "Microsoft 365 (M365)", "IT Support", "Microsoft Cybersecurity", "Data Science", "Big Data", "Data Analysis", "Artificial Intelligence"};
h1.clear();

        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            certModel model=new certModel(tr[j],"data","data","data","list");


            h1.put(tr[j],model);


        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("box");
        test.child("title").setValue(vda[i]);


    }



    if(i==2)
    {
        String tr[]={"CCNA Routing & Switching", "CCNP Routing", "CCNP Switching", "CCNP Trouble Shooting", "CCNA Voice", "CCNA Wireless", "CCNA Security", "CCNP Security", "CCIE"};
        h1.clear();
        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            certModel model=new certModel(tr[j],"data","data","data","box");


            h1.put(tr[j],model);
        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("box");
        test.child("title").setValue(vda[i]);


    }



    if(i==3)
    {
        String tr[]={"ISTQB - BCS", "Intermediate BCS", "BCS Certified advanced Tested level Test Manager", "ISTQB - ISQI", "ISQI Test Manager", "ISTQB - ITB"};
        h1.clear();

        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            certModel model=new certModel(tr[j],"data","data","data","box");


            h1.put(tr[j],model);
        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("box");
        test.child("title").setValue(vda[i]);


    }


    if(i==4)
    {
        String tr[]={"Comptia A+", "Comptia N+", "Comptia S+"};
        h1.clear();

        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            tr[j]=tr[j].replace("+","_p");
            certModel model=new certModel(tr[j],"data","data","data","box");

            h1.put(tr[j],model);
        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("box");
        test.child("title").setValue(vda[i]);


    }




    if(i==5)
    {
        String tr[]={"VMware Certified Professional 6 - Data Center Virtualization", "VMware Certified Professional 6.5 - Data Center Virtualization", "VMware Certified Professional 6 - Cloud Management and Automation", "VMware Certified Professional 6 - Network Virtualization 6.2 Exam", "VMware Certified Professional 7 - Cloud Management and Automation (VCP7-CMA)", "VMware Certified Associate 6 - Data Center Virtualization Fundamentals"};
        h1.clear();

        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            tr[j]=tr[j].replace("+","_p");
            certModel model=new certModel(tr[j],"data","data","data","list");

            h1.put(tr[j],model);
        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("list");
        test.child("title").setValue(vda[i]);


    }




    if(i==6)
    {
        String tr[]={"Palo Alto Networks Certified Cybersecurity Associate", "Palo Alto Networks Certified Network Security Administrator", "Palo Alto Networks Certified Network Security Engineer"};

        h1.clear();

        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            tr[j]=tr[j].replace("+","_p");
            certModel model=new certModel(tr[j],"data","data","data","list");

            h1.put(tr[j],model);
        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("list");
        test.child("title").setValue(vda[i]);


    }



    if(i==7)
    {
        String tr[]={"SAS Certified Base Programmer For SAS 9"};
        h1.clear();

        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            tr[j]=tr[j].replace("+","_p");
            certModel model=new certModel(tr[j],"data","data","data","list");

            h1.put(tr[j],model);
        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("list");
        test.child("title").setValue(vda[i]);


    }


    if(i==8)
    {
        String tr[]={"Certified Pega Marketing Consultant (CPMC) 74V1","Certified System Architect (CSA) 74V1", "Pegasystems Certified Senior System Architect","Pegasystems Certified Pega Decisioning Consultant"};
        h1.clear();

        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            tr[j]=tr[j].replace("+","_p");
            certModel model=new certModel(tr[j],"data","data","data","list");

            h1.put(tr[j],model);
        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("list");
        test.child("title").setValue(vda[i]);



    }


    if(i==9)
    {
        String tr[]={"Check Point Certified Security Principles Associate", "Checkpoint Check Point Certified Security Master", "Check Point Certified Security Administrator", "Check Point Certified Security Expert", "CheckPoint Check Point Certified PenTesting Associate", "Check Point Certified Automation Specialist"};

        h1.clear();

        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            tr[j]=tr[j].replace("+","_p");
            certModel model=new certModel(tr[j],"data","data","data","list");

            h1.put(tr[j],model);
        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("list");
        test.child("title").setValue(vda[i]);



    }

    if(i==10)
    {
        String tr[]={"Juniper Networks Certified Internet Associate, Junos", "Enterprise Routing and Switching, Specialist"
        };

        h1.clear();

        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            tr[j]=tr[j].replace("+","_p");
            certModel model=new certModel(tr[j],"data","data","data","list");

            h1.put(tr[j],model);
        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("list");
        test.child("title").setValue(vda[i]);


    }



    if(i==11)
    {
        String tr[]={"Citrix XenApp and XenDesktop 7.15 Administration", "Citrix NetScaler 12 Essentials and Unified Gateway"
        };
        h1.clear();

        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            tr[j]=tr[j].replace("+","_p");
            certModel model=new certModel(tr[j],"data","data","data","list");

            h1.put(tr[j],model);
        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("list");
        test.child("title").setValue(vda[i]);


    }



    if(i==12)
    {
        String tr[]={"Information Storage and Management v3", "Specialist, Technology Architect | Midrange Storage Solutions", "Dell EMC Certified Specialist - Systems Administrator, Data Domain","Storage Administrator, VMAX All Flash and VMAX3 Solutions Specialist Version 2.0"};
        h1.clear();

        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            tr[j]=tr[j].replace("+","_p");
            certModel model=new certModel(tr[j],"data","data","data","list");

            h1.put(tr[j],model);
        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("list");
        test.child("title").setValue(vda[i]);


    }





    if(i==13)
    {
        String tr[]={"AWS Solution Architect Associate", "AWS Solution Architect Professional", "AWS Sysops Admin Associate", "AWS Developer Associate", "AWS Devops Professional"
        };

        h1.clear();

        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            tr[j]=tr[j].replace("+","_p");
            certModel model=new certModel(tr[j],"data","data","data","box");

            h1.put(tr[j],model);
        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("box");
        test.child("title").setValue(vda[i]);


    }

    if(i==14)
    {
        String tr[]={"Google Cloud Associate Cloud Engineer", "Google Cloud Professional Cloud Architect"};
        h1.clear();

        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            tr[j]=tr[j].replace("+","_p");
            certModel model=new certModel(tr[j],"data","data","data","box");

            h1.put(tr[j],model);
        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("box");
        test.child("title").setValue(vda[i]);


    }



    if(i==15)
    {
        String tr[]={"Prince 2 Foundation - People Cert", "Prince 2 Practioner - People Cert"};
        h1.clear();

        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            tr[j]=tr[j].replace("+","_p");
            certModel model=new certModel(tr[j],"data","data","data","box");

            h1.put(tr[j],model);
        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("box");
        test.child("title").setValue(vda[i]);


    }


    if(i==16)
    {
        String tr[]={"Prince 2 Agile Foundation - People Cert", "Prince 2 Agile Practioner - People Cert"

        };
        h1.clear();

        for(int j=0;j<tr.length;j++)
        {
            tr[j]=tr[j].replace("/","_b");
            tr[j]=tr[j].replace(".","_");
            tr[j]=tr[j].replace("+","_p");
            certModel model=new certModel(tr[j],"data","data","data","box");

            h1.put(tr[j],model);
        }

        test.child("DATA").setValue(h1);
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("addetails").setValue("box");
        test.child("title").setValue(vda[i]);


    }

    if(i==17)
    {

        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("price").setValue("price");
        test.child("addetails").setValue("single");
        test.child("title").setValue(vda[i]);


    }

   if(i==18)
    {
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("price").setValue("price");
        test.child("addetails").setValue("single");
        test.child("title").setValue(vda[i]);
    }    if(i==19)
{
    test.child("image").setValue("link");
    test.child("desc").setValue("desc");
    test.child("price").setValue("price");
    test.child("addetails").setValue("single");
    test.child("title").setValue(vda[i]);

}
    if(i==20)
    {
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("price").setValue("price");
        test.child("addetails").setValue("single");
        test.child("title").setValue(vda[i]);

    }    if(i==21)
{
    test.child("image").setValue("link");
    test.child("desc").setValue("desc");
    test.child("price").setValue("price");
    test.child("addetails").setValue("single");
    test.child("title").setValue(vda[i]);

}

    if(i==22)
    { test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("price").setValue("price");
        test.child("addetails").setValue("single");
        test.child("title").setValue(vda[i]);

    }
    if(i==23)
    {
        test.child("image").setValue("link");
        test.child("desc").setValue("desc");
        test.child("price").setValue("price");
        test.child("addetails").setValue("single");
        test.child("title").setValue(vda[i]);


    }


}*/

//test.setValue(h1);


      /*  String[] scnna = {"Alltechz Solutions Pvt Ltd", "Trichrome Technologies", "Infycle Technologies", "Sevael Technologies", "Msquare Networks", "Geo Insys", "V Tech Soft IT Service", " Fita Training & Placement", "JPA Solutions", "Hope Tutors", "Network Geek", "Credo Systemz", "Zuan Education", "Accord", "IICT Training Institute", "Ampersand Academy", "SA Techno Solutions", "EYE Open", "G-Tech", "Greens Technologies"};
        double[] pcnna = {12000, 0, 0, 0, 7500, 0, 0, 0, 0, 10000, 10000, 13000, 15999, 20000, 0, 0, 0, 0, 0, 0};

        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("CCNA");
        db.child("desc").setValue("desc");
        //  db.child("image").setValue("url");
        db.child("title").setValue("CCNA");
        db.child("addetails").setValue("add");

        if (scnna.length == pcnna.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < scnna.length; i++) {

                scnna[i] = scnna[i].replace("-", "_m");
                scnna[i] = scnna[i].replace("&", "_a");
                hashMap.put(scnna[i], pcnna[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] CCNP = {"Alltechz Solutions Pvt Ltd", "Msquare Networks", "Network Geek", "IICT Training Institute", "G-Tech"};
        double[] pCCNP = {14000, 12000, 10000, 0, 0};
        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("CCNP");
        db.child("desc").setValue("desc");
        //db.child("image").setValue("url");
        db.child("title").setValue("CCNP");
        db.child("addetails").setValue("add");

        if (CCNP.length == pCCNP.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < CCNP.length; i++) {

                CCNP[i] = CCNP[i].replace("-", "_m");
                CCNP[i] = CCNP[i].replace("&", "_a");
                hashMap.put(CCNP[i], pCCNP[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] linux = {"Alltechz Solutions Pvt Ltd", "JPA Solutions", "IICT Training Institute", "Greens Technologies"};
        double[] plinux = {13000, 0, 0, 0};

        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("LINUX");
        db.child("desc").setValue("desc");
        //db.child("image").setValue("url");
        db.child("title").setValue("LINUX");
        db.child("addetails").setValue("add");

        if (linux.length == plinux.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < plinux.length; i++) {

                linux[i] = linux[i].replace("-", "_m");
                linux[i] = linux[i].replace("&", "_a");
                hashMap.put(linux[i], plinux[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] ui = {"V Tech Soft IT Service", "Hope Tutors", "Geo Insys", "Fita Training & Placement", "JPA Solutions"};
        double[] pui = {0, 10000, 0, 0, 0};

        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("UI PATH");
        db.child("desc").setValue("desc");
        //  db.child("image").setValue("url");
        db.child("title").setValue("UI PATH");
        db.child("addetails").setValue("add");

        if (ui.length == pui.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < pui.length; i++) {

                ui[i] = ui[i].replace("-", "_m");
                ui[i] = ui[i].replace("&", "_a");
                hashMap.put(ui[i], pui[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] aws = {"Fita Training & Placement", "Infycle Technologies", "JPA Solutions", "Hope Tutors", "SA Techno Solutions", "G-Tech", "Greens Technologies", "Alltechz Solutions Pvt Ltd"};
        double[] paws = {0, 0, 20000, 13000, 0, 0, 15000, 13000};

        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("AWS");
        db.child("desc").setValue("desc");
        //  db.child("image").setValue("url");
        db.child("title").setValue("AWS");
        db.child("addetails").setValue("add");

        if (aws.length == paws.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < aws.length; i++) {

                aws[i] = aws[i].replace("-", "_m");
                aws[i] = aws[i].replace("&", "_a");
                hashMap.put(aws[i], paws[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] microsoft = {"Sevael Technologies", "Alltechz Solutions Pvt Ltd", "JPA Solutions", "Hope Tutors", "EYE Open", "G-Tech", "Greens Technologies"};
        double[] pmicrosoft = {0, 13000, 20000, 18000, 0, 0, 15000};


        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("MICROSOFT");
        db.child("desc").setValue("desc");
        //  db.child("image").setValue("url");
        db.child("title").setValue("MICROSOFT");
        db.child("addetails").setValue("add");

        if (microsoft.length == pmicrosoft.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < microsoft.length; i++) {

                microsoft[i] = microsoft[i].replace("-", "_m");
                microsoft[i] = microsoft[i].replace("&", "_a");
                hashMap.put(microsoft[i], pmicrosoft[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] blue = {"V Tech Soft IT Service", "Hope Tutors", "Geo Insys", "Fita Training & Placement", "JPA Solutions", "Credo Systemz", "Greens Technologies", "Alltechz Solutions Pvt Ltd"};
        double[] pblue = {0, 0, 0, 0, 0, 13000, 10000, 11000};


        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("BLUEPRISM");
        db.child("desc").setValue("desc");
        //  db.child("image").setValue("url");
        db.child("title").setValue("BLUEPRISM");
        db.child("addetails").setValue("add");
        if (blue.length == pblue.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < blue.length; i++) {

                blue[i] = blue[i].replace("-", "_m");
                blue[i] = blue[i].replace("&", "_a");
                hashMap.put(blue[i], pblue[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] vmware = {"Msquare Networks", "Fita Training & Placement", "JPA Solutions", "IICT Training Institute", "SA Techno Solutions", "Alltechz Solutions Pvt Ltd"};
        double[] pvmware = {0, 0, 0, 0, 0, 14000};

        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("VMWARE");
        db.child("desc").setValue("desc");
        //db.child("image").setValue("url");
        db.child("addetails").setValue("add");

        db.child("title").setValue("VMWARE");
        if (vmware.length == pvmware.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < vmware.length; i++) {

                vmware[i] = vmware[i].replace("-", "_m");
                vmware[i] = vmware[i].replace("&", "_a");
                hashMap.put(vmware[i], pvmware[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] PYTHON = {"Infycle Technologies", "Geo Insys", "JPA Solutions", "Hope Tutors", "Credo Systemz", "Zuan Education", "Accord", "IICT Training Institute", "Ampersand Academy", "SA Techno Solutions", "EYE Open", "G-Tech", "Alltechz Solutions Pvt Ltd"};
        double[] pPYTHON = {0, 20000, 16000, 24000, 15000, 14999, 15000, 15000, 18000, 0, 16000, 0, 7000};

        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("PYTHON");
        db.child("desc").setValue("desc");
        //  db.child("image").setValue("url");
        db.child("addetails").setValue("add");
        db.child("title").setValue("PYTHON");
        if (PYTHON.length == pPYTHON.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < PYTHON.length; i++) {

                PYTHON[i] = PYTHON[i].replace("-", "_m");
                PYTHON[i] = PYTHON[i].replace("&", "_a");
                hashMap.put(PYTHON[i], pPYTHON[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] AUTOMATIONANYWHERE = {"V Tech Soft IT Service", "Hope Tutors", "Fita Training & Placement", "Credo Systemz", "Alltechz Solutions Pvt Ltd", "Greens Technologies", "EYE Open"};
        double[] pAUTOMATIONANYWHERE = {0, 10000, 0, 13000, 9000, 10000, 18000};


        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("AUTOMATION ANYWHERE");
        db.child("desc").setValue("desc");
        //db.child("image").setValue("url");
        db.child("addetails").setValue("add");
        db.child("title").setValue("AUTOMATION ANYWHERE");
        if (AUTOMATIONANYWHERE.length == pAUTOMATIONANYWHERE.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < AUTOMATIONANYWHERE.length; i++) {

                AUTOMATIONANYWHERE[i] = AUTOMATIONANYWHERE[i].replace("-", "_m");
                AUTOMATIONANYWHERE[i] = AUTOMATIONANYWHERE[i].replace("&", "_a");
                hashMap.put(AUTOMATIONANYWHERE[i], pAUTOMATIONANYWHERE[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] ORACLE = {"Infycle Technologies", "V Tech Soft IT Service", "Fita Training & Placement", "JPA Solutions", "Hope Tutors", "Credo Systemz", "Zuan Education", "IICT Training Institute", "Ampersand Academy", "EYE Open", "Greens Technologies", "SA Techno Solutions"};
        double[] pORACLE = {0, 0, 0, 0, 12000, 15000, 15999, 0, 20000, 0, 10000};


        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("ORACLE");
        db.child("desc").setValue("desc");
        // db.child("image").setValue("url");
        db.child("addetails").setValue("add");
        db.child("title").setValue("ORACLE");
        if (ORACLE.length == pORACLE.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < ORACLE.length; i++) {

                ORACLE[i] = ORACLE[i].replace("-", "_m");
                ORACLE[i] = ORACLE[i].replace("&", "_a");
                hashMap.put(ORACLE[i], pORACLE[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] PEGA = {"V Tech Soft IT Service", "Credo Systemz", "IICT Training Institute", "Greens Technologies"};
        double[] pPEGA = {0, 0, 0, 0};


        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("PEGA");
        db.child("desc").setValue("desc");
        db.child("addetails").setValue("add");
        //db.child("image").setValue("url");
        db.child("title").setValue("PEGA");
        if (PEGA.length == pPEGA.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < PEGA.length; i++) {

                PEGA[i] = PEGA[i].replace("-", "_m");
                PEGA[i] = PEGA[i].replace("&", "_a");
                hashMap.put(PEGA[i], pPEGA[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] TOGAF = {"Greens Technologies"};
        double[] pTOGAF = {0};


        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("TOGAF");
        db.child("desc").setValue("desc");
        // db.child("image").setValue("url");
        db.child("addetails").setValue("add");
        db.child("title").setValue("TOGAF");
        if (TOGAF.length == pTOGAF.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < TOGAF.length; i++) {

                TOGAF[i] = TOGAF[i].replace("-", "_m");
                TOGAF[i] = TOGAF[i].replace("&", "_a");
                hashMap.put(TOGAF[i], pTOGAF[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] DEVOPS = {"Fita Training & Placement", "JPA Solutions", "Hope Tutors", "Greens Technologies"};
        double[] pDEVOPS = {0, 20000, 13000, 15000};


        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("DEVOPS");
        db.child("desc").setValue("desc");
        db.child("addetails").setValue("add");
        // db.child("image").setValue("url");
        db.child("title").setValue("DEVOPS");
        if (DEVOPS.length == pDEVOPS.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < DEVOPS.length; i++) {

                DEVOPS[i] = DEVOPS[i].replace("-", "_m");
                DEVOPS[i] = DEVOPS[i].replace("&", "_a");
                hashMap.put(DEVOPS[i], pDEVOPS[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] BIGDATA = {"JPA Solutions", "Greens Technologies", "EYE Open", "V Tech Soft IT Service", "Fita Training & Placement", "Zuan Education", "IICT Training Institute", "SA Techno Solutions"};
        double[] pBIGDATA = {0, 0, 0, 0, 0, 0, 0, 0};


        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("BIGDATA HADOOP");
        db.child("desc").setValue("desc");
        // db.child("image").setValue("url");
        db.child("addetails").setValue("add");
        db.child("title").setValue("BIGDATA HADOOP");
        if (BIGDATA.length == pBIGDATA.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < BIGDATA.length; i++) {

                BIGDATA[i] = BIGDATA[i].replace("-", "_m");
                BIGDATA[i] = BIGDATA[i].replace("&", "_a");
                hashMap.put(BIGDATA[i], pBIGDATA[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] IOT = {"Zuan Education", "EYE Open", "Greens Technologies"};
        double[] pIOT = {5999, 0, 0};


        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("IOT");
        db.child("desc").setValue("desc");
        db.child("addetails").setValue("add");
        //db.child("image").setValue("url");
        db.child("title").setValue("IOT");
        if (IOT.length == pIOT.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < IOT.length; i++) {

                IOT[i] = IOT[i].replace("-", "_m");
                IOT[i] = IOT[i].replace("&", "_a");
                hashMap.put(IOT[i], pIOT[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] ISTQB = {"Alltechz Solutions Pvt Ltd", "Sevael Technologies", "Credo Systemz"};
        double[] pISTQB = {0, 0, 0};


        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("ISTQB");
        db.child("desc").setValue("desc");
        db.child("addetails").setValue("add");
        //db.child("image").setValue("url");
        db.child("title").setValue("ISTQB");
        if (ISTQB.length == pISTQB.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < ISTQB.length; i++) {

                ISTQB[i] = ISTQB[i].replace("-", "_m");
                ISTQB[i] = ISTQB[i].replace("&", "_a");
                hashMap.put(ISTQB[i], pISTQB[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] COMPTIA = {"Msquare Networks", "Alltechz Solutions Pvt Ltd", "Credo Systemz"};
        double[] pCOMPTIA = {0, 0, 0};


        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("COMPTIA");
        db.child("desc").setValue("desc");
        db.child("addetails").setValue("add");
        //db.child("image").setValue("url");
        db.child("title").setValue("DEVOPS");
        if (COMPTIA.length == pCOMPTIA.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < COMPTIA.length; i++) {

                COMPTIA[i] = COMPTIA[i].replace("-", "_m");
                COMPTIA[i] = COMPTIA[i].replace("&", "_a");
                hashMap.put(COMPTIA[i], pCOMPTIA[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] Citrix = {"Greens Technologies", "SA Techno Solutions"};
        double[] pCitrix = {0, 0};

        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("Citrix");
        db.child("desc").setValue("desc");
        // db.child("image").setValue("url");
        db.child("addetails").setValue("add");
        db.child("title").setValue("Citrix");
        if (Citrix.length == pCitrix.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < Citrix.length; i++) {

                Citrix[i] = Citrix[i].replace("-", "_m");
                Citrix[i] = Citrix[i].replace("&", "_a");
                hashMap.put(Citrix[i], pCitrix[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] ITIL = {"Alltechz Solutions Pvt Ltd", "Sevael Technologies"};
        double[] pITIL = {0, 0};


        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("ITIL Foundation");
        db.child("desc").setValue("desc");
        db.child("addetails").setValue("add");
        //db.child("image").setValue("url");
        db.child("title").setValue("ITIL Foundation");
        if (ITIL.length == pITIL.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < ITIL.length; i++) {

                ITIL[i] = ITIL[i].replace("-", "_m");
                ITIL[i] = ITIL[i].replace("&", "_a");
                hashMap.put(ITIL[i], pITIL[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] Prince2 = {"Sevael Technologies"};
        double[] pPrince2 = {0};
        db.child("addetails").setValue("add");


        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("Prince2 Foundation");
        db.child("desc").setValue("desc");
        //  db.child("image").setValue("url");
        db.child("title").setValue("Prince2 Foundation");
        if (Prince2.length == pPrince2.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < Prince2.length; i++) {

                Prince2[i] = Prince2[i].replace("-", "_m");
                Prince2[i] = Prince2[i].replace("&", "_a");
                hashMap.put(Prince2[i], pPrince2[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }


        String[] ethical = {"Sevael Technologies", "Network Geek", "Msquare Networks"};
        double[] pethical = {0, 0, 0};

        db = FirebaseDatabase.getInstance().getReference().child("MainData").child("TrainingData").child("Ethical Hacking");
        db.child("desc").setValue("desc");
        db.child("addetails").setValue("add");
        //db.child("image").setValue("url");
        db.child("title").setValue("Ethical Hacking");
        if (ethical.length == pethical.length) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < ethical.length; i++) {

                ethical[i] = ethical[i].replace("-", "_m");
                ethical[i] = ethical[i].replace("&", "_a");
                hashMap.put(ethical[i], pethical[i] + "");

            }
            db.child("Centers").setValue(hashMap);
        }

*/
///////////////////////////////////////////////////////////////

        //  + =  %2B
// space = %20
/*
        if(data.length==prefix.length && data.length==price.length && lname.length==data.length)
        {


            for (int i=0;i<data.length;i++)
            {
                String link="https://firebasestorage.googleapis.com/v0/b/doitappfin.appspot.com/o/certdata%2F_dummy_.png?alt=media&token=9c82cfdc-80c4-4215-a87b-b7406601b53f";


                HashMap<String,String> hashMap=new HashMap<>();
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("MainData").child("CertificationData");
                hashMap.put("desc","description");

                lname[i].replace("+","%2B");
                lname[i].replace(" ","%20");
                link=link.replace("dummy",lname[i]);

                hashMap.put("image",link);
                hashMap.put("price",price[i]+" "+prefix[i]);

                hashMap.put("adddetail","notfixed");

                data[i]=data[i].replace("-","_m");
                data[i]=data[i].replace("+","_p");
                data[i]=data[i].replace(".","_d");

                hashMap.put("title",data[i]);

                databaseReference.child(data[i]).setValue(hashMap);

            }
        }




     /*   HashMap<String,String> hashMap1=new HashMap<>();
        DatabaseReference databaseRef =FirebaseDatabase.getInstance().getReference().child("MainData").child("CertificationList");


        for (int i=0;i<data.length;i++) {

            hashMap1.put("cert_id_"+i,data[i]);

        }


        databaseRef.setValue(hashMap1);

*/


        String[] m = {"CCNA", "AWS", "MICROSOFT", "Blue Prism", "ISQI", "Oracle",};

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Failure", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.d("Token", token);
                        //Toast.makeText(LoginPassword.this, token, Toast.LENGTH_SHORT).show();
                    }
                });



/*
        DatabaseReference databaseRef =FirebaseDatabase.getInstance().getReference().child("PopularData").child("Certification");

        for (int i=0;i<m.length;i++) {

            HashMap<String,String> hashMap1=new HashMap<>();

            hashMap1.put("title",m[i]);
            hashMap1.put("desc",m[i]);
            hashMap1.put("image","link");
            hashMap1.put("price","data");
            hashMap1.put("addetails","data");

            databaseRef.child(m[i]).setValue(hashMap1);
        }*/


        imv = findViewById(R.id.spl1);
        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);
        fromtop.setDuration(1500);
        imv.setAnimation(fromtop);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                if(connectedToNetwork()){
                    volley();
                }else{ NoInternetAlertDialog(); }
            }
        }, SPLASH_TIME_OUT);
    }


    @SuppressWarnings("MissingPermission")
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

        GoogleSignInAccount alr = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("com.doitAppfin.PRIVATEDATA", Context.MODE_PRIVATE);

        phone = sp.getString("number", "kjhgg");


        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {                                  //startActivity(new Intent(GoogleLoginActivity.this, MainWorkActivity.class));
            if (currentUser.getEmail() != null)
                mail = currentUser.getEmail().replace(".", "_");

            System.out.println(mail);
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("LoginData");
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

int t = 0;
                    for(DataSnapshot data: snapshot.getChildren()){
                        if(data.getValue().equals(mail)){
                        //    Toast.makeText(MainActivity.this, "User exixts", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,MainWorkActivity.class));
                            t = 1;
                        }
                    }
                    if(t==0) {
                        startActivity(new Intent(MainActivity.this,GoogleLoginActivity.class));
                    }






//                    System.out.println("in splash" + snapshot);
//                    if (!snapshot.hasChild(mail)) {
//
//                        //    FirebaseDatabase.getInstance().getReference().child("LoginData").child(mail).setValue("empty");
//
//                        Intent i = (new Intent(MainActivity.this, Registration.class));
//                        i.putExtra("mail", currentUser.getEmail());
//                        i.putExtra("number", "");
//
//                        if(connectedToNetwork()){
//                            startActivity(i);
//                        }
//                        else{ NoInternetAlertDialog(); }
//
//                    } else {
//                        Toast.makeText(MainActivity.this, "Yes", Toast.LENGTH_SHORT).show();
//
//                        Intent i = (new Intent(MainActivity.this, MainWorkActivity.class));
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                        if(connectedToNetwork()){
//                            startActivity(i);
//                        }
//                        else{ NoInternetAlertDialog(); }
//
//                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("LoginData");
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    System.out.println("in splash" + snapshot);
                    if (!snapshot.hasChild(mail) || (!snapshot.getValue().toString().contains(phone) && (phone.length() < 9))) {

                        //    FirebaseDatabase.getInstance().getReference().child("LoginData").child(mail).setValue("empty");

                        Intent i = (new Intent(MainActivity.this, GoogleLoginActivity.class));
                        if(connectedToNetwork()){
                            finish();
                            startActivity(i);
                        }
                        else{ NoInternetAlertDialog(); }
                    } else {

                        Intent i = (new Intent(MainActivity.this, MainWorkActivity.class));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        if(connectedToNetwork()){
                            startActivity(i);
                        }
                        else{ NoInternetAlertDialog(); }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }
}