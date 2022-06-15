/*
    This file is part of Bodhi Timer.

    Bodhi Timer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Bodhi Timer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Bodhi Timer.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.yuttadhammo.BodhiTimer.Util;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.yuttadhammo.BodhiTimer.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time {


    private static final String TAG = "Time";

    public static final String TIME_SEPARATOR = " again ";


    public static int msFromNumbers(int hour, int minutes, int seconds) {
        return hour * 60 * 60 * 1000 + minutes * 60 * 1000 + seconds * 1000;
    }

    public static int msFromArray(int[] numbers) {
        return msFromNumbers(numbers[0], numbers[1], numbers[2]);
    }

    public static String padWithZeroes(int number) {
        if (number > 9) {
            return Integer.toString(number);
        } else {
            return "0" + number;
        }
    }

    /**
     * Converts a millisecond time to a string time
     * Not meant to be pretty, but fast..
     *
     * @param ms the time in milliseconds
     * @return the formatted string
     */
    public static String ms2Str(int ms) {
        int[] time = time2Array(ms);

        if (time[0] == 0 && time[1] == 0) {
            return Integer.toString(time[2]);
        } else if (time[0] == 0) {
            return Integer.toString(time[1]).concat(":").concat(padWithZeroes(time[2]));
        } else {
            return Integer.toString(time[0]).concat(":").concat(padWithZeroes(time[1])).concat(":").concat(padWithZeroes(time[2]));
        }
    }

    /**
     * Creates a time vector
     *
     * @param time the time in milliseconds
     * @return [hour, minutes, seconds, ms]
     */
    public static int[] time2Array(int time) {
        int ms = time % 1000;
        int seconds = time / 1000;  // 3550000 / 1000 = 3550
        int minutes = seconds / 60; // 59.16666
        int hours = minutes / 60; // 0.9

        if (hours > 60)
            hours = 60;

        minutes = minutes % 60;
        seconds = seconds % 60;

        int[] timeVec = new int[4];
        timeVec[0] = hours;
        timeVec[1] = minutes;
        timeVec[2] = seconds;
        timeVec[3] = ms;
        return timeVec;
    }

    public static String time2humanStr(Context context, int time) {
        int[] timeVec = time2Array(time);
        int hour = timeVec[0], minutes = timeVec[1], seconds = timeVec[2];

        ArrayList<String> strList = new ArrayList<>();

        Resources res = context.getResources();

        // string formatting
        if (hour != 0) {
            strList.add(res.getQuantityString(R.plurals.x_hours, hour, hour));
        }
        if (minutes != 0) {
            strList.add(res.getQuantityString(R.plurals.x_mins, minutes, minutes));
        }
        if (seconds != 0 || (seconds >= 0 && minutes == 0 && hour == 0)) {
            strList.add(res.getQuantityString(R.plurals.x_secs, seconds, seconds));
        }

        return TextUtils.join(", ", strList);
    }


    public static String time2hms(int time) {
        return ms2Str(time);
    }

    public static String str2complexTimeString(AppCompatActivity activity, String numberString) {
        String out;

        ArrayList<String> stringArray = new ArrayList<>();


        String[] strings = numberString.split(TIME_SEPARATOR);

        for (String string : strings) {
            int atime = str2timeString(activity, string);
            if (atime > 0)
                stringArray.add(atime + "#sys_def#" + activity.getString(R.string.sys_def));
        }

        out = TextUtils.join("^", stringArray);

        return out;
    }

    public static int str2timeString(AppCompatActivity activity, String numberString) {
        final Resources res = activity.getResources();
        final String[] numbers = res.getStringArray(R.array.numbers);
        int position = 0;
        String newString = numberString;
        for (String number : numbers) {
            int num = 60 - position++;
            newString = newString.replaceAll(number, Integer.toString(num));
        }

        Pattern HOUR = Pattern.compile("([0-9]+) " + activity.getString(R.string.hour));
        Pattern MINUTE = Pattern.compile("([0-9]+) " + activity.getString(R.string.minute));
        Pattern SECOND = Pattern.compile("([0-9]+) " + activity.getString(R.string.second));

        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        Matcher m = HOUR.matcher(newString);
        while (m.find()) {
            String match = m.group(1);
            hours += Integer.parseInt(match);
        }

        m = MINUTE.matcher(newString);
        while (m.find()) {
            String match = m.group(1);
            minutes += Integer.parseInt(match);
        }

        m = SECOND.matcher(newString);
        while (m.find()) {
            String match = m.group(1);
            seconds += Integer.parseInt(match);
        }

        Log.d(TAG, "Got numbers: " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds");

        int total = hours * 60 * 60 * 1000 + minutes * 60 * 1000 + seconds * 1000;
        if (total > 60 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59 * 1000)
            total = 60 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59 * 1000;
        return total;
    }

}