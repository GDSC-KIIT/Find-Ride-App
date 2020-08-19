package com.example.myapplication;
//This class stores the info about a Customer
public class Customers
{
    private String names,phno;

    public Customers() {
    }

    public Customers(String name, String phno)
    {
        this.names = name;
        this.phno = phno;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String name) {
        this.names = name;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }
}
