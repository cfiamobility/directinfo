package ca.gc.inspection.directinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

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

    SharedPreferences.Editor editor;


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

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean first = prefs.getBoolean("detailInfo",true);
        if(first){
            showdialog();
        }

    }
    private void showdialog() {
        startTutorial();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        editor = prefs.edit();
        editor.putBoolean("detailInfo",false);
        editor.apply();
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

    /* Tutorial starts */
    private void startTutorial() {

        TapTargetSequence intro = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(tvName, getString(R.string.contactname))
                                .outerCircleColor(R.color.md_blue_800)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.50f)            // Specify the alpha amount for the outer circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.md_white_1000)      // Specify the color of the title text
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
                        TapTarget.forView(btnCallPrimary, getString(R.string.phonenumber))
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
                        TapTarget.forView(btnSendEmail, getString(R.string.emailaddress))
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
                        TapTarget.forView(btnMapPhysicalAddress, getString(R.string.physicaladdress))
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
                                .id(6),
                        TapTarget.forView(btnMapPostalAddress, getString(R.string.postaladdress))
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
                        }else if (lastTarget.id() == 3) {
//                            fabAddToContacts.performClick();
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
