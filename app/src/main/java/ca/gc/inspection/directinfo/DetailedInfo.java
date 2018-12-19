package ca.gc.inspection.directinfo;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DetailedInfo extends AppCompatActivity {

    TextView tvName;
    TextView tvTitle;
    TextView tvPhone;
    TextView tvMobile;
    TextView tvEmail;
    TextView tvPhysicalAddress;
    TextView tvPostalAddress;

    TextView tvDisplayMobile;

//    FloatingActionButton fabAddToContacts;
    ImageButton fabAddToContacts;
    ImageButton btnCallPrimary;
    ImageButton btnCallMobile;
    ImageButton btnTextMobile;
    ImageButton btnSendEmail;
    ImageButton btnMapPhysicalAddress;
    ImageButton btnMapPostalAddress;

    CardView cvPrimaryPhone;
    CardView cvMobilePhone;
    CardView cvEmail;
    CardView cvPhysicalAddress;
    CardView cvPostalAddress;

    Person person;

    Toolbar toolbar;

    private static final String TAG = "DetailedInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_info);

        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
//        toolbar.setTitle("");

        Intent intent = getIntent();
        person = (Person) intent.getSerializableExtra("PERSON");

        tvName = findViewById(R.id.tvName);
        tvName.setText(person.getName());

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(person.getTitle());

        tvPhone = findViewById(R.id.tvPrimaryPhone);
        tvPhone.setText(person.getPhone());

        tvMobile = findViewById(R.id.tvMobilePhone);
        tvMobile.setText(person.getMobile());

        tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setText(person.getEmail());


        tvPhysicalAddress = findViewById(R.id.tvPhysicalAddress);
        tvPhysicalAddress.setText(person.getPhysicalAddress());

        tvPostalAddress = findViewById(R.id.tvPostalAddress);
        tvPostalAddress.setText(person.getPostalAddress());

        fabAddToContacts = findViewById(R.id.addToContactsFAB);
        fabAddToContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(person.addToContacts());
            }
        });

        cvPrimaryPhone = findViewById(R.id.cvPrimaryPhone);
        cvPrimaryPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDial(person.getPhone());
            }
        });

        cvMobilePhone = findViewById(R.id.cvMobilePhone);
        cvMobilePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDial(person.getMobile());
            }
        });

        cvEmail = findViewById(R.id.cvEmail);
        cvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEmail(person.getEmail());
            }
        });

        cvPhysicalAddress = findViewById(R.id.cvPhysicalAddress);
        cvPhysicalAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMaps(person.getPhysicalMapInfo());
            }
        });

        cvPostalAddress = findViewById(R.id.cvPostalAddress);
        cvPostalAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMaps(person.getPostalMapInfo());
            }
        });


        btnTextMobile = findViewById(R.id.btnTextMobile);
        btnTextMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startText(person.getMobile());
            }
        });

        btnCallPrimary = findViewById(R.id.btnCallPrimary);
        btnCallPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String primary = person.getPhone();
                startDial(primary);
            }
        });

        btnCallMobile = findViewById(R.id.btnCallMobile);
        btnCallMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDial(person.getMobile());
            }
        });

        btnSendEmail = findViewById(R.id.btnSendEmail);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEmail(person.getEmail());
            }
        });

        btnMapPhysicalAddress = findViewById(R.id.btnMapPhysicalAddress);
        btnMapPhysicalAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMaps(person.getPhysicalMapInfo());
            }
        });

        btnMapPostalAddress = findViewById(R.id.btnMapPostalAddress);
        btnMapPostalAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMaps(person.getPostalMapInfo());
            }
        });

        if (person.getMobile() == null) {
//            tvMobile.setVisibility(View.INVISIBLE);
//            tvDisplayMobile = findViewById(R.id.tvMobilePhoneDisplay);
//            tvDisplayMobile.setVisibility(View.INVISIBLE);
//            btnTextMobile.setVisibility(View.INVISIBLE);
//            btnCallMobile.setVisibility(View.INVISIBLE);
            cvMobilePhone.setVisibility(View.GONE);
        }

    }

    public void startDial(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    public void startText(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));
        startActivity(intent);
    }

    public void startEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        startActivity(intent);
    }

    public void startMaps(String address) {

        Uri uri =Uri.parse("https://www.google.com/maps/search/?api=1&query="+address) ;
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
        Log.d(TAG, "startMaps: ");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        menu.findItem(R.id.searchView).setVisible(false);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.abouticon:
                Intent intent = new Intent(this, DirectInfoAbout.class);
                startActivity(intent);
                break;

//                 go to Gcdirectory website
            case R.id.gcdirectory:
                String url = "http://gcdirectory-gcannuaire.ssc-spc.gc.ca/en/GCD/?pgid=009";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

            // exit application

            default:
                return true;
        }
        return true;
    }

} // end of class
