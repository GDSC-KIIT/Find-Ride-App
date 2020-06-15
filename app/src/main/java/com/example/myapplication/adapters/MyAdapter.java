package com.example.myapplication.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

class MyAdapter extends BaseAdapter {

    // override other abstract methods here

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
          //  convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
        }

        assert convertView != null;
        ((TextView) convertView.findViewById(android.R.id.text1))
                .setText((Integer) getItem(position));
        return convertView;
    }
}
