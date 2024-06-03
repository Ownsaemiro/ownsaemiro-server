package org.dongguk.ownsaemiro.ownsaemiroserver.util;

import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static String convertDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public static String splitDate(String duration){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd");

        String[] dates = duration.split(Constants.STR_CONNECTOR);

        // 날짜 형식 변환
        LocalDate startDate = LocalDate.parse(dates[0], inputFormatter);
        LocalDate endDate = LocalDate.parse(dates[1], inputFormatter);

        // 변환된 날짜를 원하는 출력 형식으로 포맷
        String formattedStartDate = startDate.format(outputFormatter);
        String formattedEndDate = endDate.format(outputFormatter);

        return formattedStartDate + Constants.STR_CONNECTOR + formattedEndDate;
    }
}
