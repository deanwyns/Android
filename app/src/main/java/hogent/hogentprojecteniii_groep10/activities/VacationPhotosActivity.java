package hogent.hogentprojecteniii_groep10.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.helpers.HelperMethods;
import hogent.hogentprojecteniii_groep10.helpers.RestClient;
import hogent.hogentprojecteniii_groep10.models.Photo;
import hogent.hogentprojecteniii_groep10.models.Vacation;

/**
 * De activity die foto's zal tonen van een kamp
 */
public class VacationPhotosActivity extends Activity {

    private HashMap<String, String> full_image_url_maps;
    private HashMap<String, String> thumbnail_url_maps;
    private Vacation vacation;
    private SliderLayout sliderShow;
    private List<Photo> photoList = new ArrayList<Photo>();

    /**
     * De methode die het venster zal maken en de foto url's downloaden.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_photos);
        vacation = (Vacation) getIntent().getParcelableExtra("SpecificVacation");
        sliderShow = (SliderLayout) findViewById(R.id.photo_slider);

        try {
            if(HelperMethods.isNetworkAvailable(getApplicationContext()))
                photoList = new DownloadFotoUrlsTask().execute().get();
            else
                Toast.makeText(this, getResources().getString(R.string.no_internet_available), Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        full_image_url_maps = new LinkedHashMap<String, String>();
        thumbnail_url_maps = new LinkedHashMap<String, String>();
        for(Photo p : photoList){
            full_image_url_maps.put(p.getTitle(), p.getFullsize());
            thumbnail_url_maps.put(p.getTitle(), p.getThumbnail());
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

    /**
     * Indien er op een thumbnail wordt geklikt, zal via deze methode de foto getoond worden in het scherm
     * @param position de positie van de afbeelding waar op werd geklikt
     */
    private void showImageInSliderShow(int position) {
        sliderShow.stopAutoCycle();
        sliderShow.removeAllSliders();
        TextSliderView textSliderView = new TextSliderView(this);
        textSliderView
                .description(new ArrayList<String>(thumbnail_url_maps.keySet()).get(position))
                .image(new ArrayList<String>(full_image_url_maps.values()).get(position));
        sliderShow.addSlider(textSliderView);
    }

    /**
     * Start een nieuwe slideshow van alle foto's.
     */
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

    /**
     * Zal de menubar maken op basis van vacation_photos xml
     * @param menu het menu dat wordt opgevuld
     * @return
     */
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

    /**
     * Bepaald de actie die wordt uitgevoerd als er op een menu item geklikt wordt.
     * @param item het item waarop werd geklikt
     * @return bepaald hoe verdere menu processing wordt afgehandeld
     */
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

    /**
     * De klasse die foto url's zal downloaden via RetroFit.
     * Zal deze teruggeven zodra hij klaar is.
     */
    public class DownloadFotoUrlsTask extends AsyncTask<Void, Void, List<Photo>> {

        /**
         * Haalt de foto url's op van de server voor de huidige vakantie
         * @param params zal niks zijn
         * @return de foto url's van de server
         */
        @Override
        protected List<Photo> doInBackground(Void... params) {
            RestClient restClient = new RestClient();
            return restClient.getRestService().getPhotosForVacation(vacation.getId());
        }
    }

    /**
     * De custom adapter om een gridview op te vullen met afbeeldingen.
     */
    private class ImageAdapter extends BaseAdapter {
        private Context mContext;

        /**
         * Zal de context in deze klasse gebruiken en zal de universal image loader initialiseren.
         * @param c
         */
        public ImageAdapter(Context c) {
            mContext = c;

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .build();
            ImageLoader.getInstance().init(config);
        }

        /**
         * Bepaalde hoeveel afbeeldingen er zijn
         * @return
         */
        @Override
        public int getCount() {
            return thumbnail_url_maps.size();
        }

        /**
         * Standaard methode die overridden moet worden
         * @param position
         * @return
         */
        @Override
        public Object getItem(int position) {
            return null;
        }

        /**
         * Standaard methode die overridden moet worden.
         * @param position
         * @return
         */
        @Override
        public long getItemId(int position) {
            return 0;
        }

        /**
         * Zal een imageView aanmaken voor elk item in de adapter.
         * De afbeeldingen worden asynchroon geladen via de universal image loader.
         * @param position de positie van de huidige afbeelding
         * @param convertView de oude view om te hergebruiken
         * @param parent de ouder waarop deze view geplaatst wordt
         * @return de view met de afbeelding
         */
        @Override
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
