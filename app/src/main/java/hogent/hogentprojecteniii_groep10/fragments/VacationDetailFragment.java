package hogent.hogentprojecteniii_groep10.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.models.Vacation;

public class VacationDetailFragment extends Fragment {

    private Vacation vacation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vacation = (Vacation) getArguments().getParcelable("SpecificVacation");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vacation_detail, container, false);

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
        vacationDiscountPriceTextView.setText(String.format("%s €%.2f\n%s €%.2f", R.string.eenOuderLidBM,
                vacation.getOneBmMemberCost(), R.string.beideOudersLidBM, vacation.getTwoBmMemberCost()));
        vacationTaxDeductableTextView.setText((vacation.isTaxDeductable()==1 ? R.string.ja : R.string.nee));

        return view;
    }

    public static VacationDetailFragment newInstance(Vacation vacation) {
        VacationDetailFragment vacFragment = new VacationDetailFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("SpecificVacation", vacation);
        vacFragment.setArguments(mBundle);
        return vacFragment;
    }
}
