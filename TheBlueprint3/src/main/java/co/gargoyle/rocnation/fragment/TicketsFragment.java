package co.gargoyle.rocnation.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import co.gargoyle.rocnation.R;



////////////////////////////////////////////////////////////
// TicketsFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class TicketsFragment extends Fragment {
   

    public TicketsFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tickets, container, false);
        Button purchaseTickets = (Button) rootView.findViewById(R.id.purchaseTicketsButton);
        purchaseTickets.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // go to next fragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.hide(TicketsFragment.this);
                transaction.addToBackStack("add");
                transaction.add(R.id.content_frame, new BuyTicketFragment(), "Buy").commit();
            }
        });

        getActivity().setTitle("Tickets");

        return rootView;
    }

}
