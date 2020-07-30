package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DriverList;
import com.example.myapplication.R;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private DriverList[] driverLists;
    public ListAdapter(DriverList[] driverLists){
        this.driverLists=driverLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final DriverList driverListData = driverLists[position];
        ViewHolder y = (ViewHolder)holder;
        y.driver_name.setText(driverLists[position].getName());
        y.distance.setText(driverLists[position].getDis());
        y.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Name : "+driverListData.getName()+"\nDistance = "+ driverListData.getDis(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return driverLists.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView driver_name;
        public TextView distance;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.distance = (TextView) itemView.findViewById(R.id.distance);
            this.driver_name = (TextView) itemView.findViewById(R.id.driver_name);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.list_item);
        }
    }
}
