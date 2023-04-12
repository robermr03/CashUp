package com.example.cashupnd;

public class ListElement {
    public String name;
    public String number;
    public String username;

    public ListElement(String name, String number, String username) {
        this.name = name;
        this.number = number;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
