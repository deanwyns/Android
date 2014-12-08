package hogent.hogentprojecteniii_groep10.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.authentication.Login;
import hogent.hogentprojecteniii_groep10.helpers.RestClient;
import hogent.hogentprojecteniii_groep10.models.Photo;
import hogent.hogentprojecteniii_groep10.models.Vacation;

public class VacationPhotosActivity extends Activity {

    //Temp tot Laracasa mij links kan geven
    private HashMap<String, String> full_image_url_maps;
    private HashMap<String, String> thumbnail_url_maps;
    private Vacation vacation;
    private SliderLayout sliderShow;
    private List<Photo> photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_photos);
        vacation = (Vacation) getIntent().getParcelableExtra("SpecificVacation");
        sliderShow = (SliderLayout) findViewById(R.id.photo_slider);

        try {
            photoList = new DownloadFotoUrlsTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        full_image_url_maps = new LinkedHashMap<String, String>();
        thumbnail_url_maps = new LinkedHashMap<String, String>();
        for(Photo p : photoList){
            full_image_url_maps.put(Long.toString(p.getId()), p.getFullsize());
            thumbnail_url_maps.put(Long.toString(p.getId()), p.getThumbnail());
        }

        createBaseSliderShow();

        GridView gridview = (GridView) findViewById(R.id.photo_gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                showImageInSliderShow(position);
            }
        });

    }

    private void showImageInSliderShow(int position) {
        sliderShow.stopAutoCycle();
        sliderShow.removeAllSliders();
        TextSliderView textSliderView = new TextSliderView(this);
        textSliderView
                .description(new ArrayList<String>(thumbnail_url_maps.keySet()).get(position))
                .image(new ArrayList<String>(full_image_url_maps.values()).get(position));
        sliderShow.addSlider(textSliderView);
    }

    private void createBaseSliderShow() {
        sliderShow.removeAllSliders();
        sliderShow.startAutoCycle();
        for (Map.Entry<String, String> entry : full_image_url_maps.entrySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(entry.getKey())
                    .image(entry.getValue());
            sliderShow.addSlider(textSliderView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vacation_photos, menu);
        if (getActionBar() != null)
        {
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            getActionBar().setTitle(getActionBar().getTitle() + " " + vacation.getTitle());
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_play) {
            createBaseSliderShow();
        }
        return super.onOptionsItemSelected(item);
    }

    public class DownloadFotoUrlsTask extends AsyncTask<Void, Void, List<Photo>> {

        @Override
        protected List<Photo> doInBackground(Void... params) {
            RestClient restClient = new RestClient();
            return restClient.getRestService().getPhotosForVacation(vacation.getId());
        }
    }

    private class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .build();
            ImageLoader.getInstance().init(config);
        }

        public int getCount() {
            return thumbnail_url_maps.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            final ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(4, 4, 4, 4);
            } else {
                imageView = (ImageView) convertView;
            }
            //new DownloadImageTask(imageView).execute(new ArrayList<String>(thumbnail_url_maps.values()).get(position));
            ImageLoader.getInstance().loadImage(new ArrayList<String>(thumbnail_url_maps.values()).get(position), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    imageView.setImageBitmap(loadedImage);
                }
            });

            return imageView;
        }
    }
}
