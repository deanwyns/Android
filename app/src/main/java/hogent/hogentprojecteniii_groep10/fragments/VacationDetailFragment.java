package hogent.hogentprojecteniii_groep10.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.activities.MainSettingsActivity;
import hogent.hogentprojecteniii_groep10.activities.VacationPhotosActivity;
import hogent.hogentprojecteniii_groep10.activities.VacationSignupActivity;
import hogent.hogentprojecteniii_groep10.models.Vacation;

/**
 * Het fragment dat een specifieke vakantie zal voorstellen.
 */
public class VacationDetailFragment extends Fragment {

    private Vacation vacation;
    private Button registerButton, photosButton;

    /**
     * Zal de vakantie als parcelable uit de argumenten halen.
     * Deze is meegegeven uit het listfragment nadat er op een vakantie is geklikt.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vacation = (Vacation) getArguments().getParcelable("SpecificVacation");

    }

    /**
     * Zal de interface van de specifieke vakantie opbouwen
     * @param inflater instantieert de xml voor een view object
     * @param container
     * @param savedInstanceState
     * @return de view die gemaakt werd
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vacation_detail, container, false);

        registerButton = (Button) view.findViewById(R.id.specific_vacation_signup_btn);
        photosButton = (Button) view.findViewById(R.id.specific_vacation_images_btn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupActivity = new Intent(getActivity().getApplicationContext(), VacationSignupActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("SpecificVacation", vacation);
                signupActivity.putExtras(mBundle);
                startActivity(signupActivity);
            }
        });

        photosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoGallery = new Intent(getActivity().getApplicationContext(), VacationPhotosActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("SpecificVacation", vacation);
                photoGallery.putExtras(mBundle);
                startActivity(photoGallery);
            }
        });

        TextView vacationTitleTextView = (TextView) view.findViewById(R.id.specific_vacation_title);
        TextView vacationPromotionalTextTextView = (TextView) view.findViewById(R.id.specific_vacation_promotext_text);
        TextView vacationDescriptionTextView = (TextView) view.findViewById(R.id.specific_vacation_description_text);
        TextView vacationDestinationTextView = (TextView)view. findViewById(R.id.specific_vacation_destination_text);
        TextView vacationAgeTextView = (TextView) view.findViewById(R.id.specific_vacation_age_text);
        TextView vacationWhenTextView = (TextView) view.findViewById(R.id.specific_vacation_when_text);
        TextView vacationTransportationTextView = (TextView) view.findViewById(R.id.specific_vacation_transportation_text);
        TextView vacationParticipantsTextView = (TextView) view.findViewById(R.id.specific_vacation_participants_text);
        TextView vacationPriceTextView = (TextView) view.findViewById(R.id.specific_vacation_price_text);
        TextView vacationDiscountPriceTextView = (TextView) view.findViewById(R.id.specific_vacation_discount_price_text);
        TextView vacationTaxDeductableTextView = (TextView) view.findViewById(R.id.specific_vacation_tax_deductable_text);

        vacationTitleTextView.setText(vacation.getTitle());
        vacationPromotionalTextTextView.setText(vacation.getPromoText());
        vacationDescriptionTextView.setText(vacation.getDescription());
        vacationDestinationTextView.setText(vacation.getLocation());
        vacationAgeTextView.setText(String.format("%d - %d", vacation.getAgeFrom(), vacation.getAgeTo()));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        vacationWhenTextView.setText(String.format("%s - %s", formatter.format(vacation.getBeginDate().getTime()), formatter.format(vacation.getEndDate().getTime())));
        vacationTransportationTextView.setText(vacation.getTransportation());
        vacationParticipantsTextView.setText(String.format("?/%d", vacation.getMaxParticipants()));
        vacationPriceTextView.setText(String.format("€%.2f", vacation.getBaseCost()));
        vacationDiscountPriceTextView.setText(String.format("%s €%.2f\n%s €%.2f", getResources().getString(R.string.eenOuderLidBM),
                vacation.getOneBmMemberCost(), getResources().getString(R.string.beideOudersLidBM), vacation.getTwoBmMemberCost()));
        vacationTaxDeductableTextView.setText((vacation.isTaxDeductable()==1 ? R.string.ja : R.string.nee));


        //Zaken verbergen als ingelogd als kind
        TextView taxDeductableTitle = (TextView) view.findViewById(R.id.specific_vacation_tax_deductable_title);
        TextView priceTitle = (TextView) view.findViewById(R.id.specific_vacation_price_title);
        TextView discountPriceTitle = (TextView) view.findViewById(R.id.specific_vacation_discount_price_title);
        TextView moreInformationLbl = (TextView) view.findViewById(R.id.specific_vacation_more_information);

        moreInformationLbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settings = new Intent(getActivity().getApplicationContext(), MainSettingsActivity.class);
                startActivity(settings);
            }
        });

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean loggedInAsChild = sharedPref.getBoolean(MainSettingsActivity.LOGGED_IN_AS_CHILD, false);
        if(loggedInAsChild){
            makeTextViewsVisible(moreInformationLbl);
            makeTextViewsGone(vacationDiscountPriceTextView, vacationPriceTextView, vacationTaxDeductableTextView, taxDeductableTitle, priceTitle, discountPriceTitle);
        }else{
            makeTextViewsGone(moreInformationLbl);
            makeTextViewsVisible(vacationDiscountPriceTextView, vacationPriceTextView, vacationTaxDeductableTextView, taxDeductableTitle, priceTitle, discountPriceTitle);
        }

        return view;
    }

    /**
     * Zal bepaalde textview zichtbaar maken
     * @param textViews de textviews die zichtbaar gemaakt moeten worden
     */
    //Kan ook in 1 methode, maar dit vind ik duidelijker
    private void makeTextViewsVisible(TextView... textViews) {
        for(TextView textView : textViews)
            textView.setVisibility(View.VISIBLE);
    }

    /**
     * Zal bepaalde textviews onzichtbaar maken
     * @param textViews de textviews die onzichtbaar gemaakt moeten worden
     */
    private void makeTextViewsGone(TextView... textViews) {
        for(TextView textView : textViews)
            textView.setVisibility(View.GONE);
    }

    /**
     * Bij het maken van een fragment zal de vakantie geplaatst worden in de bundle
     * Dit wordt gebruikt bij de activity van deze fragment
     * @param vacation de vakantie die in de bundle zal gestoken worden
     * @return de fragment met een vakantie in de arguments
     */
    public static VacationDetailFragment newInstance(Vacation vacation) {
        VacationDetailFragment vacFragment = new VacationDetailFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("SpecificVacation", vacation);
        vacFragment.setArguments(mBundle);
        return vacFragment;
    }
}
