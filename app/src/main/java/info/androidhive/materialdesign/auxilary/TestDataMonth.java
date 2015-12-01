package info.androidhive.materialdesign.auxilary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.Locale;
import java.util.Queue;

import info.androidhive.materialdesign.activity.DataBase;

/**
 * Created by rshir on 30.11.2015.
 */
public class TestDataMonth {
    private Cursor cursor;
    private DataBase mDbHelper;
    private SQLiteDatabase mDb;

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
        Long L = Math.round(calories);
        int caloriesInt = Integer.valueOf(L.intValue());

        return Integer.toString(caloriesInt);
    }

    private String[] parceDataString(String dataString) {
        String delims="[;]";
        String[] parcedData = dataString.split(delims);
        return parcedData;
    }

    public static void main(String args[]){
        TestDataMonth formData = new TestDataMonth();
        ArrayList<String[]> testData = formData.createTestData();
        for(String[] testDay : testData){
            String date = testDay[0];
            String steps = testDay[1];
            String distance = testDay[2];
            String calories = testDay[3];
            System.out.println(
                    "Date:" + date + "\n" +
                            "Steps:" + steps + "\n" +
                            "Distance: " + distance + "\n" +
                            "Calories: " + calories);
        }
    }
}
