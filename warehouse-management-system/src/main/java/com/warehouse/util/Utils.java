package com.warehouse.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Utils {

	public static Integer getDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
		return (int) ChronoUnit.DAYS.between(startDate, endDate);
	}
}
