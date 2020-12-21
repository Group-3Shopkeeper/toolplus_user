package com.e.toolplus.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class BuyCartList implements Serializable {
    private ArrayList<Cart> list;

    public BuyCartList() {

    }

    public ArrayList<Cart> getList() {
        return list;
    }

    public void setList(ArrayList<Cart> list) {
        this.list = list;
    }

    public BuyCartList(ArrayList<Cart> list) {
        this.list = list;
    }
}
