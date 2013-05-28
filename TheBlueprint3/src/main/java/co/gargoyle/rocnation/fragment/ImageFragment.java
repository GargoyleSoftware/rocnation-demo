package co.gargoyle.rocnation.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import co.gargoyle.rocnation.R;
 
@SuppressLint("ValidFragment")
public class ImageFragment extends Fragment {
    private final int imageResourceId;
 
	public ImageFragment(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.fragment_image, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView1);
        imageView.setImageResource(imageResourceId);
        return rootView;
    }
}