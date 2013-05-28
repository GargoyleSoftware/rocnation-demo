package co.gargoyle.rocnation.fragment;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.gargoyle.rocnation.R;



/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class CheckoutFragment extends Fragment {
    public static final String ARG_PLANET_NUMBER = "planet_number";

    public CheckoutFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tickets_checkout, container, false);
 

        getActivity().setTitle("Checkout Page");

        return rootView;
    }
}
