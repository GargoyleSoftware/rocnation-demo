package co.gargoyle.rocnation.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import co.gargoyle.rocnation.R;

public class NavAdapter extends BaseAdapter {

	private static LayoutInflater inflater = null;
	private static final int[] NAV_IMAGE_IDS = new int[] { R.drawable.music,
			R.drawable.videos, R.drawable.concerts, R.drawable.merchandise,
			R.drawable.lyrics};

	public NavAdapter(Context context) { 
        inflater = LayoutInflater.from(context);
    }

	public int getCount() {
		return NAV_IMAGE_IDS.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (vi == null) {
			vi = inflater.inflate(R.layout.row_nav, null);
		}

		ImageView imageView = (ImageView) vi.findViewById(R.id.image);
		int imageId = NAV_IMAGE_IDS[position];
		imageView.setImageResource(imageId);

		return vi;
	}
}
