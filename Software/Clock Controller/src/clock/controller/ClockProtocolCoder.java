/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock.controller;

import clock.controller.ScheduleDate.DayOfWeek;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joshualange
 */
public class ClockProtocolCoder implements ClockDataListener {

    private ClockConnection theConnection;
    private ScheduleEventListener theScheduleListener;
    private DateTimeListener theDateListener;

    public ClockConnection getTheConnection() {
        return theConnection;
    }

    public void setTheConnection(ClockConnection theConnection) {
        this.theConnection = theConnection;
    }

    public ScheduleEventListener getTheScheduleListener() {
        return theScheduleListener;
    }

    public void setTheScheduleListener(ScheduleEventListener theScheduleListener) {
        this.theScheduleListener = theScheduleListener;
    }

    public DateTimeListener getTheDateListener() {
        return theDateListener;
    }

    public void setTheDateListener(DateTimeListener theDateListener) {
        this.theDateListener = theDateListener;
    }

    private ClockProtocolCoder() {

    }

    public ClockProtocolCoder(ClockConnection theConnection) {
        this.theConnection = theConnection;
        if (theConnection != null) {
            theConnection.setReadHandler(this);
        }
    }

    public void setClockConnection(ClockConnection theConnection) {
        this.theConnection = theConnection;
    }

    public boolean requestCurrentDateFromClock() {
        //U=0x55
        //8=0x38
        return this.sendClockMessage((byte) 0x55, (byte) 0x38, null);
    }

    public boolean setCurrentDateOnClock(Date theDate) {
        try {
            byte[] messageData;
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
            messageData = sdf.format(theDate).getBytes("US-ASCII");
            //U=0x55
            //1=0x31
            return this.sendClockMessage((byte) 0x55, (byte) 0x31, messageData);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClockProtocolCoder.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean requestCurrentOffsetFromClock(int systemNumber) {
        switch (systemNumber) {
            case 1:
                //U=0x55
                //B=0x42
                return this.sendClockMessage((byte) 0x55, (byte) 0x42, null);

            case 2:
                //U=0x55
                //C=0x43
                return this.sendClockMessage((byte) 0x55, (byte) 0x43, null);

            default:
                return false;
        }
    }

    public boolean setClockOffset(int systemNumber, Date offset) {
        try {
            byte[] messageData;
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
            messageData = sdf.format(offset).getBytes("US-ASCII");

            switch (systemNumber) {
                case 1:
                    //U=0x55
                    //2=0x32
                    return this.sendClockMessage((byte) 0x55, (byte) 0x32, messageData);

                case 2:
                    //U=0x55
                    //3=0x33
                    return this.sendClockMessage((byte) 0x55, (byte) 0x33, messageData);

                default:
                    return false;
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClockProtocolCoder.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean requestFirstScheduleProgramFromClock() {
        //W=0x57
        //0=0x30
        return this.sendClockMessage((byte) 0x57, (byte) 0x30, null);
    }

    public boolean requestNextScheduleProgramFromClock() {
        //W=0x57
        //N=0x4E
        return this.sendClockMessage((byte) 0x57, (byte) 0x4E, null);
    }

    public boolean deleteAllSchedulePrograms() {
        //W=0x57
        //D=0x44
        return this.sendClockMessage((byte) 0x57, (byte) 0x44, null);
    }

    public boolean sendScheduleProgram(int id, int hour, int minute,
            DayOfWeek dayOfWeek, int bellNumber, int bellDuration) {
        byte[] programData = new byte[18];
        byte dayNum;
        //program ID not used
        //explicitly ignore by clock per documentation
        programData[0] = 0x30;
        programData[1] = 0x30;
        programData[2] = 0x30;
        programData[3] = 0x30;

        //Program schedule settings - constant
        programData[4] = 0x38;
        programData[5] = 0x30;
        programData[6] = 0x30;

        //bell output number
        programData[7] = getASCIIHexBytes(bellNumber, 1)[0];

        //bell duration
        bellDuration = (bellDuration > 255) ? 255 : (bellDuration < 0) ? 0 : bellDuration;
        byte[] bellDurationBytes = getASCIIHexBytes(bellDuration, 2);
        programData[8] = bellDurationBytes[0];
        programData[9] = bellDurationBytes[1];

        //day of week bitmask
        switch (dayOfWeek) {
            case SATURDAY_AND_SUNDAY:
                //0300
                programData[10] = 0x30;
                programData[11] = 0x33;
                dayNum = 0;
                break;
            case MONDAY:
                //4001
                programData[10] = 0x34;
                programData[11] = 0x30;
                dayNum = 1;
                break;
            case TUESDAY:
                //2002
                programData[10] = 0x32;
                programData[11] = 0x30;
                dayNum = 2;
                break;
            case WEDNESDAY:
                //1003
                programData[10] = 0x31;
                programData[11] = 0x30;
                dayNum = 3;
                break;
            case THURSDAY:
                //0804
                programData[10] = 0x30;
                programData[11] = 0x38;
                dayNum = 4;
                break;
            case FRIDAY:
                //0405
                programData[10] = 0x30;
                programData[11] = 0x34;
                dayNum = 5;
                break;
            case SATURDAY:
                //0206
                programData[10] = 0x30;
                programData[11] = 0x32;
                dayNum = 6;
                break;
            case SUNDAY:
                //0107
                programData[10] = 0x30;
                programData[11] = 0x31;
                dayNum = 7;
                break;
            case MONDAY_FRIDAY:
                //7C08
                programData[10] = 0x37;
                programData[11] = 0x43;
                dayNum = 8;
                break;
            case ALL_THE_DAYS:
                //7F09
                programData[10] = 0x37;
                programData[11] = 0x46;
                dayNum = 9;
                break;
            default:
                return false;
        }
        //day of week number
        byte[] dayOfWeekBytes = getASCIIHexBytes(dayNum, 2);
        programData[12] = dayOfWeekBytes[0];
        programData[13] = dayOfWeekBytes[1];

        //hour
        byte[] hourBytes = getASCIIHexBytes(hour, 2);
        programData[14] = hourBytes[0];
        programData[15] = hourBytes[1];

        //minute
        byte[] minuteBytes = getASCIIHexBytes(minute, 2);
        programData[16] = minuteBytes[0];
        programData[17] = minuteBytes[1];

        //W=0x57
        //w=0x57
        return this.sendClockMessage((byte) 0x57, (byte) 0x57, programData);
    }

    //must be an array of 8 or fewer boolean values
    public boolean setOutputs(boolean[] outputs) {
        byte[] messageData = new byte[16];
        int outputsInfo = 0x00;
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i]) {
                int tmp = 1 << (7 - i);
                outputsInfo |= tmp;
            }

        }
        byte[] outputBytes = getASCIIHexBytes(outputsInfo, 2);
        messageData[0] = outputBytes[0];
        messageData[1] = outputBytes[1];
        for (int i = 2; i < messageData.length; i++) {
            messageData[i] = 0x30;
        }
        //R=0x52
        //1=0x31
        return this.sendClockMessage((byte) 0x52, (byte) 0x31, messageData);
    }

    @Override
    public void dataReceived(byte[] data) {
        //sanity checks
        if (data.length < 7) {
            return;
        }
        if (data[0] != 0x02) {
            return;
        }

        if (verifyChecksum(data)) {
            //switch on field 3 (RT byte)
            switch (data[3]) {
                case 0x44://D=Display
                    switch (data[4]) {//Switch on RTM Byte
                        case 0x31://1=Display Contents
                            //message contains display contents
                            break;
                        default:
                            //any other messages shouldn't happen
                            //return silently
                            return;
                    }
                    break;
                case 0x52://R=Outputs
                    switch (data[4]) {//Switch on RTM Byte
                        case 0x31://1=Output Status
                            //message contains output status
                            break;
                        default:
                            //any other messages shouldn't happen
                            //return silently
                            return;
                    }
                    break;
                case 0x55://U=Time, impulse time
                    if (theDateListener != null) {
                        switch (data[4]) {//switch on RTM Byte
                            case 0x31://1=System Time
                                Date theDate = parseClockDate(data);
                                theDateListener.clockDateReceived(theDate);
                                break;
                            case 0x32://2=Impulse 1 Time
                                Date theDate2 = parseClockDate(data);
                                theDateListener.clockOffsetReceived(1, theDate2);
                                break;
                            case 0x33://3=Impulse 2 Time
                                Date theDate3 = parseClockDate(data);
                                theDateListener.clockOffsetReceived(2, theDate3);
                                break;
                            default:
                                //any other messages shouldn't happen
                                //return silently
                                return;
                        }
                    }
                    break;

                case 0x57://W=Week/Day Programs
                    if (theScheduleListener != null) {
                        switch (data[4]) {
                            case 0x39://9=Last Program Requested
                                theScheduleListener.lastEventReceived();
                                break;
                            case 0x57://W=Program Received
                                int id = parseID(data);
                                ScheduleDate theDate = parseDate(data);
                                theScheduleListener.eventReceived(id, theDate);
                                break;
                            default:
                                //any other messages shouldn't happen
                                //return silently
                                return;
                        }
                    }
                    break;
                default:
                    throw new AssertionError();
            }
        }

    }

    //returns true if the checksum is valid
    public boolean verifyChecksum(byte[] data) {
        //skip the checksum byte when verifying
        int newck, oldck;
        newck = generateChecksum(data, data.length - 1);
        oldck = data[data.length - 1];
        System.out.println("Got Checksum: " + oldck + " Expected Checksum: " + newck);
        return newck == oldck;
    }

    public byte generateChecksum(byte[] data) {
        return generateChecksum(data, data.length);
    }

    public byte generateChecksum(byte[] data, int length) {
        byte checksum = 0;
        if (length > data.length) {
            length = data.length;
        }
        for (int i = 1; i < length; i++) {
            checksum = (byte) (data[i] ^ checksum);
        }
        return checksum;
    }

    private boolean sendClockMessage(byte RT, byte RTM, byte[] messageData) {
        if (theConnection != null && theConnection.isConnected()) {
            int totalMessageLength;
            if (messageData == null) {
                totalMessageLength = 7;
            } else {
                totalMessageLength = messageData.length + 7;
            }
            byte dataToSend[] = new byte[totalMessageLength];
            dataToSend[0] = 0x02;
            dataToSend[1] = 0x41;
            dataToSend[2] = 0x41;
            dataToSend[3] = RT;
            dataToSend[4] = RTM;
            if (messageData != null) {
                System.arraycopy(messageData, 0,
                        dataToSend, 5, messageData.length);
            }
            dataToSend[dataToSend.length - 2] = 0x03;
            dataToSend[dataToSend.length - 1] = generateChecksum(dataToSend, dataToSend.length - 1);//fixme
            return theConnection.writeBytes(dataToSend);
        }
        return false;
    }

//converts a number to the equivalent number in ASCII as hexadecimal
    private byte[] getASCIIHexBytes(int number, int length) {
        try {
            byte[] result = new byte[length];
            String s = Integer.toHexString(number).toUpperCase();
            byte[] hexFormatTmp = s.getBytes("US-ASCII");
            if (hexFormatTmp.length >= length) {
                return hexFormatTmp;
            }
            System.arraycopy(hexFormatTmp, 0, result, length - hexFormatTmp.length, hexFormatTmp.length);

            for (int i = 0; i < (length - hexFormatTmp.length); i++) {
                result[i] = 0x30;
            }
            return result;
        } catch (UnsupportedEncodingException ex) {

            return null;
        }
    }

    private ScheduleDate parseDate(byte[] data) {
        //sanity checks
        if (data.length > 7) {
            if (data[3] == 0x57) {//W
                char[] dateStringArray = new char[14];
                //manually copy part of the array
                //bytes 9-22 are date string
                for (int i = 0; i < 14; i++) {
                    dateStringArray[i] = (char) data[i + 9];
                }
                String dateString = String.copyValueOf(dateStringArray);
                int outNum = Integer.parseInt(dateString.substring(3, 4), 16);//output number
                int length = Integer.parseInt(dateString.substring(4, 6), 16);
                int hour = Integer.parseInt(dateString.substring(10, 12), 16);
                int minute = Integer.parseInt(dateString.substring(12, 14), 16);
                DayOfWeek weekday;
                String ds = dateString.substring(6, 10);
                weekday = ScheduleDate.dayOfWeekForString(ds);
                return new ScheduleDate(outNum, length, weekday, hour, minute);
            }
        }
        return null;
    }

    private Date parseClockDate(byte[] data) {
        if (data.length > 7) {
            if (data[3] == 0x55) {
                try {
                    //U
                    SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
                    char[] textString = new char[12];
                    for (int i = 5; i < 17; i++) {
                        textString[i - 5] = (char) data[i];
                    }
                    String dateString = String.copyValueOf(textString);
                    System.out.println(dateString);
                    Date dd = sdf.parse(dateString);
                    return dd;
                } catch (ParseException ex) {
                    Logger.getLogger(ClockProtocolCoder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    private int parseID(byte[] data) {
        //bytes 5,6,7,8 are ID in Decimal ASCII
        char[] id = new char[4];
        for (int i = 0; i < 4; i++) {
            id[i] = (char) data[i + 5];

        }
        String idStr = String.valueOf(id);
        return Integer.parseInt(idStr);
    }

}
