package com.test.kani.songhistory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.kani.songhistory.R;
import com.test.kani.songhistory.utility.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryListAdapter extends BaseAdapter
{
    TextView noTextView, titleTextView, singerTextView;

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> list;
    private int layout;

    public HistoryListAdapter(Context context, int layout, ArrayList<HashMap<String, Object>> list)
    {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount()
    {
        return this.list.size();
    }

    @Override
    public Object getItem(int i)
    {
        return this.list.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        if( view == null )
        {
            this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = this.inflater.inflate(this.layout, viewGroup, false);
        }

        // Bind views
        this.noTextView = ViewHolder.get(view, R.id.no_textView);
        this.titleTextView = ViewHolder.get(view, R.id.title_textView);
        this.singerTextView = ViewHolder.get(view, R.id.singer_textView);

        // Set attributes


        this.noTextView.setText(this.list.get(i).get("no").toString().trim());
        this.titleTextView.setText(this.list.get(i).get("title").toString().trim());
        this.singerTextView.setText(this.list.get(i).get("singer").toString().trim());

        // Add Events

        return view;
    }
}
