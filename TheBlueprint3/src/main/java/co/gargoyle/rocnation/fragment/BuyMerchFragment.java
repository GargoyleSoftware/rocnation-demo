package co.gargoyle.rocnation.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import co.gargoyle.rocnation.R;



////////////////////////////////////////////////////////////
//BuyMerchFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class BuyMerchFragment extends Fragment {

	public BuyMerchFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_merchandise_buy, container, false);
		Button checkoutMerch = (Button) rootView.findViewById(R.id.checkoutMerchButton);
		checkoutMerch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Your item has been purchased", Toast.LENGTH_LONG).show();

				// go to next fragment
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.hide(BuyMerchFragment.this);
				transaction.addToBackStack("add");
				transaction.add(R.id.content_frame, new CheckoutMerchFragment(), "Checkout Merch").commit();
			}
		});

		getActivity().setTitle("Buy Merchandise");

		return rootView;
	}
}
