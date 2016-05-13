package com.huixiang.live.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huixiang.live.R;
import com.huixiang.live.model.PositionAdvertBO;
import com.huixiang.live.utils.widget.BannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stone on 16/5/13.
 */
public class FragmentIndex extends Fragment {

    private List<PositionAdvertBO> guangGao = new ArrayList<PositionAdvertBO>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater
                .inflate(R.layout.fragment_index, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

