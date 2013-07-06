package com.github.krzysztofwrobel.glassappgame.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.krzysztofwrobel.glassappgame.R;
import com.github.krzysztofwrobel.glassappgame.models.Reward;

/**
 * Created by Krzysztof on 06.07.13.
 */
public class ShareFragment extends Fragment implements OnFragmentClickedListener{
    private Reward reward;
    private TextView shareTextView;
    private Handler handler;

    public static Fragment getInstance(Reward reward){
        ShareFragment fragment = new ShareFragment();
        Bundle args = new Bundle();
        args.putParcelable("reward",reward);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.reward= getArguments().getParcelable("reward");
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.share_layout, container,false);
        shareTextView = (TextView) rootView.findViewById(R.id.tv_share);
        shareTextView.setText(getResources().getString(R.string.share_text));

        return rootView;
    }

    @Override
    public void fragmentClicked() {
        shareTextView.setText(getResources().getString(R.string.sharing_text));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shareTextView.setText(getResources().getString(R.string.shared_text));
            }
        },1000);

    }
}
