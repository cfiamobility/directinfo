package ca.gc.inspection.directinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    Context context;

    Button downloadBtn;
    Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        downloadBtn = findViewById(R.id.downloadBtn);
        searchBtn = findViewById(R.id.searchBtn);


        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, DownloadDatabase.class));
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SearchActivity.class));
            }
        });
    }
}
