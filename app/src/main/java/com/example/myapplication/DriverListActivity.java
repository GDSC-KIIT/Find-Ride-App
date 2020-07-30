package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapters.ListAdapter;

public class DriverListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverview);
        //TODO : Add name and distance here
        DriverList[] driverLists = new DriverList[]{
                new DriverList("Chandan", "0.2km"),
                new DriverList("Ram", "0.3km"),
                new DriverList("Lakhan", "0.4km"),
                new DriverList("Champak", "0.5km"),
        };
        RecyclerView recyclerView  = (RecyclerView)findViewById(R.id.rec);
        ListAdapter adapter = new ListAdapter(driverLists);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
