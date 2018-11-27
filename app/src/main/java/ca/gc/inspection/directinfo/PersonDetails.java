package ca.gc.inspection.directinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PersonDetails extends Activity {

    TextView personName;
    TextView personPhone;
    TextView personEmail;
    TextView personPosition;
    TextView personAddressPhysical;

    TextView gcDirectory;
    TextView  cellphone;
    Button savecontacts;
    TextView postalAddress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        Intent intent = getIntent();
        Person person = (Person) intent.getSerializableExtra("PERSON");

        personName = findViewById(R.id.personDetailsNameTV);
        personPhone = findViewById(R.id.personDetailsPhoneTV);
        cellphone = findViewById(R.id.cellPhoneNumberTextView);

        personEmail = findViewById(R.id.personDetailsEmailTV);
        personPosition = findViewById(R.id.personDetailsPositionTV);
        personAddressPhysical = findViewById(R.id.physicalAddresstextview);
        postalAddress = findViewById(R.id.postalAddresstextview2);



        // click on gcDirectory textView to go to GCdirectory website
        gcDirectory = findViewById(R.id.gcDirectoryTextview);
        gcDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),GcdirectoryWebView.class);
                startActivity(intent);
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
}
