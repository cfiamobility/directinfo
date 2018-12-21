package ca.gc.inspection.directinfo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;


import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

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
    MaterialSearchView searchView;
    Toolbar toolbar;
    String result_query = "";

    ItemTouchHelper itemTouchHelper;
    SwipeGestureController swipeGestureController;

    CardView cvSearchOptions;
    CheckBox cbFirstName;
    CheckBox cbLastName;
    CheckBox cbEmail;
    CheckBox cbPhone;
    CheckBox cbAddress;
    CheckBox cbTitle;

    String searchBy = "default";

    LinearLayout alphabet;

    SharedPreferences.Editor editor;

    View search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initToolbar();

        cvSearchOptions = findViewById(R.id.cvSearchOptions);
        cvSearchOptions.setVisibility(View.GONE);

        setCheckBoxFunctionality();

        resultCount = findViewById(R.id.resultCountTv);

        dbHelper = new DirectInfoDbHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();

        recyclerView = findViewById(R.id.searchResultsRecyclerView);


        swipeGestureController = new SwipeGestureController();
        itemTouchHelper = new ItemTouchHelper(swipeGestureController);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        people = new ArrayList<>();

        adapter = new DirectInfoAdapter(people, getApplicationContext());
        if (savedInstanceState != null) {
            result_query = savedInstanceState.getString("userInput");
            searchDatabase(result_query);
        } else {
            searchDatabase("");
            resultCount.setText("");
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));
        
        // set up the alphabet sidebar
        alphabet = findViewById(R.id.llAlphabet);

        for (int i = 0; i < alphabet.getChildCount(); i++) {

            TextView textView = (TextView) alphabet.getChildAt(i);
            final String search = textView.getText().toString();

            alphabet.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String temp = searchBy;
                    searchBy = "first name";
                    searchDatabase(search);
                    searchBy = temp;
                }
            });
        }


        search = findViewById(R.id.search_view);
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean first = prefs.getBoolean("firststart",true);
        if(first){
            showdialog();
        }

    }
    private void showdialog() {
        startTutorial();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        editor = prefs.edit();
        editor.putBoolean("firststart",false);
        editor.apply();

    }

    public void setCheckBoxFunctionality() {
        cbFirstName = findViewById(R.id.cbFirstName);
        cbLastName = findViewById(R.id.cbLastName);
        cbEmail = findViewById(R.id.cbEmail);
        cbPhone = findViewById(R.id.cbPhone);
        cbAddress = findViewById(R.id.cbAddress);
        cbTitle = findViewById(R.id.cbTitle);

        cbFirstName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cbFirstName.isChecked()) {
                    cbEmail.setChecked(false);
                    cbPhone.setChecked(false);
                    cbAddress.setChecked(false);
                    cbTitle.setChecked(false);

                    if (cbLastName.isChecked()) {
                        searchBy = "first name, last name";
                    } else {
                        searchBy = "first name";
                    }

                    Log.d(TAG, "setCheckBoxFunctionality: Search by: " + searchBy);
                }

                if (!cbFirstName.isChecked() && cbLastName.isChecked()) {
                    searchBy = "last name";
                    Log.d(TAG, "setCheckBoxFunctionality: Search by: " + searchBy);
                } else if (!cbFirstName.isChecked() && !cbLastName.isChecked()) {
                    searchBy = "default";
                    Log.d(TAG, "setCheckBoxFunctionality: Search by: " + searchBy);
                }
            }
        });

        cbLastName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbLastName.isChecked()) {
                    cbEmail.setChecked(false);
                    cbPhone.setChecked(false);
                    cbAddress.setChecked(false);
                    cbTitle.setChecked(false);

                    if (cbFirstName.isChecked()) {
                        searchBy = "first name, last name";
                    } else {
                        searchBy = "last name";
                    }

                    Log.d(TAG, "setCheckBoxFunctionality: Search by: " + searchBy);
                }

                if (!cbLastName.isChecked() && cbFirstName.isChecked()) {
                    searchBy = "first name";
                    Log.d(TAG, "setCheckBoxFunctionality: Search by: " + searchBy);
                } else if (!cbFirstName.isChecked() && !cbLastName.isChecked()) {
                    searchBy = "default";
                    Log.d(TAG, "setCheckBoxFunctionality: Search by: " + searchBy);
                }
            }
        });

        cbEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cbEmail.isChecked()) {
                    cbFirstName.setChecked(false);
                    cbLastName.setChecked(false);
                    cbPhone.setChecked(false);
                    cbAddress.setChecked(false);
                    cbTitle.setChecked(false);

                    searchBy = "email";

                    Log.d(TAG, "setCheckBoxFunctionality: Search by: " + searchBy);
                }

                if (!cbEmail.isChecked()) {
                    searchBy = "default";
                    Log.d(TAG, "setCheckBoxFunctionality: Search by: " + searchBy);
                }
            }
        });

        cbPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cbPhone.isChecked()) {
                    cbFirstName.setChecked(false);
                    cbLastName.setChecked(false);
                    cbEmail.setChecked(false);
                    cbAddress.setChecked(false);
                    cbTitle.setChecked(false);

                    searchBy = "phone";

                    Log.d(TAG, "setCheckBoxFunctionality: Search by: " + searchBy);
                }
                if (!cbPhone.isChecked()) {
                    searchBy = "default";
                    Log.d(TAG, "setCheckBoxFunctionality: Search by: " + searchBy);
                }
            }
        });

        cbAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cbAddress.isChecked()) {
                    cbFirstName.setChecked(false);
                    cbLastName.setChecked(false);
                    cbPhone.setChecked(false);
                    cbEmail.setChecked(false);
                    cbTitle.setChecked(false);

                    searchBy = "address";


                    Log.d(TAG, "setCheckBoxFunctionality: Search by: " + searchBy);
                }

                if (!cbAddress.isChecked()) {
                    searchBy = "default";
                    Log.d(TAG, "setCheckBoxFunctionality: Search by: " + searchBy);
                }
            }
        });



        cbTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (cbTitle.isChecked()) {
                    cbFirstName.setChecked(false);
                    cbLastName.setChecked(false);
                    cbPhone.setChecked(false);
                    cbAddress.setChecked(false);
                    cbEmail.setChecked(false);

                    searchBy = "title";

                    Log.d(TAG, "setCheckBoxFunctionality: Search by: " + searchBy);
                }

                if (!cbTitle.isChecked()) {
                    searchBy = "default";
                    Log.d(TAG, "setCheckBoxFunctionality: Search by: " + searchBy);
                }
            }
        });


    }

    @SuppressLint("SetTextI18n")
    public void searchDatabase(String textToSearch) {

        final String[] projection = {
                DirectInfo._ID,                                     // 0
                DirectInfo.COLUMN_NAME_FIRST_NAME,                  // 1
                DirectInfo.COLUMN_NAME_LAST_NAME,                   // 2
                DirectInfo.COLUMN_NAME_EMAIL,                       // 3
                DirectInfo.COLUMN_NAME_TELEPHONE_NUMBER,            // 4
                DirectInfo.COLUMN_NAME_TITLE_EN,                    // 5
                DirectInfo.COLUMN_NAME_MOBILE_NUMBER,               // 6
                DirectInfo.COLUMN_NAME_POSTAL_STREET_NUMBER,        // 7
                DirectInfo.COLUMN_NAME_POSTAL_STREET_NAME,          // 8
                DirectInfo.COLUMN_NAME_POSTAL_BUILDING_UNIT_TYPE,   // 9
                DirectInfo.COLUMN_NAME_POSTAL_BUILDING_UNIT_ID,     // 10
                DirectInfo.COLUMN_NAME_PO_BOX_EN,                   // 11
                DirectInfo.COLUMN_NAME_POSTAL_CITY_EN,              // 12
                DirectInfo.COLUMN_NAME_POSTAL_PROVINCE_EN,          // 13
                DirectInfo.COLUMN_NAME_POSTAL_CODE,                 // 14
                DirectInfo.COLUMN_NAME_BUILDING_NAME_EN,            // 15
                DirectInfo.COLUMN_NAME_FLOOR,                       // 16
                DirectInfo.COLUMN_NAME_ROOM,                        // 17
                DirectInfo.COLUMN_NAME_PHYSICAL_STREET_NUMBER,      // 18
                DirectInfo.COLUMN_NAME_PHYSICAL_STREET_NAME,        // 19
                DirectInfo.COLUMN_NAME_PHYSICAL_BUILDING_UNIT_TYPE, // 20
                DirectInfo.COLUMN_NAME_PHYSICAL_BUILDING_UNIT_ID,   // 21
                DirectInfo.COLUMN_NAME_PHYSICAL_CITY_EN,            // 22
                DirectInfo.COLUMN_NAME_PHYSICAL_PROVINCE_EN,        // 23


        };

//        final String selection = "name LIKE ?";


        final String selection = getSelectionString(searchBy, textToSearch);

        // How you want the resultCount sorted in the resulting Cursor
        final String sortOrder =
                DirectInfo.COLUMN_NAME_FIRST_NAME + " ASC";

        Cursor cursor = db.query(
                DirectInfo.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
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
                    cursor.getString(1) + " " + cursor.getString(2),   // name
                    cursor.getString(3),                                        // email
                    cursor.getString(4),                                        // phone
                    cursor.getString(5),                                        // title
                    cursor.getString(6),                                        // mobile
                    cursor.getString(7),                                        // postal street number
                    cursor.getString(8),                                        // postal street name
                    cursor.getString(9),                                        // postal building unit type
                    cursor.getString(10),                                        // postal building unit id
                    cursor.getString(11),                                       // po box
                    cursor.getString(12),                                       // postal city
                    cursor.getString(13),                                       // postal province
                    cursor.getString(14),                                       // postal code
                    cursor.getString(15),                                       // building name
                    cursor.getString(16),                                       // floor
                    cursor.getString(17),                                       // room
                    cursor.getString(18),                                       // physical street number
                    cursor.getString(19),                                       // physical street name
                    cursor.getString(20),                                       // physical building unit type
                    cursor.getString(21),                                       // physical building unit id
                    cursor.getString(22),                                       // physical city
                    cursor.getString(23)                                        // physical province
            );
            people.add(person);
            cursor.moveToNext();
        }

        adapter.notifyDataSetChanged();
        cursor.close();

    } // end of searchDatabase()

    public String getSelectionString(String searchBy, String textToSearch) {
        // search only entire GEDS database of employees by first name + last name, email, phone number, or title
        String selection;


        switch (searchBy) {
            case "default":
                // search the DirectInfo database of employees by first name + last name, email, phone number, or title
                selection =
                        DirectInfo.COLUMN_NAME_FIRST_NAME + " || ' ' || " + DirectInfo.COLUMN_NAME_LAST_NAME + " LIKE '" + textToSearch + "%' OR " +
                                DirectInfo.COLUMN_NAME_EMAIL + " LIKE '%" + textToSearch + "%'  OR " +
                                DirectInfo.COLUMN_NAME_TELEPHONE_NUMBER + " LIKE '%" + textToSearch + "%' OR " +
                                DirectInfo.COLUMN_NAME_TITLE_EN + " LIKE '%" + textToSearch + "%' ";
                break;
            case "first name":
                // search the DirectInfo database of employees by first name
                selection = DirectInfo.COLUMN_NAME_FIRST_NAME + " LIKE '" + textToSearch + "%'";
                break;
            case "last name":
                // search the DirectInfo database of employees by last name
                selection = DirectInfo.COLUMN_NAME_LAST_NAME + " LIKE '" + textToSearch + "%'";
                break;
            case "first name, last name":
                // search the DirectInfo database of employees by first name + last name
                selection = DirectInfo.COLUMN_NAME_FIRST_NAME + " || ' ' || " + DirectInfo.COLUMN_NAME_LAST_NAME + " LIKE '" + textToSearch + "%'";
                break;
            case "email":
                // search the DirectInfo database of employees by email
                selection = DirectInfo.COLUMN_NAME_EMAIL + " LIKE '" + textToSearch + "%'";
                break;
            case "phone":
                // search the DirectInfo database of employees by phone
                selection = DirectInfo.COLUMN_NAME_TELEPHONE_NUMBER + " LIKE '%" + textToSearch + "%' OR " +
                        DirectInfo.COLUMN_NAME_MOBILE_NUMBER + " LIKE '%" + textToSearch + "%'";
                break;
            case "address":
                // search the DirectInfo database of employees by physical address
                selection = DirectInfo.COLUMN_NAME_PHYSICAL_STREET_NUMBER + " || ' ' || " +
                        DirectInfo.COLUMN_NAME_PHYSICAL_STREET_NAME + " || ' ' || " +
                        DirectInfo.COLUMN_NAME_PHYSICAL_CITY_EN + " || ' ' || " +
                        DirectInfo.COLUMN_NAME_PHYSICAL_PROVINCE_EN + " LIKE '%" + textToSearch + "%'";
                break;
            case "title":
                // search the DirectInfo database of employees by title
                selection = DirectInfo.COLUMN_NAME_TITLE_EN + " LIKE '%" + textToSearch + "%'";
                break;
            default:
                // search the DirectInfo database of employees by first name + last name, email, phone number, or title
                selection =
                        DirectInfo.COLUMN_NAME_FIRST_NAME + " || ' ' || " + DirectInfo.COLUMN_NAME_LAST_NAME + " LIKE '" + textToSearch + "%' OR " +
                                DirectInfo.COLUMN_NAME_EMAIL + " LIKE '%" + textToSearch + "%'  OR " +
                                DirectInfo.COLUMN_NAME_TELEPHONE_NUMBER + " LIKE '%" + textToSearch + "%' OR " +
                                DirectInfo.COLUMN_NAME_TITLE_EN + " LIKE '%" + textToSearch + "%' ";
                break;
        }

        return selection;
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");

        Intent intent = new Intent(this, DetailedInfo.class);
        intent.putExtra("PERSON", adapter.getPerson(position));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.searchView);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setMenuItem(menuItem);
        searchView.showSearch(true);


        searchView.setHint(getString(R.string.searchHint));


        if (result_query != null) {
            searchView.setQuery(result_query, false);
        }
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {

                result_query = newText;
                searchDatabase(result_query);
                recyclerView.setAdapter(adapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                result_query = newText;
                searchDatabase(result_query);
                recyclerView.setAdapter(adapter);

                return true;
            }
        });


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.abouticon:
                Intent intent = new Intent(this, DirectInfoAbout.class);
                startActivity(intent);
                break;

            case R.id.gcdirectory:
                String url = "http://gcdirectory-gcannuaire.ssc-spc.gc.ca/en/GCD/?pgid=009";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

            case R.id.miToggleSearchOptions:
                if (cvSearchOptions.getVisibility() == View.VISIBLE)
                    cvSearchOptions.setVisibility(View.GONE);
                else if (cvSearchOptions.getVisibility() == View.GONE)
                    cvSearchOptions.setVisibility(View.VISIBLE);
                break;


//            case R.id.restBtn:
//                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
//                builder.setTitle(R.string.DialogTitle);
//
//                builder.setNegativeButton(R.string.DialogNegativeBtn, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                builder.setPositiveButton(R.string.DialogPositiveBtn, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        startActivity(new Intent(SearchActivity.this, DownloadDatabase.class));
//                        finish();
//                    }
//                });
//                builder.show();
//                break;

            default:
                return true;
        }
        return true;
    }

    public void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //disable toolbar name


        getSupportActionBar().setTitle(R.string.searchToolbar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("userInput", result_query);
        super.onSaveInstanceState(outState);
    }

    private void startTutorial() {

        TapTargetSequence intro = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(search, getString(R.string.searchby))
                                .outerCircleColor(R.color.md_blue_800)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.75f)            // Specify the alpha amount for the outer circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.md_white_1000)      // Specify the color of the title text
                                .descriptionTextSize(16)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.md_white_1000)  // Specify the color of the description text
                                .textColor(R.color.md_white_1000)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(100)
                                .id(1),
                        TapTarget.forView(alphabet, getString(R.string.firstletter))
                                .outerCircleColor(R.color.md_blue_800)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.75f)            // Specify the alpha amount for the outer circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.md_white_1000)      // Specify the color of the title text
                                .descriptionTextSize(16)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.md_white_1000)  // Specify the color of the description text
                                .textColor(R.color.md_white_1000)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(100)
                                .id(2))


                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        // Yay
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        if (lastTarget.id() == 1) {
                            searchView.performClick();
                        } else if (lastTarget.id() == 2) {
                            alphabet.performClick();
                        }
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                });
        intro.start();
    } // end of startTutorial()

} // end of class