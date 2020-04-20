
package com.example.week8;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Thing {
    private Date thing_startDate;
    private Date thing_endDate;
    private String thing_name;
    private int thing_rate;
    private int number;
    private Uri thing_imgbyte;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    public Thing(String thing_name, Date thing_startDate, Date thing_endDate, int thing_rate, Uri thing_imgbyte,int number){

        this.thing_startDate = thing_startDate;
        this.thing_name = thing_name;
        this.thing_endDate=thing_endDate;
        this.thing_imgbyte=thing_imgbyte;
        this.thing_rate=thing_rate;
        this.number=number;
    }

    public String getStartDate() {
        return sdf1.format(thing_startDate);
    }
    public String getEndDate() {
        return sdf1.format(thing_endDate);
    }
    public String getthing_name() {
        return thing_name;
    }
    public int getthing_rate() { return thing_rate; }
    public void setthing_rate(int thing_rate) { this.thing_rate = thing_rate; }
    public Uri getthing_imgbyte() {
        return thing_imgbyte;
    }
    public Integer getSort(){ return Integer.parseInt(sdf.format(thing_startDate)); }
    public String getNumber2Str(){ return String.valueOf(number);}
}
