package co.gargoyle.rocnation.list;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.gargoyle.rocnation.R;
import co.gargoyle.rocnation.model.json.Lyric;

public class LyricAdapter extends BaseAdapter {

	private static LayoutInflater inflater = null;


	private List<Lyric> lyrics = new ArrayList<Lyric>();

	public LyricAdapter(Context context, List<Lyric> lyrics) {
        inflater = LayoutInflater.from(context);
        this.lyrics = lyrics;
    }

	public int getCount() {
		return lyrics.size();
	}

	public Object getItem(int position) {
		return lyrics.get(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (vi == null) {
			vi = inflater.inflate(R.layout.row_lyric, null);
		}

		TextView textView = (TextView) vi.findViewById(R.id.lyric_text);
		ImageView imageView = (ImageView) vi.findViewById(R.id.indicator);

		imageView.setVisibility(View.VISIBLE);

		Lyric lyric = lyrics.get(position);

		textView.setText(lyric.text);

		return vi;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
