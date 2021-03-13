package com.winterbe.java8.samples.time;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

/**
 * @author Benjamin Winterberg
 */
public class LocalTime1 {

    public static void main(String[] args) {

        // get the current time
        Clock clock = Clock.systemDefaultZone();
        long t0 = clock.millis();
        System.out.println(t0);     //1615674761647

        Instant instant = clock.instant();
        System.out.println(instant);    //2021-03-13T22:35:03.730Z
        Date legacyDate = Date.from(instant);
        System.out.println(legacyDate); //Sat Mar 13 17:35:03 EST 2021


        ZoneId zone1 = ZoneId.of("Europe/Berlin");
        ZoneId zone2 = ZoneId.of("Brazil/East");

        System.out.println(zone1.getRules());       //ZoneRules[currentStandardOffset=+01:00]
        System.out.println(zone2.getRules());       //ZoneRules[currentStandardOffset=-03:00]

        // time
        LocalTime now1 = LocalTime.now(zone1);
        LocalTime now2 = LocalTime.now(zone2);

        System.out.println(now1);   //23:32:41.685
        System.out.println(now2);   //19:32:41.691

        System.out.println(now1.isBefore(now2));  // false

        long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
        System.out.println(hoursBetween);   //-3

        long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);
        System.out.println(minutesBetween); //-239


        // create time

        LocalTime now = LocalTime.now();
        System.out.println(now);    //17:32:41.693

        LocalTime late = LocalTime.of(23, 59, 59);
        System.out.println(late);   //23:59:59

        DateTimeFormatter germanFormatter =
                DateTimeFormatter
                        .ofLocalizedTime(FormatStyle.SHORT)
                        .withLocale(Locale.GERMAN);

        LocalTime leetTime = LocalTime.parse("13:37", germanFormatter);
        System.out.println(leetTime);       //13:37


        // to legacy date


    }

}