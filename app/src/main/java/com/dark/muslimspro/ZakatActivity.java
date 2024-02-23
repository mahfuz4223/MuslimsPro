package com.dark.muslimspro;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ZakatActivity extends AppCompatActivity {


    TextView sub1,sub2,sub3,sub4,sub5,sub6,sub7,a1,a2,a3,a4,b1,b2,b3,b4,c1,d1,e1,f1,g1,g2,g3,g4,g5,g6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zakat);



        // Initialize views
        initViews();

        // Load JSON data
       loadDataFromJSON();

        // ... [initialize other TextViews here]

        setupExpandableCard(R.id.img1, R.id.expandableView1);
        setupExpandableCard(R.id.img2, R.id.expandableView2);
        setupExpandableCard(R.id.img3, R.id.expandableView3);
        setupExpandableCard(R.id.img4, R.id.expandableView4);
        setupExpandableCard(R.id.img5, R.id.expandableView5);
        setupExpandableCard(R.id.img6, R.id.expandableView6);
        setupExpandableCard(R.id.img7, R.id.expandableView7);


        setupExpandableCardText(R.id.sub1, R.id.expandableView1);
        setupExpandableCardText(R.id.sub2, R.id.expandableView2);
        setupExpandableCardText(R.id.sub3, R.id.expandableView3);
        setupExpandableCardText(R.id.sub4, R.id.expandableView4);
        setupExpandableCardText(R.id.sub5, R.id.expandableView5);
        setupExpandableCardText(R.id.sub6, R.id.expandableView6);
        setupExpandableCardText(R.id.sub7, R.id.expandableView7);











    }


    private void initViews() {
        // Initialize TextViews

        sub1 = findViewById(R.id.sub1);
        a1 = findViewById(R.id.a1);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);
        a4 = findViewById(R.id.a4);

        sub2 = findViewById(R.id.sub2);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);

        sub3 = findViewById(R.id.sub3);
        c1 = findViewById(R.id.c1);

        sub4 = findViewById(R.id.sub4);
        d1 = findViewById(R.id.d1);

        sub5 = findViewById(R.id.sub5);
        e1 = findViewById(R.id.e1);

        sub6 = findViewById(R.id.sub6);
        f1 = findViewById(R.id.f1);

        sub7 = findViewById(R.id.sub7);
        g1 = findViewById(R.id.g1);
        g2 = findViewById(R.id.g2);
        g3 = findViewById(R.id.g3);
        g4 = findViewById(R.id.g4);
        g5 = findViewById(R.id.g5);
        g6 = findViewById(R.id.g6);





    }


//    private void setupExpandableCard(final int imageViewId, final int expandableViewId) {
//        final LinearLayout expandableView = findViewById(expandableViewId);
//        ImageView imgView = findViewById(imageViewId);
//
//        imgView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (expandableView.getVisibility() == View.GONE) {
//                    expandableView.setVisibility(View.VISIBLE);
//                } else {
//                    expandableView.setVisibility(View.GONE);
//                }
//            }
//        });
//    }


    // Method for setting up expandable card with an ImageView
    private void setupExpandableCard(final int imageViewId, final int expandableViewId) {
        ImageView imageView = findViewById(imageViewId);
        final LinearLayout expandableView = findViewById(expandableViewId);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView.getVisibility() == View.GONE) {
                    expandableView.setVisibility(View.VISIBLE);
                } else {
                    expandableView.setVisibility(View.GONE);
                }
            }
        };

        imageView.setOnClickListener(onClickListener);
    }

    // Method for setting up expandable card with a TextView
    private void setupExpandableCardText(final int textViewId, final int expandableViewId) {
        TextView textView = findViewById(textViewId);
        final LinearLayout expandableView = findViewById(expandableViewId);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView.getVisibility() == View.GONE) {
                    expandableView.setVisibility(View.VISIBLE);
                } else {
                    expandableView.setVisibility(View.GONE);
                }
            }
        };

        textView.setOnClickListener(onClickListener);
    }





    private void loadDataFromJSON() {
        try {
            JSONArray jSONArray = new JSONArray(datapasss());
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                this.sub1.setText(Html.fromHtml(jSONObject.getString("sub1")));
                this.a1.setText(Html.fromHtml(jSONObject.getString("a1")));
                this.a2.setText(Html.fromHtml(jSONObject.getString("a2")));
                this.a3.setText(Html.fromHtml(jSONObject.getString("a3")));
                this.a4.setText(Html.fromHtml(jSONObject.getString("a4")));

                this.sub2.setText(Html.fromHtml(jSONObject.getString("sub2")));
                this.b1.setText(Html.fromHtml(jSONObject.getString("b1")));
                this.b2.setText(Html.fromHtml(jSONObject.getString("b2")));
                this.b3.setText(Html.fromHtml(jSONObject.getString("b3")));
                this.b4.setText(Html.fromHtml(jSONObject.getString("b4")));

                this.sub3.setText(Html.fromHtml(jSONObject.getString("sub3")));
                this.c1.setText(Html.fromHtml(jSONObject.getString("c1")));

                this.sub4.setText(Html.fromHtml(jSONObject.getString("sub4")));
                this.d1.setText(Html.fromHtml(jSONObject.getString("d1")));

                this.sub5.setText(Html.fromHtml(jSONObject.getString("sub5")));
                this.e1.setText(Html.fromHtml(jSONObject.getString("e1")));

                this.sub6.setText(Html.fromHtml(jSONObject.getString("sub6")));
                this.f1.setText(Html.fromHtml(jSONObject.getString("f1")));


                this.sub7.setText(Html.fromHtml(jSONObject.getString("sub7")));
                this.g1.setText(Html.fromHtml(jSONObject.getString("g1")));
                this.g2.setText(Html.fromHtml(jSONObject.getString("g2")));
                this.g3.setText(Html.fromHtml(jSONObject.getString("g3")));
                this.g4.setText(Html.fromHtml(jSONObject.getString("g4")));
                this.g5.setText(Html.fromHtml(jSONObject.getString("g5")));
                this.g6.setText(Html.fromHtml(jSONObject.getString("g6")));


            }
        } catch (IOException | JSONException e) {
            Log.d("TAG", "addItemsFromJSON: ", e);
        }
    }
    private String datapasss() throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = getResources().openRawResource(R.raw.zakat);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
            }
            return new String(sb);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }



}
