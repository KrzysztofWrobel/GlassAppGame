package com.hackato.GlassAppGame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created with IntelliJ IDEA.
 * User: Krzysztof
 * Date: 06.07.13
 * Time: 12:42
 * To change this template use File | Settings | File Templates.
 */
public class HomeSlideFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.home_item_fragment, container, false);

        return rootView;
    }
}
