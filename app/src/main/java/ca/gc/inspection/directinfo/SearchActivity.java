package ca.gc.inspection.directinfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import ca.gc.inspection.directinfo.DirectInfoDbContract.DirectInfo;

public class SearchActivity extends Activity {

    EditText input;
    Button searchBtn;
    TextView resultCount;

    DirectInfoDbHelper dbHelper;
    SQLiteDatabase db;

    RecyclerView recyclerView;
    DirectInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        input = findViewById(R.id.searchEditText);
        searchBtn = findViewById(R.id.searchBtn3);
        resultCount = findViewById(R.id.resultCountTv);


        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchDatabase(input.getText().toString());

            }
        });

        dbHelper = new DirectInfoDbHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();

        recyclerView = findViewById(R.id.searchResultsRecyclerView);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDatabase(input.getText().toString());

            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void searchDatabase(String textToSearch) {

        final String[] projection = {
                DirectInfo._ID,                             // 0
                DirectInfo.COLUMN_NAME_FIRST_NAME + " || ' ' || " + DirectInfo.COLUMN_NAME_LAST_NAME + " AS 'name'",          // 1
                DirectInfo.COLUMN_NAME_EMAIL,               // 2
                DirectInfo.COLUMN_NAME_TELEPHONE_NUMBER,    // 3
                DirectInfo.COLUMN_NAME_TITLE_EN,            // 4

        };

        // Filter resultCount WHERE "title" = 'My Title'

//        final String selection = "name LIKE ?";


        // search only entire GEDS database of employees by first name + last name, email, phone number, or title
        final String selection = "name LIKE '%" + textToSearch + "%' OR " +
                DirectInfo.COLUMN_NAME_EMAIL + " LIKE '%" + textToSearch + "%'  OR " +
                DirectInfo.COLUMN_NAME_TELEPHONE_NUMBER + " LIKE '%" + textToSearch + "%' OR " +
                DirectInfo.COLUMN_NAME_TITLE_EN + " LIKE '%" + textToSearch + "%' ";


//        String[] selectionArgs = { "%" + textToSearch + "%"};

        // How you want the resultCount sorted in the resulting Cursor
        final String sortOrder =
                DirectInfo.COLUMN_NAME_FIRST_NAME + " ASC LIMIT 500";


        Cursor cursor = db.query(
                DirectInfo.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
//                selectionArgs,          // The values for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        ArrayList<Person> people = new ArrayList<>();
        resultCount.setText("Your search returned " + cursor.getCount() + " results");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {

            Person person = new Person(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    "COMING SOON"
            );
            people.add(person);
            cursor.moveToNext();
        }

        cursor.close();

        adapter = new DirectInfoAdapter(people, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    } // end of searchDatabase()

} // end of class
