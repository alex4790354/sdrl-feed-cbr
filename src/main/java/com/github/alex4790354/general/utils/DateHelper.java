package com.github.alex4790354.general.utils;


import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateHelper {


    /** Example:
     *    getDateAsString("05.20.2025",
     *                    "MM.dd.yyyy",
     *                    "yyyy-MM-dd")  =>  "2025-05-20"
     */
    public static String getNewDateAsString(String providedDateAsString,
                                  String providedFormat,
                                  String requiredFormat) {

        LocalDate date = LocalDate.parse(providedDateAsString,
                DateTimeFormatter.ofPattern(providedFormat));

        return date.format(DateTimeFormatter.ofPattern(requiredFormat));
    }

    public static String getTodateDateMinusDaysAsString(String requiredFormat, int mDays) {
        return LocalDate.now().minusDays(mDays).format(DateTimeFormatter.ofPattern(requiredFormat));
    }

    // isoDateString example: 2023-12-13T00:00:00+03:00
    public static String convertToSimpleDate(String isoDateString) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(isoDateString);
        return zonedDateTime.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

}
