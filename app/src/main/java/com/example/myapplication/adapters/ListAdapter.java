package com.example.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DriverList;
import com.example.myapplication.DriverListActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.MapsActivity;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DriverList> driverLists;
    double cuslat,cuslon;

    public ListAdapter(ArrayList<DriverList> driverLists, double cuslat, double cuslon) {
        this.driverLists = driverLists;
        this.cuslat = cuslat;
        this.cuslon = cuslon;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        final DriverList driverListData = driverLists.get(position);
        ViewHolder y = (ViewHolder)holder;
        y.driver_name.setText(driverLists.get(position).getName());
        y.distance.setText(driverLists.get(position).getDis());
        final double drilat,drilon;
        drilat=driverLists.get(position).getLat();
        drilon=driverLists.get(position).getLon();
        y.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Name : "+driverListData.getName()+"\nDistance = "+ driverListData.getDis(),Toast.LENGTH_LONG).show();
            }
        });
        y.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                intent.putExtra("cuslat", cuslat);
                intent.putExtra("cuslong", cuslon);
                intent.putExtra("drilat",drilat);
                intent.putExtra("drilon",drilon);
                view.getContext().startActivity(intent); }
        });
    }

    @Override
    public int getItemCount() {
        return driverLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView driver_name;
        public TextView distance;
        public RelativeLayout relativeLayout;
        public ImageButton imageButton;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageButton = (ImageButton)itemView.findViewById(R.id.info_button);
            this.distance = (TextView) itemView.findViewById(R.id.distance);
            this.driver_name = (TextView) itemView.findViewById(R.id.driver_name);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.list_item);
        }
    }
}
