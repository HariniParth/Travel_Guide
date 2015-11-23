package com.travel_guide.ketan.travel_guide;

/**
 * Created by Harini on 11/8/2015.
 */
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;


public class AboutFragment extends Fragment {

    Handler handler = new Handler();

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView img_1 = (ImageView) view.findViewById(R.id.snow_1);
        ImageView img_2 = (ImageView) view.findViewById(R.id.snow_2);
        ImageView img_3 = (ImageView) view.findViewById(R.id.snow_3);
        ImageView img_4 = (ImageView) view.findViewById(R.id.snow_4);
        ImageView img_5 = (ImageView) view.findViewById(R.id.snow_5);
        ImageView img_6 = (ImageView) view.findViewById(R.id.snow_6);
        ImageView img_7 = (ImageView) view.findViewById(R.id.snow_7);
        ImageView img_8 = (ImageView) view.findViewById(R.id.snow_8);
        ImageView img_9 = (ImageView) view.findViewById(R.id.snow_9);

        showDelayed(img_1, 1000);
        showDelayed(img_2, 2000);
        showDelayed(img_3, 3000);
        showDelayed(img_4, 4000);
        showDelayed(img_5, 5000);
        showDelayed(img_6, 6000);
        showDelayed(img_7, 7000);
        showDelayed(img_8, 8000);
        showDelayed(img_9, 9000);
    }

    public void showDelayed(final View v, int delay){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,
                        0.0f, 1400.0f);
                animation.setDuration(5000);
                animation.setRepeatCount(Animation.INFINITE);
                animation.setRepeatMode(Animation.REVERSE);
                v.setVisibility(View.VISIBLE);
                v.startAnimation(animation);
            }
        }, delay);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}