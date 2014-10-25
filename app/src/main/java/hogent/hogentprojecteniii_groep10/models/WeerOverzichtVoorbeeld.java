package hogent.hogentprojecteniii_groep10.models;

/*
Alles staat public en in 1 klasse omdat dit slechts een voorbeeld is.
Om je op te kunnen refereren hoe de JSON is opgebouwd tov de model.
In dit voorbeeld is de volgende GET gebruikt:
http://api.openweathermap.org/data/2.5/weather?q=Brussels,be
Hier is een voorbeeld van het oproepen van een rest service:

private void getWeatherData() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.openweathermap.org")
                .build();
        final RestService service = restAdapter.create(RestService.class);
        WeerOverzichtVoorbeeld response = null;
        try {
            response = new AsyncTask<Void, Void, WeerOverzichtVoorbeeld>(){
                @Override
                protected WeerOverzichtVoorbeeld doInBackground(Void... voids) {
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
        Met de volgende methode in de RestService interface

        @GET("/data/2.5/weather")
        WeerOverzichtVoorbeeld getWeerOverzicht(@Query("q") String stadEnLand);

 */
public class WeerOverzichtVoorbeeld {
    public Coord coord;
    public Sys sys;
    public Weather[] weather;
    public String base;
    public Main main;
    public Wind wind;
    public Clouds clouds;
    public long dt;
    public long id;
    public String name;
    public int cod;

    public class Coord {
        public double lon;
        public double lat;
    }

    public class Sys {
        public int type;
        public int id;
        public String message;
        public String country;
        public int sunrise;
        public int sunset;
    }

    public class Weather {
        public int id;
        public String main;
        public String description;
        public String icon;
    }

    public class WeatherData{
        public int id;
        public String main;
        public String description;
        public String icon;
    }

    public class Main {
        public double temp;
        public int pressure;
        public int humidity;
        public double temp_min;
        public double temp_max;
    }

    public class Wind {
        public double speed;
        public int deg;
    }

    public class Clouds{
        public int all;
    }
}





