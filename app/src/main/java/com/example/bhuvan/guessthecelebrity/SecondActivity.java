package com.example.bhuvan.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by bhuvan on 15/1/17.
 */

public class SecondActivity extends AppCompatActivity implements OnClickListener {
    Random r;
    int click = 0;
    String nameList[];
    String images[];
    String Options[];
    ArrayList<String> names;
    ArrayList<String> urls;
    ImageView iv;
    Button b1, b2, b3, b4;


    class Download extends AsyncTask<String, Integer, Bitmap> {
        URL url;
        HttpURLConnection httpURLConnection;
        Bitmap mp;

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                mp = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mp;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r = new Random();
        Options = new String[3];
        names = new ArrayList<String>();
        urls = new ArrayList<String>();
        images = urls.toArray(new String[names.size()]);
        nameList = names.toArray(new String[urls.size()]);
        iv = (ImageView) findViewById(R.id.iv);
        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);
        b4 = (Button) findViewById(R.id.b4);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        DownloadTask task = new DownloadTask();
        String result = null;

        try {

            result = task.execute("http://www.posh24.com/celebrities").get();
            CreateArrays(result);
        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }


    }

    void CreateOption(int click, View v) {
        if (((Button) v).getText().toString().equals(nameList[click - 1])) {
            Toast.makeText(SecondActivity.this, "Correct", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SecondActivity.this, "Wrong", Toast.LENGTH_SHORT).show();

        }
        String correctNSWER = nameList[click];
        ArrayList<Integer> list = new ArrayList();
        for (int m = 1; m < 5; m++) {
            list.add(m);
        }
        Collections.shuffle(list);

        b1.setText("" + nameList[click - 1 + list.get(0)]);
        b2.setText("" + nameList[click - 1 + list.get(1)]);
        b3.setText("" + nameList[click - 1 + list.get(2)]);
        b4.setText("" + nameList[click - 1 + list.get(3)]);

    }

    @Override
    public void onClick(View v) {
        click++;
        Download d = new Download();
        try {
            Bitmap mp = d.execute(images[click]).get();
            iv.setImageBitmap(mp);
            CreateOption(click, v);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }

                return result;

            } catch (Exception e) {

                e.printStackTrace();

            }

            return null;
        }
    }


    void CreateArrays(String s) {

        Pattern url = Pattern.compile("<img src=\"(.*?)\"");
        Matcher murl = url.matcher(s);
        Pattern name = Pattern.compile("alt=\"(.*?)\"");
        Matcher mname = name.matcher(s);
        for (int i = 1; i <= 100; i++) {
            murl.find();
            mname.find();
            urls.add(murl.group(1).toString());
            names.add(mname.group(1).toString());


        }

    }
}


