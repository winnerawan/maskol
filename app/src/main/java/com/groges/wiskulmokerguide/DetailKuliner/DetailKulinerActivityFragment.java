package com.groges.wiskulmokerguide.DetailKuliner;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groges.wiskulmokerguide.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailKulinerActivityFragment extends Fragment {

    public DetailKulinerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_kuliner, container, false);
    }
}
