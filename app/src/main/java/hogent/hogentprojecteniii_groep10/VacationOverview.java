package hogent.hogentprojecteniii_groep10;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hogent.hogentprojecteniii_groep10.models.Vacation;


public class VacationOverview extends Activity {

    private List<Vacation> vacationList = new ArrayList<Vacation>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_overview);

        populateVacationList();
        populateListView();
        registerClickCallback();
    }

    private void populateVacationList(){
        String promoTextBarkenTijn = "Recept voor een fantastische zomervakantie: toffe monitoren, leuke vrienden, een prachtig vakantiecentrum en véél fun en ambiance! De monitoren zorgen voor een afwisselend programma (strand- en duinspelen, daguitstappen, themaspelen, fuif, …) maar willen jou er natuurlijk bij. Wacht niet te lang en plan je vakantie naar zee met JOETZ!";
        Vacation barkentijnZomerLp = new Vacation(0, "JOETZ aan zee", "Uitgebreide beschrijving die momenteel korter is dan de promotext.", promoTextBarkenTijn, "De Barkentijn, Nieuwpoort", 5, 16, "busvervoer of eigen vervoer", 90, 400.00, 310.00, 220.00, true);
        String promoTextKrokus = "Verveling krijgt geen kans tijdens de krokusvakantie want op maandag 03 maart 2014 trekken we er met z’n allen op uit! We logeren in het vakantiecentrum “De Barkentijn” te Nieuwpoort.\n" +
                "Vijf dagen lang spelen we de leukste spelletjes, voor klein en groot. Samen met je vakantievriendjes beleef je het ene avontuur na het andere.  Plezier gegarandeerd!";
        Vacation krokusVakantie = new Vacation(1, "Krokusvakantie aan zee", "Een beschrijving die meer zegt dan de huidige promotext die blijkbaar niet beschikbaar is.", promoTextKrokus, "De Barkentijn, Nieuwpoort", 5, 16, "busvervoer of eigen vervoer", 20, 165.00, 135.00, 105.00, true);

        vacationList.add(barkentijnZomerLp);
        vacationList.add(krokusVakantie);
        vacationList.add(barkentijnZomerLp);
        vacationList.add(krokusVakantie);
        vacationList.add(barkentijnZomerLp);
        vacationList.add(krokusVakantie);
        vacationList.add(barkentijnZomerLp);
        vacationList.add(krokusVakantie);
    }

    private void populateListView() {
        ArrayAdapter<Vacation> vacationAdapter = new VacationListAdapter();
        ListView vacationList = (ListView) findViewById(R.id.vacation_overview_list_view);
        vacationList.setAdapter(vacationAdapter);
    }

    private void registerClickCallback() {
        ListView vacList = (ListView) findViewById(R.id.vacation_overview_list_view);
        vacList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int pos, long id) {
                //Hier zal je de vakantie opzoeken en tonen waar he op hebt geklikt
                Vacation clickedVacation = vacationList.get(pos);
                String message = "Er is geklikt op " + pos
                + "\nMet titel: " + clickedVacation.getTitle();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private class VacationListAdapter extends ArrayAdapter<Vacation>{

        public VacationListAdapter() {
            super(getApplicationContext(), R.layout.vacation_item_view, vacationList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Haal de view op die ingevuld moet worden
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.vacation_item_view, parent, false);
            }

            //Geselecteerde vakantie zoeken
            Vacation currentVacation = vacationList.get(position);

            //Afbeelding opzetten
            ImageView imageView = (ImageView) itemView.findViewById(R.id.vacation_icon);
            //Het zou makkelijker zijn om een id in de model te steken en op basis daarvan de afbeelding te selecteren.
            if(currentVacation.getTitle().startsWith("Fiets"))
                imageView.setImageResource(R.drawable.biking);
            else if (currentVacation.getTitle().startsWith("Zwem"))
                imageView.setImageResource(R.drawable.swimming);
            else
                imageView.setImageResource(R.drawable.sports);

            //Verder de view opvullen
            TextView titleTxt =  (TextView) itemView.findViewById(R.id.vacation_title_lbl);
            titleTxt.setText(currentVacation.getTitle());
            TextView descTxt =  (TextView) itemView.findViewById(R.id.vacation_desc_lbl);
            descTxt.setText(currentVacation.getDescription());

            return itemView;
        }
    }
}
