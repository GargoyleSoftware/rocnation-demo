package co.gargoyle.rocnation.fragment;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import co.gargoyle.rocnation.R;



/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class BuyTicketFragment extends Fragment {
    public static final String ARG_PLANET_NUMBER = "planet_number";

    public BuyTicketFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tickets_buy, container, false);
        Button checkout = (Button) rootView.findViewById(R.id.checkoutButton);
        checkout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Your ticket has been purchased", Toast.LENGTH_LONG).show();

                // go to next fragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.hide(BuyTicketFragment.this);
                transaction.addToBackStack("add");
                transaction.add(R.id.content_frame, new CheckoutFragment(), "Checkout").commit();
            }
        });

        getActivity().setTitle("Buy Tickets");

        return rootView;
    }
}
