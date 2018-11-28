package ca.gc.inspection.directinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class PersonDetails extends AppCompatActivity {

    TextView personName;
    TextView personPhone;
    TextView personEmail;
    TextView personPosition;
    TextView personAddressPhysical;

    TextView gcDirectory;
    TextView  cellphone;
    Button   savecontacts;
    TextView postalAddress;
    TextView toolbartitle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        Person person = (Person) intent.getSerializableExtra("PERSON");


        getSupportActionBar().setTitle("");
        toolbartitle =(TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbartitle.setText(person.getName() + " - CFIA/ACIA");
        personName = findViewById(R.id.personDetailsNameTV);
        personPhone = findViewById(R.id.personDetailsPhoneTV);
        cellphone = findViewById(R.id.cellPhoneNumberTextView);

        personEmail = findViewById(R.id.personDetailsEmailTV);
        personPosition = findViewById(R.id.personDetailsPositionTV);
        personAddressPhysical = findViewById(R.id.physicalAddresstextview);
        postalAddress = findViewById(R.id.postalAddresstextview2);


        gcDirectory = findViewById(R.id.gcDirectoryTextview);

        gcDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        String url = "http://gcdirectory-gcannuaire.ssc-spc.gc.ca/en/GCD/?pgid=009";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
            }
        });


        savecontacts = (Button)findViewById(R.id.saveContactsbutton);
        savecontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.PHONE,personPhone.getText()).putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, personName.getText());
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, personEmail.getText())
                        .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
                intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE,cellphone.getText() )
                        .putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, "CFIA");
                intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE,personPosition.getText() );
                startActivity(intent);
            }
        });


        if (person != null) {
            personName.setText(person.getName());
            personPhone.setText(person.getPhone());
            personEmail.setText(person.getEmail());
            personPosition.setText(person.getTitle());
            personAddressPhysical.setText(person.getPhysicalAddress());
            cellphone.setText(person.getMobile());
            postalAddress.setText(person.getPostalAddress());

        }
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
        switch(item.getItemId()){
            case R.id.abouticon:
                Intent intent = new Intent(this,DirectInfoAbout.class);
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
            case R.id.exit:
                  finish();
                  break;
            default:
                return true;
        }
        return true;
    }

}
