package com.flightsearch.utils.models.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonthDTO {

    public int month;
    public int year;
    public String monthAndYear;

    public static String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public static List<MonthDTO> getNext12Months() {
        List<MonthDTO> returnList = new ArrayList<>();
        Date today = new Date();
        for (int i = 0; i < 12; i++) {
            int month = today.getMonth();
            int year = today.getYear() + 1900;
            String monthAndYear = monthNames[month] + " " + year;
            returnList.add(new MonthDTO(month, year, monthAndYear));
            today.setMonth(today.getMonth() + 1);
        }
        return returnList;
    }

    public MonthDTO(int month, int year, String monthAndYear) {
        this.month = month;
        this.year = year;
        this.monthAndYear = monthAndYear;
    }
}
