package com.hxk.preparser;

public class WebLogPreParser {

    public static PreparsedLog parse(String line) {
        if (line.startsWith("#")){
            return null;
        } else {
            PreparsedLog preparselog = new PreparsedLog();

            String[] temps = line.split(" ");
            preparselog.setServerTime(temps[0] + " " + temps[1]);
            preparselog.setServerIp(temps[2]);
            preparselog.setMethod(temps[3]);
            preparselog.setUriStem(temps[4]);
            String queryString = temps[5];
            preparselog.setQueryString(queryString);
            String[] queryStrTemps = queryString.split("&");
            String command = queryStrTemps[1].split("=")[1];
            preparselog.setCommand(command);
            String profileIdStr = queryStrTemps[2].split("=")[1];
            preparselog.setProfileId(getProfileId(profileIdStr));
            preparselog.setServerPort(Integer.parseInt(temps[6]));
            preparselog.setClientIp(temps[8]);
            preparselog.setUserAgent(temps[9].replace("+", " "));
            String tempTime = preparselog.getServerTime().replace("-", "");
            preparselog.setDay(Integer.parseInt(tempTime.substring(0, 8)));
            preparselog.setMonth(Integer.parseInt(tempTime.substring(0, 6)));
            preparselog.setYear(Integer.parseInt(tempTime.substring(0, 4)));

            return preparselog;
        }
    }

    private static int getProfileId(String profileIdStr) {
        return Integer.valueOf(profileIdStr.substring(profileIdStr.indexOf("-") + 1));
    }
}
