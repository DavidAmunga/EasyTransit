package com.buttercell.easytransit.admin;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buttercell.easytransit.R;

import mehdi.sakout.aboutpage.AboutPage;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAbout extends Fragment {
    private static final String TAG = "AdminAbout";

    public AdminAbout() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return new AboutPage(getActivity())
                .isRTL(false)
                .setImage(R.drawable.logo)
                .setDescription("Easy Transit is an amazing app")
                .addGroup("Connect with us")
                .addEmail("easy.transit@gmail.com")
                .addWebsite("http://easy.transit.io/")
                .addFacebook("the.easy.transit")
                .addTwitter("easytransit")
                .addYoutube("https://www.youtube.com/watch?v=vF8i_uYFp10&t=328s")
                .addPlayStore("com.easy.transit.readitlater.pro")
                .create();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("");

        Log.d(TAG, "onViewCreated: About Start");
    }
}
