package pl.maciejnierzwicki.simpleforumapp.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class MainUtil {
	
	public static int getFullYears(Date date) {
		if(date == null) { return -1; }
		LocalDate current = LocalDate.now();
		LocalDate local_date = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		Period period = Period.between(local_date, current);
		
		return period.getYears();
	}
	
	public static String getTextRepresentation(Date date, String format) {
		if(date == null) { return null; }
		SimpleDateFormat date_format = new SimpleDateFormat(format);
		return date_format.format(date);
	}

}
