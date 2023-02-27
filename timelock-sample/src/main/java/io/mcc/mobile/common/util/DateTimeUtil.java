package io.mcc.mobile.common.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

import io.mcc.common.vo.CommonVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateTimeUtil {

	/**
	 * Range Query 를 위한 날짜 검색 조건 생성.
	 * @param searchType 일별(D)/월별(M)
	 * @param zi : User Timezone
	 * @return
	 */
	public static CommonVO getRangeDate(String searchType, ZoneId zi) {
		
		CommonVO days = new CommonVO();
		
		LocalDateTime userDateTime = LocalDateTime.ofInstant(Instant.now(), zi);		

		//hadle
		LocalDateTime yesterdayDateTime = LocalDateTime.ofInstant(Instant.now(), zi).minusDays(1);
		LocalDateTime beforeSixDay = LocalDateTime.ofInstant(Instant.now(), zi).minusDays(6);
		LocalDateTime beforeFiveMonth = LocalDateTime.ofInstant(Instant.now(), zi).minusMonths(5);
		LocalDateTime beforeOneMonth = LocalDateTime.ofInstant(Instant.now(), zi).minusMonths(1);
		
		LocalDateTime startOfDate = null;
		LocalDateTime endOfDate = null;

		if("R".equals(searchType)) {
			startOfDate = userDateTime.with(LocalTime.MIN);     //2018-10-22T00:00
			endOfDate = userDateTime;        					//요청현재시간
		} else if("D".equals(searchType)){
			startOfDate = userDateTime.with(LocalTime.MIN);		//2018-10-22T00:00
			endOfDate = userDateTime.with(LocalTime.MAX);		//2018-10-22T23:59:59.999999999
		} else if("Y".equals(searchType)){
			//-yesterday
			startOfDate = yesterdayDateTime.with(LocalTime.MIN);
			endOfDate = yesterdayDateTime.with(LocalTime.MAX);		
		} else if("7D".equals(searchType)){
			//-before 7 day
			startOfDate = beforeSixDay.with(LocalTime.MIN);		//2018-10-22T00:00
			endOfDate = userDateTime.with(LocalTime.MAX);		//2018-10-22T23:59:59.999999999
		} else if("M".equals(searchType)) {
			startOfDate = userDateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);	//2018-10-01T13:31:21.838
			endOfDate = userDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);		//2018-10-31T13:31:21.838
		} else if("1M".equals(searchType)) {
			startOfDate = beforeOneMonth.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);	//2018-10-01T13:31:21.838
			endOfDate = beforeOneMonth.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);	//2018-10-31T13:31:21.838			
		} else if("6M".equals(searchType)) {
			startOfDate = beforeFiveMonth.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);	//2018-10-01T13:31:21.838
			endOfDate = userDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);		//2018-10-31T13:31:21.838
		} else if ("DU".equals(searchType)) {
			startOfDate = userDateTime.with(LocalTime.MIN).minusHours(9);
			endOfDate = userDateTime.with(LocalTime.MAX).minusHours(9);
		}
		
		
		//- 
		String format_startOfDate = startOfDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String format_endOfDate = endOfDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		days.put("startOfDate", format_startOfDate);
		days.put("endOfDate", format_endOfDate);
		
		return days;
	}
	
	public static String getDate(ZoneId zi) {
		LocalDateTime userDateTime = LocalDateTime.ofInstant(Instant.now(), zi);
		
		String format_userDate = userDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		
		return format_userDate;
	}
	
	public static String getDatetime(ZoneId zi) {
		LocalDateTime userDateTime = LocalDateTime.ofInstant(Instant.now(), zi);
		
		String format_userDateTime = userDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		return format_userDateTime;
	}

	public static String getDatetime(ZoneId zi, String format) {
		LocalDateTime userDateTime = LocalDateTime.ofInstant(Instant.now(), zi);

		String format_userDateTime = userDateTime.format(DateTimeFormatter.ofPattern(format));

		return format_userDateTime;
	}

	public static String getPreviousMonth(ZoneId zoneId) {
		LocalDateTime previousMonth = LocalDateTime.ofInstant(Instant.now(), zoneId).minusMonths(1);
		return previousMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
	}
	
	public static String getYesterDate(ZoneId zi) {
		LocalDateTime yesterdayDateTime = LocalDateTime.ofInstant(Instant.now(), zi).minusDays(1);
		
		String format_userDate = yesterdayDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return format_userDate;
	}
	
	public static String getUtcZeroDatetime() {
		ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
		 String format_utcDateTime = utcDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		 
		 return format_utcDateTime;
	}
	
	public static String getUtcZeroDate() {
		ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
		 String format_utcDate = utcDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		 
		 return format_utcDate;
	}

	public static String getUtcZeroDate(Date date) {
		ZonedDateTime utcDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC"));
		String format_utcDate = utcDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		return format_utcDate;
	}

	public static String getNowDatetime() {
		LocalDateTime userDateTime = LocalDateTime.now();
		String format_userDateTime = userDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		return format_userDateTime;
	}
	
	public static long getDayBetween(String befor, String after) {
		
		LocalDate beforDate = LocalDate.parse(befor, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		LocalDate afterDate = LocalDate.parse(after, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		long between = ChronoUnit.DAYS.between(beforDate, afterDate);
		
		return between;
	}

	public static String getUtcZeroDateByOffset(long timezoneOffsetSec, String format) {
		ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneId.of("UTC"));

		ZonedDateTime ltzDateTime = utcDateTime.plusSeconds(timezoneOffsetSec);

		String format_ltzDate = ltzDateTime.format(DateTimeFormatter.ofPattern(format));

		return format_ltzDate;
	}
	
	public static boolean isUpdateToday(String dt) {
		if (dt == null) return false; 

		boolean rst = true;
		LocalDate beforDate = LocalDate.parse(dt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
		
		long between = ChronoUnit.DAYS.between(beforDate, utcDateTime);
		if(between>0) {
			rst = false;
		}
		
		return rst;
	}

	public static int dateCompareTo(String enddate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar1 = Calendar.getInstance(); //
		Date date =	 null;
		try {
			date =	dateFormat.parse(enddate);
		} catch(Exception e) {
			return 0;
		}
		 
        //앞에 변수가 크면 1, 작으면 -1, 같으면 0
        int result1 = date.compareTo(calendar1.getTime());
        
        if(result1 >= 0) {
        	//종료일이 더 크면  1 같으면 0
        	// 0은 아직 종료 되지 않았으므로 1을 리턴한다
        	return 1;
        }
        
        return 0;
	}
	
	
	public static int dateCompare(String startdate, String enddate) {
		SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
		int compare = 0;
		try {
			Date date1 = df.parse(startdate); 
			Date date2 = df.parse(enddate); 
			compare = date1.compareTo( date2 ); // 날짜비교	
		} catch (Exception e) {
			return 0;
		}
		
		return compare;
	}

	
	
	
	public static long getEndTime(String enddate, String toDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date2 = null;
		Date date =	 null;
		try {
			date =	dateFormat.parse(enddate+" 23:59:59");
			date2 = dateFormat.parse(toDate);
		} catch(Exception e) {
			return 0;
		}
		//1000밀리초는 1초니까, getTime()으로 구한 값을 1000으로 나누면 초를 얻습니다.
		long now = date2.getTime();
		
		long retM = date.getTime() - now;
		
		long sec = retM / 1000;
		System.out.println(sec);
        return sec;
	}

	
	public static String getToDay() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar1 = Calendar.getInstance(); 
		//1000밀리초는 1초니까, getTime()으로 구한 값을 1000으로 나누면 초를 얻습니다.
		String sec = dateFormat.format(calendar1.getTime());
        return sec;
	}
	
	public static long getUtcMilli() {
		ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
		return utcDateTime.toInstant().toEpochMilli();
	}
	
	public static long getDateDiff(String time, String pattern) {
	    LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		LocalDateTime dt   = LocalDateTime.parse(time, formatter);

	    long diff = ChronoUnit.DAYS.between(dt, now);
	    return diff;
	}
}
