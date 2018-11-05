package com.test.kani.songhistory.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.test.kani.songhistory.R;
import com.test.kani.songhistory.utility.FireStoreCallbackListener;
import com.test.kani.songhistory.utility.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewHistoryFragment extends Fragment
{
    Spinner searchSpinner;
    EditText searchEditText;
    ImageButton searchImgBtn;
    ListView historListView;

    View view;

    private ArrayList<HashMap<String, Object>> historyList;
    private FireStoreCallbackListener fireStoreCallbackListener;
    private LoadingDialog loadingDialog;

    public void setFireStoreCallbackListener(FireStoreCallbackListener listener)
    {
        this.fireStoreCallbackListener = listener;
    }

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

        this.view = inflater.inflate(R.layout.fragment_view_history, container, false);

        bindUI();

        return view;
    }

    private void bindUI()
    {
        this.searchSpinner = this.view.findViewById(R.id.search_spinner);
        this.searchEditText = this.view.findViewById(R.id.search_editText);
        this.searchImgBtn = this.view.findViewById(R.id.search_imgBtn);
        this.historListView = this.view.findViewById(R.id.history_listView);

        ArrayAdapter<CharSequence> searchAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.search_list,
                android.R.layout.simple_spinner_item);

        searchAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        this.searchSpinner.setAdapter(searchAdapter);
    }

}
