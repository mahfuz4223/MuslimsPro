package com.dark.muslimspro;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class KalimaActivity extends AppCompatActivity {

    // Declare your TextViews
    private TextView f24897G, f24911U, f24912V, f24898H, f24913W, f24914X, f24899I, f24915Y, f24916Z,
            f24900J, f24917a0, f24918b0, f24901K, f24919c0, f24920d0, f24902L, f24921e0, f24922f0,
            f24903M, f24923g0, f24924h0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalima);

        // TextViews
        f24897G = findViewById(R.id.sub1);
        f24911U = findViewById(R.id.a1);
        f24912V = findViewById(R.id.a2);
        f24898H = findViewById(R.id.sub2);
        f24913W = findViewById(R.id.b1);
        f24914X = findViewById(R.id.b2);
        f24899I = findViewById(R.id.sub3);
        f24915Y = findViewById(R.id.c1);
        f24916Z = findViewById(R.id.c2);
        f24900J = findViewById(R.id.sub4);
        f24917a0 = findViewById(R.id.d1);
        f24918b0 = findViewById(R.id.d2);
        f24901K = findViewById(R.id.sub5);
        f24919c0 = findViewById(R.id.e1);
        f24920d0 = findViewById(R.id.e2);
        f24902L = findViewById(R.id.sub6);
        f24921e0 = findViewById(R.id.f1);
        f24922f0 = findViewById(R.id.f2);
        f24903M = findViewById(R.id.sub7);
        f24923g0 = findViewById(R.id.g1);
        f24924h0 = findViewById(R.id.g2);


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




        m10841g0();  // Call the JSON processing method




    }

    private void setupExpandableCard(final int imageViewId, final int expandableViewId) {
        final LinearLayout expandableView = findViewById(expandableViewId);
        ImageView imgView = findViewById(imageViewId);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView.getVisibility() == View.GONE) {
                    expandableView.setVisibility(View.VISIBLE);
                } else {
                    expandableView.setVisibility(View.GONE);
                }
            }
        });
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

    private void m10841g0() {
        try {
            JSONArray jSONArray = new JSONArray(m10837k0());
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                this.f24897G.setText(Html.fromHtml(jSONObject.getString("sub1")));
                this.f24911U.setText(Html.fromHtml(jSONObject.getString("a1")));
                this.f24912V.setText(Html.fromHtml(jSONObject.getString("a2")));
                this.f24898H.setText(Html.fromHtml(jSONObject.getString("sub2")));
                this.f24913W.setText(Html.fromHtml(jSONObject.getString("b1")));
                this.f24914X.setText(Html.fromHtml(jSONObject.getString("b2")));
                this.f24899I.setText(Html.fromHtml(jSONObject.getString("sub3")));
                this.f24915Y.setText(Html.fromHtml(jSONObject.getString("c1")));
                this.f24916Z.setText(Html.fromHtml(jSONObject.getString("c2")));
                this.f24900J.setText(Html.fromHtml(jSONObject.getString("sub4")));
                this.f24917a0.setText(Html.fromHtml(jSONObject.getString("d1")));
                this.f24918b0.setText(Html.fromHtml(jSONObject.getString("d2")));
                this.f24901K.setText(Html.fromHtml(jSONObject.getString("sub5")));
                this.f24919c0.setText(Html.fromHtml(jSONObject.getString("e1")));
                this.f24920d0.setText(Html.fromHtml(jSONObject.getString("e2")));
                this.f24902L.setText(Html.fromHtml(jSONObject.getString("sub6")));
                this.f24921e0.setText(Html.fromHtml(jSONObject.getString("f1")));
                this.f24922f0.setText(Html.fromHtml(jSONObject.getString("f2")));
                this.f24903M.setText(Html.fromHtml(jSONObject.getString("sub7")));
                this.f24923g0.setText(Html.fromHtml(jSONObject.getString("g1")));
                this.f24924h0.setText(Html.fromHtml(jSONObject.getString("g2")));
            }
        } catch (IOException | JSONException e) {
            Log.d("TAG", "addItemsFromJSON: ", e);
        }
    }
    private String m10837k0() throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = getResources().openRawResource(R.raw.kalema);
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
