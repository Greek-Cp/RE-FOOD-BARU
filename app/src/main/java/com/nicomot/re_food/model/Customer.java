package com.nicomot.re_food.model;

import java.util.List;

public class Customer {
    private String name;
    private List<String> listMessage;

    public Customer(String name, List<String> listMessage) {
        this.name = name;
        this.listMessage = listMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getListMessage() {
        return listMessage;
    }

    public void setListMessage(List<String> listMessage) {
        this.listMessage = listMessage;
    }
}
