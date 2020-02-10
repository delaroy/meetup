package com.bamideleoguntuga.meetup.fragments;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bamideleoguntuga.meetup.R;
import com.bamideleoguntuga.meetup.adapter.TabAdapter;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UsersFragment extends Fragment {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private TabAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_bar, container, false);
        ButterKnife.bind(this, view);

        adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new UserA(), "Dress");
        adapter.addFragment(new UserA(), "Pants");
        adapter.addFragment(new UserA(), "Blazers");
        adapter.addFragment(new UserA(), "Jackets");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
