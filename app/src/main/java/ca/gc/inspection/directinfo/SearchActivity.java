package ca.gc.inspection.directinfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;


import java.util.ArrayList;

import ca.gc.inspection.directinfo.DirectInfoDbContract.DirectInfo;

public class SearchActivity extends AppCompatActivity implements RecyclerItemClickListener.OnRecyclerClickListener {

    private static final String TAG = "SearchActivity";

    TextView resultCount;

    DirectInfoDbHelper dbHelper;
    SQLiteDatabase db;

    RecyclerView recyclerView;
    DirectInfoAdapter adapter;
    ArrayList<Person> people;
    SearchView searchView;
    Toolbar toolbar;
    String result_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initToolbar();


        resultCount = findViewById(R.id.resultCountTv);

        dbHelper = new DirectInfoDbHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();

        recyclerView = findViewById(R.id.searchResultsRecyclerView);

        people = new ArrayList<>();

        adapter = new DirectInfoAdapter(people, getApplicationContext());
        if (savedInstanceState != null) {
            result_query = savedInstanceState.getString("userInput");
            searchDatabase(result_query);
        }


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

    }

    @SuppressLint("SetTextI18n")
    public void searchDatabase(String textToSearch) {

        final String[] projection = {
                DirectInfo._ID,                                     // 0
                DirectInfo.COLUMN_NAME_FIRST_NAME + " || ' ' || " + DirectInfo.COLUMN_NAME_LAST_NAME + " AS 'name'",          // 1
                DirectInfo.COLUMN_NAME_EMAIL,                       // 2
                DirectInfo.COLUMN_NAME_TELEPHONE_NUMBER,            // 3
                DirectInfo.COLUMN_NAME_TITLE_EN,                    // 4
                DirectInfo.COLUMN_NAME_MOBILE_NUMBER,               // 5
                DirectInfo.COLUMN_NAME_POSTAL_STREET_NUMBER,        // 6
                DirectInfo.COLUMN_NAME_POSTAL_STREET_NAME,          // 7
                DirectInfo.COLUMN_NAME_POSTAL_BUILDING_UNIT_TYPE,   // 8
                DirectInfo.COLUMN_NAME_POSTAL_BUILDING_UNIT_ID,     // 9
                DirectInfo.COLUMN_NAME_PO_BOX_EN,                   // 10
                DirectInfo.COLUMN_NAME_POSTAL_CITY_EN,              // 11
                DirectInfo.COLUMN_NAME_POSTAL_PROVINCE_EN,          // 12
                DirectInfo.COLUMN_NAME_POSTAL_CODE,                 // 13
                DirectInfo.COLUMN_NAME_BUILDING_NAME_EN,            // 14
                DirectInfo.COLUMN_NAME_FLOOR,                       // 15
                DirectInfo.COLUMN_NAME_ROOM,                        // 16
                DirectInfo.COLUMN_NAME_PHYSICAL_STREET_NUMBER,      // 17
                DirectInfo.COLUMN_NAME_PHYSICAL_STREET_NAME,        // 18
                DirectInfo.COLUMN_NAME_PHYSICAL_BUILDING_UNIT_TYPE, // 19
                DirectInfo.COLUMN_NAME_PHYSICAL_BUILDING_UNIT_ID,   // 20
                DirectInfo.COLUMN_NAME_PHYSICAL_CITY_EN,            // 21
                DirectInfo.COLUMN_NAME_PHYSICAL_PROVINCE_EN,        // 22


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

        people.clear();
        resultCount.setText("Your search returned " + cursor.getCount() + " results");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Person person = new Person(
                    cursor.getString(1),    // name
                    cursor.getString(2),    // email
                    cursor.getString(3),    // phone
                    cursor.getString(4),    // title
                    cursor.getString(5),    // mobile
                    cursor.getString(6),    // postal street number
                    cursor.getString(7),    // postal street name
                    cursor.getString(8),    // postal building unit type
                    cursor.getString(9),    // postal building unit id
                    cursor.getString(10),   // po box
                    cursor.getString(11),   // postal city
                    cursor.getString(12),   // postal province
                    cursor.getString(13),   // postal code
                    cursor.getString(14),   // building name
                    cursor.getString(15),   // floor
                    cursor.getString(16),   // room
                    cursor.getString(17),   // physical street number
                    cursor.getString(18),   // physical street name
                    cursor.getString(19),   // physical building unit type
                    cursor.getString(20),   // physical building unit id
                    cursor.getString(21),   // physical city
                    cursor.getString(22)    // physical province
            );
            people.add(person);
            cursor.moveToNext();
        }

        adapter.notifyDataSetChanged();
        cursor.close();

    } // end of searchDatabase()

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
        Toast.makeText(SearchActivity.this, "Name: " + adapter.getPerson(position).getName(), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, PersonDetails.class);
        intent.putExtra("PERSON", adapter.getPerson(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");
        onItemClick(view, position);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.searchView);
        searchView = (SearchView) menuItem.getActionView();
        searchView.onActionViewExpanded();
        if (result_query != null) {
            searchView.setQuery(result_query, false);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                result_query = newText;
                searchDatabase(result_query);

                return true;
            }
        });
        return true;
    }

    public void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("userInput", result_query);
        super.onSaveInstanceState(outState);
    }


} // end of class
