package ca.gc.inspection.directinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

public class DetailedInfo extends AppCompatActivity {

    TextView tvName;
    TextView tvTitle;
    TextView tvDept;
    TextView tvOrg;
    TextView tvPhone;
    TextView tvEmail;
    TextView tvPhysicalAddress;

    ImageButton fabAddToContacts;
    ImageButton btnCallPrimary;

    ImageButton btnSendEmail;
    ImageButton btnMapPhysicalAddress;

    CardView cvPrimaryPhone;
    CardView cvEmail;
    CardView cvPhysicalAddress;


    Person person;
    Toolbar toolbar;

    SharedPreferences.Editor editor;

    /*
    UPDATED: February 6, 2019 by Nicole
    - added a back button to the contact view to go back to the search activity
     */

    ImageView backButton;


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

        tvDept = findViewById(R.id.tvDept);
        tvDept.setText(person.getDept());

        tvOrg = findViewById(R.id.tvOrg);
        tvOrg.setText(person.getOrg());

        tvPhone = findViewById(R.id.tvPrimaryPhone);
        tvPhone.setText(person.getPhone());

        tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setText(person.getEmail());


        tvPhysicalAddress = findViewById(R.id.tvPhysicalAddress);
        tvPhysicalAddress.setText(person.getAddress());

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

        btnCallPrimary = findViewById(R.id.btnCallPrimary);
        btnCallPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String primary = person.getPhone();
                startDial(primary);
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

        /*
            UPDATED: February 6, 2019 by Nicole
            - added a back button to the contact view to go back to the search activity
        */
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(back);
            }
        });

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean first = prefs.getBoolean("detailInfo",true);
        if(first){
            showDialog();
        }

    }
    private void showDialog() {
        startTutorial();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        editor = prefs.edit();
        editor.putBoolean("detailInfo",false);
        editor.apply();
    }

    public void startDial(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + (phoneNumber.length() > 14
                                    ? phoneNumber.substring(0,14) + "," + phoneNumber.substring(20)
                                    : phoneNumber.substring(0,14)))); // format's the extension properly
        startActivity(intent);
    }

   /* public void startText(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + (phoneNumber.length() > 14
                                    ? phoneNumber.substring(0,14) + "," + phoneNumber.substring(20)
                                    : phoneNumber.substring(0,14))));
        startActivity(intent);
    }*/

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

    /* Tutorial starts */
    private void startTutorial() {

        TapTargetSequence intro = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(tvName, getString(R.string.contactname))
                                .outerCircleColor(R.color.md_blue_800)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.50f)            // Specify the alpha amount for the outer circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.md_white_1000)// Specify the color of the title text
                                .descriptionTextSize(48)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.md_white_1000)  // Specify the color of the description text
                                .textColor(R.color.md_white_1000)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .drawShadow(false)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(35)
                                .id(1),
                        TapTarget.forView(tvTitle, getString(R.string.titleposition))
                                .outerCircleColor(R.color.md_blue_800)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.50f)            // Specify the alpha amount for the outer circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.md_white_1000)      // Specify the color of the title text
                                .descriptionTextSize(48)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.md_white_1000)  // Specify the color of the description text
                                .textColor(R.color.md_white_1000)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(35)
                                .id(2),
                        TapTarget.forView(fabAddToContacts, getString(R.string.addtocontact))
                                .outerCircleColor(R.color.md_blue_800)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.75f)            // Specify the alpha amount for the outer circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.md_white_1000)      // Specify the color of the title text
                                .descriptionTextSize(48)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.md_white_1000)  // Specify the color of the description text
                                .textColor(R.color.md_white_1000)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(35)
                                .id(3),
                        TapTarget.forView(btnCallPrimary, getString(R.string.phonenumber), "Clicking here will automatically dial the number on your phone.")
                                .outerCircleColor(R.color.md_blue_800)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.75f)            // Specify the alpha amount for the outer circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.md_white_1000)      // Specify the color of the title text
                                .descriptionTextSize(16)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.md_white_1000)  // Specify the color of the description text
                                .textColor(R.color.md_white_1000)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(35)
                                .id(4),
                        TapTarget.forView(btnSendEmail, getString(R.string.emailaddress), "Clicking here will open your default email application to quickly send an email to the person.")
                                .outerCircleColor(R.color.md_blue_800)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.75f)            // Specify the alpha amount for the outer circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.md_white_1000)      // Specify the color of the title text
                                .descriptionTextSize(16)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.md_white_1000)  // Specify the color of the description text
                                .textColor(R.color.md_white_1000)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(35)
                                .id(5),
                        TapTarget.forView(btnMapPhysicalAddress, getString(R.string.physicaladdress), "Need directions? Clicking here will open up the Map on your phone!")
                                .outerCircleColor(R.color.md_blue_800)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.75f)            // Specify the alpha amount for the outer circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.md_white_1000)      // Specify the color of the title text
                                .descriptionTextSize(16)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.md_white_1000)  // Specify the color of the description text
                                .textColor(R.color.md_white_1000)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(35)
                                .id(6)
                        /*TapTarget.forView(btnMapPostalAddress, getString(R.string.postaladdress))
                                .outerCircleColor(R.color.md_blue_800)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.75f)            // Specify the alpha amount for the outer circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.md_white_1000)      // Specify the color of the title text
                                .descriptionTextSize(16)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.md_white_1000)  // Specify the color of the description text
                                .textColor(R.color.md_white_1000)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(35)*/
                )
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        // Yay
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        if (lastTarget.id() == 1) {
                            tvName.performClick();
                        } else if (lastTarget.id() == 2) {
                            tvTitle.performClick();
                        }else if (lastTarget.id() == 4) {
                            tvPhone.performClick();
                        }else if (lastTarget.id() == 5) {
                            tvEmail.performClick();
                        }else if (lastTarget.id() == 6) {
                            tvPhysicalAddress.performClick();
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
