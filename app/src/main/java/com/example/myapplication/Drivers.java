package com.example.myapplication;

//Stores the details of drivers
public class Drivers
{
    String name,address,age,chasis_no,password,mobile,class_of_vehicle,engine_no;
    boolean authenticated,available;

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Drivers() {
    }

    public Drivers(String name, String address, String age, String chasis_no, String password, String mobile, String class_of_vehicle, String engine_no) {
        this.name = name;
        this.address = address;
        this.age = age;
        this.chasis_no = chasis_no;
        this.password = password;
        this.mobile = mobile;
        this.class_of_vehicle = class_of_vehicle;
        this.engine_no = engine_no;
        this.authenticated=false;
        this.available=false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getChasis_no() {
        return chasis_no;
    }

    public void setChasis_no(String chasis_no) {
        this.chasis_no = chasis_no;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getClass_of_vehicle() {
        return class_of_vehicle;
    }

    public void setClass_of_vehicle(String class_of_vehicle) {
        this.class_of_vehicle = class_of_vehicle;
    }

    public String getEngine_no() {
        return engine_no;
    }

    public void setEngine_no(String engine_no) {
        this.engine_no = engine_no;
    }
}
