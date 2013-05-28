package co.gargoyle.rocnation.fragment;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.gargoyle.rocnation.R;



////////////////////////////////////////////////////////////
//CheckoutFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class CheckoutMerchFragment extends Fragment {

    public CheckoutMerchFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_merchandise_checkout, container, false);
 

        getActivity().setTitle("Checkout Merchandise Page");

        return rootView;
    }
}
