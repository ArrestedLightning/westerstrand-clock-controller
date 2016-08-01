/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock.controller;

/**
 *
 * @author joshualange
 */
public class ScheduleDate {

    public enum DayOfWeek {

        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY,
        SATURDAY_AND_SUNDAY,
        MONDAY_FRIDAY,
        ALL_THE_DAYS
    };

    public ScheduleDate() {
    }

    public ScheduleDate(int outputNum, int ringTime, DayOfWeek dayOfWeek, int hour, int minute) {
        this.outputNum = outputNum;
        this.ringTime = ringTime;
        this.dayOfWeek = dayOfWeek;
        this.hour = hour;
        this.minute = minute;
    }

    private int outputNum;

    public int getOutputNum() {
        return outputNum;
    }

    public void setOutputNum(int outputNum) {
        this.outputNum = outputNum;
    }

    private int ringTime;

    public int getRingTime() {
        return ringTime;
    }

    public void setRingTime(int ringTime) {
        this.ringTime = ringTime;
    }

    private DayOfWeek dayOfWeek;

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public ScheduleDate modDayOfWeek(DayOfWeek dayOfWeek) {

        ScheduleDate sd = new ScheduleDate(getOutputNum(), getRingTime(), dayOfWeek, getHour(), getMinute());
        return sd;
    }

    private int hour;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    private int minute;

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public static DayOfWeek dayOfWeekForString(String dayString) {
        int day = Integer.parseInt(dayString, 16);
        DayOfWeek weekday;
        switch (day) {
            case 0x4001:
                //Monday
                weekday = DayOfWeek.MONDAY;
                break;
            case 0x2002:
                //Tuesday
                weekday = DayOfWeek.TUESDAY;
                break;
            case 0x1003:
                //Wednesday
                weekday = DayOfWeek.WEDNESDAY;
                break;
            case 0x0804:
                //Thursday
                weekday = DayOfWeek.THURSDAY;
                break;
            case 0x0405:
                //Friday
                weekday = DayOfWeek.FRIDAY;
                break;
            case 0x0206:
                //Saturday
                weekday = DayOfWeek.SATURDAY;
                break;
            case 0x0107:
                //Sunday
                weekday = DayOfWeek.SUNDAY;
                break;
            case 0x0300:
                //Saturday & Sunday
                weekday = DayOfWeek.SATURDAY_AND_SUNDAY;
                break;
            case 0x7C08:
                //Monday-Friday
                weekday = DayOfWeek.MONDAY_FRIDAY;
                break;
            case 0x7F09:
                //Every Day
                weekday = DayOfWeek.ALL_THE_DAYS;
                break;
            default:
                throw new AssertionError();
        }
        return weekday;
    }

    public static String StringForDayOfWeek(DayOfWeek day) {
        switch (day) {
            case MONDAY:
                return "4001";
            case TUESDAY:
                return "2002";
            case WEDNESDAY:
                return "1003";
            case THURSDAY:
                return "0804";
            case FRIDAY:
                return "0405";
            case SATURDAY:
                return "0206";
            case SUNDAY:
                return "0107";
            case SATURDAY_AND_SUNDAY:
                return "0300";
            case MONDAY_FRIDAY:
                return "7C08";
            case ALL_THE_DAYS:
                return "7F09";
            default:
                throw new AssertionError();
        }
    }
}
