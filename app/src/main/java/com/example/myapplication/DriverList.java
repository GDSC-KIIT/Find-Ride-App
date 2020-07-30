package com.example.myapplication;

public class DriverList {
    private String name;
    private String dis;
    public DriverList(String name,String dis){
        this.name=name;
        this.dis=dis;
    }
    public String getName(){
        return name;
    }
    public String getDis() {
        return dis;
    }
    public void setDis(String dis) {
        this.dis = dis;
    }
    public void setName(String name) {
        this.name = name;
    }
}
