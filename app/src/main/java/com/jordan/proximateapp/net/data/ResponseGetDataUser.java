package com.jordan.proximateapp.net.data;

import java.util.ArrayList;

/**
 * Created by jordan on 06/02/2018.
 */

public class ResponseGetDataUser extends ResponseWS {

    ArrayList<DataUser> data;

    public ArrayList<DataUser> getData() {
        return data;
    }

    public void setData(ArrayList<DataUser> data) {
        this.data = data;
    }
}
