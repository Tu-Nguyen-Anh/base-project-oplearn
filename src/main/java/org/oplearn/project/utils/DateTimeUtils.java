package org.oplearn.project.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
public class DateTimeUtils {

    public static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";
    public static final String DEFAULT_DATE_TIME_PATTERN = "hh:mm dd/MM/yyyy";
    public static final String CLIENT_DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_PATTERN_FOR_COMPARISON = "yyyyMMdd";
    public static final String DATE_TIME_WITH_MILLI_SECOND = "yyyyMMdd_HH:mm:ss.SSS";
    public static final String DATE_WITH_SLASH_AND_HOUR_MINUTES = "dd/MM/yyyy HH:mm";
    public static final String DATE_WITH_SLASH_AND_HOUR_MINUTES_SECOND = "dd/MM/yyyy HH:mm:ss";

    private DateTimeUtils() {
    }

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(ZoneId.of("VST", ZoneId.SHORT_IDS));
    }

    public static LocalDate toLocalDate(String source) {
        return isNotEmpty(source)
                ? safeExecute(DateTimeUtils::toLocalDate, source, DEFAULT_DATE_PATTERN, null)
                : null;
    }

    public static LocalDate toLocalDate(String source, String pattern) {
        return isNotEmpty(source) && isNotEmpty(pattern)
                ? safeExecute(LocalDate::parse, source, DateTimeFormatter.ofPattern(pattern), null)
                : null;
    }

    public static LocalTime toLocalTime(String source, String pattern) {
        return isNotEmpty(source) && isNotEmpty(pattern)
                ? safeExecute(LocalTime::parse, source, DateTimeFormatter.ofPattern(pattern), null)
                : null;
    }

    public static LocalDateTime toLocalDateTime(String source) {
        return isNotEmpty(source)
                ? safeExecute(DateTimeUtils::toLocalDateTime, source, DEFAULT_DATE_TIME_PATTERN, null)
                : null;
    }

    public static LocalDateTime toLocalDateTime(String source, String pattern) {
        return isNotEmpty(source) && isNotEmpty(pattern)
                ? safeExecute(LocalDateTime::parse, source, DateTimeFormatter.ofPattern(pattern), null)
                : null;
    }

    public static LocalDate toLocalDateFromSecond(Long second) {
        return isNotEmpty(second)
                ? Instant.ofEpochSecond(second)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                : null;
    }

    public static LocalDate toLocalDateFromMilli(Long millisecond) {
        return isNotEmpty(millisecond)
                ? Instant.ofEpochMilli(millisecond)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                : null;
    }

    public static LocalDateTime toLocalDateTimeFromSecond(Long second) {
        return isNotEmpty(second)
                ? Instant.ofEpochSecond(second)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                : null;
    }

    public static LocalDateTime toLocalDateTimeFromMilli(Long millisecond) {
        return isNotEmpty(millisecond)
                ? Instant.ofEpochMilli(millisecond)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                : null;
    }

    public static String format(LocalDate localDate) {
        return isNotEmpty(localDate)
                ? format(localDate, DEFAULT_DATE_PATTERN)
                : "";
    }

    public static String format(LocalDate localDate, String pattern) {
        return isNotEmpty(localDate) && isNotEmpty(pattern)
                ? localDate.format(DateTimeFormatter.ofPattern(pattern))
                : "";
    }

    public static String format(LocalDateTime localDateTime) {
        return isNotEmpty(localDateTime)
                ? localDateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN))
                : "";
    }

    public static String format(LocalDateTime localDateTime, String pattern) {
        return isNotEmpty(localDateTime) && isNotEmpty(pattern)
                ? localDateTime.format(DateTimeFormatter.ofPattern(pattern))
                : "";
    }

    public static String format(ZonedDateTime zonedDateTime, String pattern) {
        return isNotEmpty(zonedDateTime) && isNotEmpty(pattern)
                ? zonedDateTime.format(DateTimeFormatter.ofPattern(pattern))
                : "";
    }

    public static String format(Timestamp timestamp) {
        return isNotEmpty(timestamp)
                ? format(timestamp, DEFAULT_DATE_TIME_PATTERN)
                : "";
    }

    public static String format(Timestamp timestamp, String pattern) {
        return isNotEmpty(timestamp) && isNotEmpty(pattern)
                ? timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern(pattern))
                : "";
    }

    public static Long toMilli(LocalDate localDate) {
        return isNotEmpty(localDate)
                ? ZonedDateTime.of(localDate, LocalTime.MIN, ZoneId.systemDefault())
                .toInstant().toEpochMilli()
                : null;
    }

    public static Long toMilli(LocalDateTime localDateTime) {
        return isNotEmpty(localDateTime)
                ? ZonedDateTime.of(localDateTime, ZoneId.systemDefault())
                .toInstant().toEpochMilli()
                : null;
    }

    public static Long toSecond(LocalDate localDate) {
        return isNotEmpty(localDate)
                ? ZonedDateTime.of(localDate, LocalTime.MIN, ZoneId.systemDefault())
                .toInstant().getEpochSecond()
                : null;
    }

    public static Long toSecond(LocalDateTime localDateTime) {
        return isNotEmpty(localDateTime)
                ? ZonedDateTime.of(localDateTime, ZoneId.systemDefault())
                .toInstant().getEpochSecond()
                : null;
    }

    public static Long atEndOfDayInMilli(LocalDate atDate) {
        return isNotEmpty(atDate)
                ? toMilli(LocalDateTime.of(atDate, LocalTime.MAX))
                : null;
    }

    public static Long atEndOfDayInMilli(LocalDateTime atDateTime) {
        return isNotEmpty(atDateTime)
                ? toSecond(LocalDateTime.of(atDateTime.toLocalDate(), LocalTime.MAX))
                : null;
    }

    public static Long getCurrentSecond() {
        return toSecond(LocalDateTime.now());
    }

    public static Long getCurrentMillisecond() {
        return toMilli(LocalDateTime.now());
    }

    public static boolean isValid(String source, String pattern) {
        return toLocalDate(source, pattern) != null || toLocalDateTime(source, pattern) != null;
    }

    public static boolean isInvalid(String source, String pattern) {
        return !isValid(source, pattern);
    }


    // ---- private support methods ---- //

    private static boolean isEmpty(Object obj) {
        return Objects.isNull(obj);
    }

    private static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    private static <T, R> R safeExecute(Function<T, R> func, T param, R orElseReturn) {
        try {
            return func.apply(param);
        }
        catch (Exception e) {
            return orElseReturn;
        }
    }

    private static <T, U, R> R safeExecute(BiFunction<T, U, R> func, T param1, U param2, R orElseReturn) {
        try {
            return func.apply(param1, param2);
        }
        catch (Exception e) {
            return orElseReturn;
        }
    }

    public static LocalDateTime toLocalDateTime(ZonedDateTime zonedDateTime) {
        if (Objects.isNull(zonedDateTime)) return null;
        return zonedDateTime.toLocalDateTime();
    }

    public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) return null;
        return localDateTime.atZone(ZoneId.systemDefault());
    }


    public static String getDayFromddMMyyyy(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        Date date;
        try {
            date = dateFormat.parse(dateString);
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            return dayFormat.format(date);
        }
        catch (ParseException e) {
            log.error("[ERROR] An error has occurred when converting dateString to Date.", e);
            return null;
        }
    }

    public static String getMonthFromddMMyyyy(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        Date date;
        try {
            date = dateFormat.parse(dateString);
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            return monthFormat.format(date);
        }
        catch (ParseException e) {
            log.error("[ERROR] An error has occurred when converting dateString to Date.", e);
            return null;
        }
    }

    public static String getYearFromddMMyyyy(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        Date date;
        try {
            date = dateFormat.parse(dateString);
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            return yearFormat.format(date);
        }
        catch (ParseException e) {
            log.error("[ERROR] An error has occurred when converting dateString to Date.", e);
            return null;
        }
    }
}