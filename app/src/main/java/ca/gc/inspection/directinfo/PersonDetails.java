package ca.gc.inspection.directinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PersonDetails extends Activity {

    TextView personName;
    TextView personPhone;
    TextView personEmail;
    TextView personPosition;
    TextView personAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        Intent intent = getIntent();
        Person person = (Person) intent.getSerializableExtra("PERSON");

        personName = findViewById(R.id.personDetailsNameTV);
        personPhone = findViewById(R.id.personDetailsPhoneTV);
        personEmail = findViewById(R.id.personDetailsEmailTV);
        personPosition = findViewById(R.id.personDetailsPositionTV);
//        personAddress = findViewById(R.id.personDetailsAddressTV);

        if (person != null) {
            personName.setText(person.getName());
            personPhone.setText(person.getPhone());
            personEmail.setText(person.getEmail());
            personPosition.setText(person.getTitle());
//            personAddress.setText(person.getAddress());
        }
    }
}
