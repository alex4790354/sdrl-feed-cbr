package com.github.alex4790354.general.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


class DateHelperTest {

    @Test
    void getNewDateAsString_ShouldCorrectlyTransformDateFormats() {
        String result = DateHelper.getNewDateAsString("05.20.2025", "MM.dd.yyyy", "yyyy-MM-dd");
        assertEquals("2025-05-20", result);
    }

    @Test
    void getNewDateAsString_ShouldThrowExceptionOnInvalidDate() {
        assertThrows(DateTimeParseException.class, () -> {
            DateHelper.getNewDateAsString("2025-05-20", "MM.dd.yyyy", "yyyy-MM-dd");
        });
    }

    @Test
    void getNewDateAsString_ShouldThrowExceptionOnInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateHelper.getNewDateAsString("05.20.2025", "wrong-format", "yyyy-MM-dd");
        });
    }

    @Test
    void getTodateDateMinusDaysAsString_ShouldReturnCorrectDate() {
        String todayMinusTen = LocalDate.now().minusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String result = DateHelper.getTodateDateMinusDaysAsString("yyyy-MM-dd", 10);
        assertEquals(todayMinusTen, result);
    }

    @Test
    void convertToSimpleDate_ShouldConvertIsoStringToDate() {
        String isoDate = "2023-12-13T00:00:00+03:00";
        String result = DateHelper.convertToSimpleDate(isoDate);
        assertEquals("2023-12-13", result);
    }

    @Test
    void convertToSimpleDate_ShouldThrowExceptionOnInvalidIsoString() {
        assertThrows(DateTimeParseException.class, () -> {
            DateHelper.convertToSimpleDate("invalid-date");
        });
    }
}