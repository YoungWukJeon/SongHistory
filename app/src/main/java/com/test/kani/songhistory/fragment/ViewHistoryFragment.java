package com.test.kani.songhistory.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.kani.songhistory.R;

public class ViewHistoryFragment extends Fragment
{


    public ViewHistoryFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_history, container, false);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

}
