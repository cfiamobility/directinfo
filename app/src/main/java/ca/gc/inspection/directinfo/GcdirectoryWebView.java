package ca.gc.inspection.directinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class GcdirectoryWebView extends AppCompatActivity {

    WebView gc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcdirectory_web_view);
        gc =(WebView)findViewById(R.id.gcWebview);
        gc.loadUrl("http://gcdirectory-gcannuaire.ssc-spc.gc.ca/en/GCD/?pgid=009");

    }
}
