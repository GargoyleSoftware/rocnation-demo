package co.gargoyle.rocnation.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.gargoyle.rocnation.R;



////////////////////////////////////////////////////////////
// ArtFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a art
 */
public class ArtFragment extends Fragment {
	private MyAdapter pagerAdapter;
	private ViewPager artPager;

	public ArtFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_art, container, false);
		pagerAdapter = new MyAdapter(getFragmentManager());
		artPager = (ViewPager) rootView.findViewById(R.id.pager);
		artPager.setAdapter(pagerAdapter);
		return rootView;
	}
	
	public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
 
        @Override
        public int getCount() {
            return 3;
        }
 
        @Override
        public Fragment getItem(int position) {
            switch (position) {
            case 0:
                return new ImageFragment(R.drawable.art1);
            case 1:
                return new ImageFragment(R.drawable.art2);
            case 2:
                return new ImageFragment(R.drawable.art3);
 
            default:
                return null;
            }
        }
    }
}
