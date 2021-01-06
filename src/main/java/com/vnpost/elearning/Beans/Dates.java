package com.vnpost.elearning.Beans;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Dates {
    public long days() {
        return days();
    }

    public long days(Date date2) { //xu li ngay thabg
        long getDaysDiff =0;
        DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date currentDate = new Date();
        Date date1 = null;
        Date date3 = null;

        try {
            String startDate =  simpleDateFormat.format(date2);
            String endDate = simpleDateFormat.format(currentDate);

            date1 = simpleDateFormat.parse(startDate);
            date3 = simpleDateFormat.parse(endDate);

            long getDiff = date3.getTime() - date1.getTime();

            getDaysDiff = getDiff / (24 * 60 * 60 * 1000);



        } catch (Exception e) {
            e.printStackTrace();
        }
        return getDaysDiff;

    }

    public long someday(Date dates,Date date2) { //xu li ngay thabg tru hai thơi didem khac nhau
        long getDaysDiff =0;
        DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");


        Date date1 = null;
        Date date3 = null;

        try {
            String startDate =  simpleDateFormat.format(date2);
            String endDate = simpleDateFormat.format(dates);

            date1 = simpleDateFormat.parse(startDate);
            date3 = simpleDateFormat.parse(endDate);

            long getDiff = date3.getTime() - date1.getTime();

            getDaysDiff = getDiff / (24 * 60 * 60 * 1000);



        } catch (Exception e) {
            e.printStackTrace();
        }
        return getDaysDiff;

    }

}
