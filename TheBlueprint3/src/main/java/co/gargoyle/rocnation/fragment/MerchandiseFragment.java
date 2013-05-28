package co.gargoyle.rocnation.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import co.gargoyle.rocnation.R;



////////////////////////////////////////////////////////////
// MerchandiseFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class MerchandiseFragment extends Fragment {


	public MerchandiseFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_merchandise, container, false);
		Button buyMerch = (Button) rootView.findViewById(R.id.buyMerchButton);
        buyMerch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // go to next fragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.hide(MerchandiseFragment.this);
                transaction.addToBackStack("add");
                transaction.add(R.id.content_frame, new BuyMerchFragment(), "Buy Merch").commit();
            }
        });

		// int i = getArguments().getInt(ARG_PLANET_NUMBER);
		// String planet = getResources().getStringArray(R.array.nav_array)[i];

		getActivity().setTitle("Merchandise");

		return rootView;
	}
}
