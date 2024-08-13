package com.flightsearch.utils.models.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DayDTO {

    public int count;
    public String dayString;

    public static List<DayDTO> getDays() {
        List<DayDTO> returnList = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            returnList.add(new DayDTO(i, i + " " + (i == 1 ? "Day" : "Days")));
        }
        return returnList;
    }

    public DayDTO(int count, String dayString) {
        this.count = count;
        this.dayString = dayString;
    }
}
