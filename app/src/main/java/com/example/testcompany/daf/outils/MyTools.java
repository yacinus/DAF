package com.example.testcompany.daf.outils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public abstract class MyTools {

    //Sat Jul 20 01:32:23 GMT+00:00 2019
    /**
     * Conversion chaine sous forme Tue Jun 25 15:32:18 GMT 2019 en date
     * @param uneDate
     * @return
     */
    public static Date convertStringToDate(String uneDate) {
        String formatAttendu = "EEE MMM dd hh:mm:ss 'GMT' yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(formatAttendu);
        try {
            return formatter.parse(uneDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date convertStringToDateLocal(String uneDate) {
        String formatAttendu = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(formatAttendu);
        try {
            return formatter.parse(uneDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date convertStringToDateWithoutTime(String uneDate){
        String formatAttendu = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(formatAttendu);
        try {
            return formatter.parse(uneDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Conversion d'une date en chaine
     * @param uneDate
     * @return
     */
    public static String convertDateToString(Date uneDate){
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
        return date.format(uneDate);
    }

    public static String convertDateToStringWtime(Date uneDate){
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        return date.format(uneDate);
    }

    public static String removeTime(String date){
       StringBuffer sb = new StringBuffer(date);

       for (int i=10;i<sb.length();i++){
           sb.deleteCharAt(i);
           i--;
       }

       date = sb.toString();
       return date;
    }

    public static int convertDateToInteger(){
        int dateInteger;
        Calendar calendar = new GregorianCalendar();
        String hour = String.valueOf(calendar.get(Calendar.HOUR));        // 12 hour clock
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String second = String.valueOf(calendar.get(Calendar.SECOND));
        dateInteger = Integer.parseInt(hour.concat(minute).concat(second));
        return dateInteger;
    }

    // Function to convert ArrayList<String> to String[]
    public static String[] GetStringArray(ArrayList<String> arr)
    {

        // declaration and initialise String Array
        String[] str = new String[arr.size()];

        // ArrayList to Array Conversion
        for (int j = 0; j < arr.size(); j++) {

            // Assign each value to String array
            str[j] = arr.get(j);
        }

        return str;
    }

    public static String convertStreamToString(InputStream is) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

}

