package info.androidhive.fitnessreward.auxilary;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TestDataMonth {

    /**
     * Generate dump data for presentation
     * @return ArrayList<String[]> wiht dump data
     */
    public ArrayList<String[]> createTestData() {
        ArrayList<String[]> testData = new ArrayList<String[]>();
        for (int i = 0; i<31; i++) {
            int steps = 500 + (int)(Math.random() * ((10000 - 500) + 1));
            String stepsStr =Integer.toString(steps);
            String[] dataRecord = {getDate(i),stepsStr,
                    convertToDistance(stepsStr),convertToCalories(stepsStr)};
            testData.add(dataRecord);
        }
        return testData;
    }

    /**
     * Get date in String from Calendar
     * @param dayOffset - day offset in days
     * @return date(String)
     */
    private String getDate(int dayOffset) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -dayOffset);

        Date date = cal.getTime();
        return dateFormat.format(date);
    }

    /**
     * Convert steps measurements to distance
     * @param steps - fitness band measurement of steps
     * @return distance - measurement of the distance in meters
     */
    private String convertToDistance(String steps){
        double stepsInt = Double.parseDouble(steps);
        double distance = stepsInt * 0.698;
        Long L = Math.round(distance);
        int distanceInt = Integer.valueOf(L.intValue());

        return Integer.toString(distanceInt);
    }

    /**
     * Convert steps measurements to calories
     * @param steps - fitness band measurement of steps
     * @return caloeris - measurement of calories in kcal
     */
    private String convertToCalories(String steps){
        double stepsInt = Double.parseDouble(steps);
        double calories = stepsInt * 0.048;
        Log.d("DeviceService1", "convertToCalories ");
        Long L = Math.round(calories);
        int caloriesInt = Integer.valueOf(L.intValue());

        return Integer.toString(caloriesInt);
    }
}
