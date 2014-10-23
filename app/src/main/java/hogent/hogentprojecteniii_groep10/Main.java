package hogent.hogentprojecteniii_groep10;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Main extends Activity {

    private EditText et;
    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getButton = (Button) findViewById(R.id.get_button);
        et = (EditText) findViewById(R.id.example_textarea);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeatherData();
            }
        });
    }

    private void getWeatherData() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.openweathermap.org")
                .build();
        final RestService service = restAdapter.create(RestService.class);
        RestService.WeerOverzicht response = null;
        try {
            response = new AsyncTask<Void, Void, RestService.WeerOverzicht>(){
                @Override
                protected RestService.WeerOverzicht doInBackground(Void... voids) {
                    return service.getWeerOverzicht("Brussels,be");
                }
            }.execute().get();
        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(response != null){
            et.setText("Lat: " + response.coord.lat + "; Lon: " + response.coord.lon + "\nName: "
                    + response.name + "\nWind:\nspeed: " + response.wind.speed + "\ndegree: " + response.wind.deg +
                    "\nid: " + response.id);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
