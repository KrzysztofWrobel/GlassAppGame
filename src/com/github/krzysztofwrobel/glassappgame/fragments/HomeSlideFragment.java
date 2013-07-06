package com.github.krzysztofwrobel.glassappgame.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.krzysztofwrobel.glassappgame.R;
import com.github.krzysztofwrobel.glassappgame.models.Challenge;
import com.squareup.picasso.Picasso;

/**
 * Created with IntelliJ IDEA.
 * User: Krzysztof
 * Date: 06.07.13
 * Time: 12:42
 * To change this template use File | Settings | File Templates.
 */
public class HomeSlideFragment extends Fragment {

    private TextView slideTitleTextView;
    private ImageView backgroundImageView;
    private Challenge challenge;

    public HomeSlideFragment(Challenge challenge) {
        this.challenge = challenge;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.home_item_fragment, container, false);

        backgroundImageView = (ImageView) rootView.findViewById(R.id.iv_slide_image);
        slideTitleTextView = (TextView) rootView.findViewById(R.id.tv_slide_title);
        getChallangeView();


        return rootView;
    }

    private void getChallangeView() {
        //TODO Alek użyj picasso żeby pobrać jakoś ten obrazek i ustawić
        //backgroundImageView.setImageDrawable(null);
		Picasso.with(getActivity()).load(challenge.getImage_link()).resize(640, 320).into(backgroundImageView);
        slideTitleTextView.setText(challenge.getTitle());
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge chalange) {
        this.challenge = chalange;
        getChallangeView();
    }
}
