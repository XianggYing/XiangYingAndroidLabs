package algonquin.cst2335.XiangYing;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

// new classes stuff
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import algonquin.cst2335.mylab5.R;

public class MainActivity extends AppCompatActivity {

    String text;
    Double currentTemp, maxTemp, minTemp, humidi;
    String description;
    String iconName ;
    String current;
    String min;
    String max;
    String humidity;
    String cityName;
    Bitmap image;
    String locationCity;
    String locationCountry;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText et= findViewById(R.id.editText);
        Button btn = findViewById(R.id.button);

        btn.setOnClickListener( click -> {
            cityName = et.getText().toString();
            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Getting forecast")
                    .setMessage("We're calling people in " + cityName + " to look outside their windows and tell us what's the weather like over there." )
                    .setView( new ProgressBar(    MainActivity.this))
                    .show();

            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute( ()-> {


                try{
                    cityName = et.getText().toString();
                    String serverURL = "https://api.openweathermap.org/data/2.5/weather?q="
                            + URLEncoder.encode(cityName, "UTF-8")  // whatever typed into EditText
                            + "&appid=7e943c97096a9784391a981c4d878b22&units=metric&mode=xml";


                    URL url = new URL(serverURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());


                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput( in  , "UTF-8"); //one element a time


                    while(xpp.next() != XmlPullParser.END_DOCUMENT) // scanner XML content
                    {
                        switch(xpp.getEventType())
                        {
                            case     XmlPullParser.START_TAG:
                                if(xpp.getName().equals("temperature"))
                                {

                                    current = xpp.getAttributeValue(null, "value");  //this gets the current temperature

                                    min = xpp.getAttributeValue(null, "min"); //this gets the min temperature

                                    max = xpp.getAttributeValue(null, "max"); //this gets the max temperature
                                }
                                else if(xpp.getName().equals("weather"))
                                {
                                    description = xpp.getAttributeValue(null, "value");  //this gets the weather description

                                    iconName = xpp.getAttributeValue(null, "icon"); //this gets the icon name
                                }
                                else if(xpp.getName().equals("humidity")){
                                    humidity = xpp.getAttributeValue(null, "value");
                                }
                                break;
                            case   XmlPullParser.END_TAG:
                                break;
                            case XmlPullParser.TEXT:
                                break;

                        }
                    }

                    text = (new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8)))
                            .lines()
                            .collect(Collectors.joining("\n"));

                    /*
                    JSONObject theDocument = new JSONObject( text ); //this converts the String to JSON Object. The whole page!!
                    JSONObject main = theDocument.getJSONObject("main");
                    JSONArray weatherArray = theDocument.getJSONArray("weather");
                    JSONObject position0 = weatherArray.getJSONObject(0);
                    description = position0.getString("description");
                    String iconName = position0.getString("icon");

                    currentTemp = main.getDouble("temp");
                    maxTemp = main.getDouble("temp_max");
                    minTemp = main.getDouble("temp_min");
                    humidi = main.getDouble("humidity");
                    String locationCity = et.getText().toString();
                    JSONObject sys = theDocument.getJSONObject("sys");
                    String locationCountry = sys.getString("country");
*/

                    File file = new File(getFilesDir(), iconName + ".png");
                    if(file.exists()){
                        image = BitmapFactory.decodeFile(getFilesDir() + "/" + iconName + ".png");
                    }
                    else {
                        URL imgUrl = new URL("https://openweathermap.org/img/w/" + iconName + ".png");
                        HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                        connection.connect();
                        int responseCode = connection.getResponseCode();
                        if (responseCode == 200) {
                            image = BitmapFactory.decodeStream(connection.getInputStream());
                            image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                        }
                    }

                    runOnUiThread( (  )  -> {

                        TextView tv2 = findViewById(R.id.temp);
                        tv2.setText("The current temperature is: " + currentTemp);
                        tv2.setVisibility(View.VISIBLE);

                        tv2 = findViewById(R.id.maxTemp);
                        tv2.setText("The Max temperature is: " + maxTemp);
                        tv2.setVisibility(View.VISIBLE);

                        tv2 = findViewById(R.id.minTemp);
                        tv2.setText("The Min temperature is: " + minTemp);
                        tv2.setVisibility(View.VISIBLE);

                        tv2 = findViewById(R.id.humitidy);
                        tv2.setText("The humidity: " + humidi + "%");
                        tv2.setVisibility(View.VISIBLE);

                        tv2 = findViewById(R.id.description);
                        tv2.setText(description);
                        tv2.setVisibility(View.VISIBLE);

                        tv2 = findViewById(R.id.location);
                        tv2.setText(locationCity + " ," + locationCountry);
                        tv2.setVisibility(View.VISIBLE);

                        ImageView iv = findViewById(R.id.icon);
                        iv.setImageBitmap(image);
                        iv.setVisibility(View.VISIBLE);
                        dialog.hide();
                    });



                } // try

                catch (IOException |  XmlPullParserException ex) {
                    ex.printStackTrace();
                }

            } ); // runnable, run() function, run on different cpu



        });

    }

}