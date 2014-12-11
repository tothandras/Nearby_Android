package com.andrastoth.nearby.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).inject(this);
    }
}