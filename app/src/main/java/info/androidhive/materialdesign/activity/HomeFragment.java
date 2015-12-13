package info.androidhive.materialdesign.activity;

/**
 * Created by Daria, Roma, Alper
 */
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.auxilary.TestDataMonth;
import info.androidhive.materialdesign.activity.StatsFragment;
import info.androidhive.materialdesign.background.DeviceService;
import info.androidhive.materialdesign.background.UpdateData;

public class HomeFragment extends Fragment {
    private DataBase mDbHelper;
    private Cursor cursor;
    private SQLiteDatabase mDb;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDatabaseForData();
        //Intent service = new Intent(getActivity(), DeviceService.class);
        //getActivity().startService(service);
        //getActivity().stopService(service);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


//--------------------------------------------------------------------------------------------------
//create database if doesnt exist
        String check = ReadPersonalName();
        if (check == "")
        {
            mDbHelper.createNewTodo("Enter your name","0","0","0","no diet","nobody");
        }
//--------------------------------------------------------------------------------------------------
//TextView, Buttons declaration
        TextView recommendation= (TextView)rootView.findViewById(R.id.MOVE);
        TextView earliest_goal = (TextView)rootView.findViewById(R.id.earliest_goalDate);
        final TextView data_home_distance = (TextView)rootView.findViewById(R.id.data_Distance);
        final TextView data_home_calories= (TextView)rootView.findViewById(R.id.data_Calories);
        final TextView data_home_steps= (TextView)rootView.findViewById(R.id.data_Steps);
        TextView diete_Time = (TextView)rootView.findViewById(R.id.diete_Time);
        TextView earliest_goal_number = (TextView)rootView.findViewById(R.id.earliest_goalNumber);
        TextView earliest_goal_text = (TextView)rootView.findViewById(R.id.earliest_goalDate);
        TextView earliest_goal_left = (TextView)rootView.findViewById(R.id.earliest_goalLeft);
        TextView earliest_goalSteps = (TextView)rootView.findViewById(R.id.earliest_goalSteps);
        TextView reward_text = (TextView)rootView.findViewById(R.id.reward_text);
        ImageButton mImageButton = (ImageButton) rootView.findViewById(R.id.imageButton);
        ImageView imgDish= (ImageView) rootView.findViewById(R.id.imageDish);
        ImageView imgReward= (ImageView) rootView.findViewById(R.id.imageReward);

//--------------------------------------------------------------------------------------------------
//Setting the initial data
        earliest_goal.setText("You don't have a goal yet.");
        imgReward.setImageResource(R.drawable.nothing);
        reward_text.setText(R.string.Reward0);
        earliest_goalSteps.setText("Steps");

//--------------------------------------------------------------------------------------------------
//Time and Date
        Date d = new Date();
        CharSequence time = DateFormat.format("yyyy-MM-dd", d.getTime());
        CharSequence date = DateFormat.format("hh:mm", d.getTime());
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
//--------------------------------------------------------------------------------------------------
//Read personal Data
        String namePerson   = ReadPersonalName();
        String agePerson    = ReadPersonalAge();
        String weightPerson = ReadPersonalWeight();
        String heihtPerson  = ReadPersonalHeiht();
        String lookPerson   = ReadPersonalLook();
//--------------------------------------------------------------------------------------------------
// Diet
        int diet_number = 0;
        switch (showDiete()) {
            case ("No diet"):
            {
                diet_number = 1;
                break;
            }
            case ("The Flat Belly Diet"):
            {
                diet_number = 2;
                break;
            }
            case ("The Fast Food Diet"):
            {
                diet_number = 3;
                break;
            }
            case ("The Grapefruit Diet"):
            {
                diet_number = 4;
                break;
            }
        }

        int hour_diet = diet_number*100+hour;
        int index = mod(day,3);
        int[] image_greip_break = new int[] {R.drawable.greip_break1, R.drawable.greip_break2, R.drawable.greip_break3};
        int[] image_greip_lunch = new int[] {R.drawable.greip_launch1, R.drawable.greip_launch2, R.drawable.greip_launch3};
        int[] image_greip_dinner = new int[] {R.drawable.greip_dinner1, R.drawable.greip_dinner2, R.drawable.greip_dinner3};

        int[] image_normal_break = new int[] {R.drawable.break1, R.drawable.break2, R.drawable.break3};
        int[] image_normal_lunch = new int[] {R.drawable.launch1, R.drawable.launch2, R.drawable.launch3};
        int[] image_normal_dinner = new int[] {R.drawable.dinner1, R.drawable.dinner2, R.drawable.dinner3};

        int[] image_fat_break = new int[] {R.drawable.fat_break1, R.drawable.fat_break2, R.drawable.fat_break3};
        int[] image_fat_lunch = new int[] {R.drawable.fat_lunch1, R.drawable.fat_lunch2, R.drawable.fat_lunch3};
        int[] image_fat_dinner = new int[] {R.drawable.fat_dinner1, R.drawable.fat_dinner2, R.drawable.fat_dinner3};

        int[] image_fast_break = new int[] {R.drawable.fast_break1, R.drawable.fast_break2, R.drawable.fast_break3};
        int[] image_fast_lunch = new int[] {R.drawable.fast_lunch1, R.drawable.fast_lunch2, R.drawable.fast_lunch3};
        int[] image_fast_dinner = new int[] {R.drawable.fast_dinner1, R.drawable.fast_dinner2, R.drawable.fast_dinner3};

//--------------------------------------------------------------------------------------------------
//Switching recommendations from 8 till 22 * 4
        switch (hour_diet) {
            //Breakfast time
            case (108):
            case (109):
                diete_Time.setText("It's time to eat!");
                recommendation.setText(R.string.StringNoDiet1);
                imgDish.setImageResource(image_normal_break[index]);
                break;
            case (208):
            case (209):
                diete_Time.setText("It's time to eat!");
                recommendation.setText(R.string.StringFatDiet1);
                imgDish.setImageResource(image_fat_break[index]);;
                break;
            case (308):
            case (309):
                diete_Time.setText("It's time to eat!");
                recommendation.setText(R.string.StringFastDiet1);
                imgDish.setImageResource(image_fast_break[index]);
                break;
            case (408):
            case (409):
                diete_Time.setText("It's time to eat!");
                recommendation.setText(R.string.StringGreipDiet1);
                imgDish.setImageResource(image_greip_break[index]);
                break;

            //Break time
            case (110):
            case (210):
            case (310):
            case (410):
                recommendation.setText(R.string.string3);
                imgDish.setImageResource(R.drawable.tom);
                break;
            case (111):
            case (211):
            case (311):
            case (411):
                recommendation.setText(R.string.string3);
                imgDish.setImageResource(R.drawable.tom);
                break;

            //Lunch time
            case (112):
            case (113):
                diete_Time.setText("It's time to eat!");
                recommendation.setText(R.string.StringNoDiet2);
                imgDish.setImageResource(image_normal_lunch[index]);
                break;
            case (212):
            case (213):
                diete_Time.setText("It's time to eat!");
                recommendation.setText(R.string.StringFatDiet2);
                imgDish.setImageResource(image_fat_lunch[index]);
                break;
            case (312):
            case (313):
                diete_Time.setText("It's time to eat!");
                recommendation.setText(R.string.StringFastDiet2);
                imgDish.setImageResource(image_fast_lunch[index]);
                break;
            case (412):
            case (413):
                diete_Time.setText("It's time to eat!");
                recommendation.setText(R.string.StringGreipDiet2);
                imgDish.setImageResource(image_greip_lunch[index]);
                break;

            //Break time
            case (114):
            case (115):
            case (116):
            case (117):
                recommendation.setText(R.string.string3);
                imgDish.setImageResource(R.drawable.tom);
                break;
            case (214):
            case (215):
            case (216):
            case (217):
                recommendation.setText(R.string.string3);
                imgDish.setImageResource(R.drawable.tom);
                break;
            case (314):
            case (315):
            case (316):
            case (317):
                recommendation.setText(R.string.string3);
                imgDish.setImageResource(R.drawable.tom);
                break;
            case (414):
            case (415):
            case (416):
            case (417):
                recommendation.setText(R.string.string3);
                imgDish.setImageResource(R.drawable.tom);
                break;

            //Dinner time
            case (118):
            case (119):
                diete_Time.setText("It's time to eat!");
                recommendation.setText(R.string.StringNoDiet3);
                imgDish.setImageResource(image_normal_dinner[index]);
                break;
            case (218):
            case (219):
                diete_Time.setText("It's time to eat!");
                recommendation.setText(R.string.StringFatDiet3);
                imgDish.setImageResource(image_fat_dinner[index]);
                break;
            case (318):
            case (319):
                diete_Time.setText("It's time to eat!");
                recommendation.setText(R.string.StringFastDiet3);
                imgDish.setImageResource(image_fast_dinner[index]);
                break;
            case (418):
            case (419):
                diete_Time.setText("It's time to eat!");
                recommendation.setText(R.string.StringGreipDiet3);
                imgDish.setImageResource(image_greip_dinner[index]);
                break;

            //AfterDinner time
            case (120):
            case (121):
            case (122):
            case (123):
                diete_Time.setText("It's rest time!");
                recommendation.setText(R.string.AfterDinner1);
                imgDish.setImageResource(R.drawable.sleep);
                break;
            case (220):
            case (221):
            case (222):
            case (223):
                diete_Time.setText("It's rest time!");
                recommendation.setText(R.string.AfterDinner1);
                imgDish.setImageResource(R.drawable.sleep);
                break;
            case (320):
            case (321):
            case (322):
            case (323):
                diete_Time.setText("It's rest time!");
                recommendation.setText(R.string.AfterDinner3);
                imgDish.setImageResource(R.drawable.sleep);
                break;
            case (420):
            case (421):
            case (422):
            case (423):
                diete_Time.setText("It's rest time!");
                recommendation.setText(R.string.AfterDinner4);
                imgDish.setImageResource(R.drawable.sleep);
                break;

            default:
                diete_Time.setText("No Data Danger!!!");
                recommendation.setText("");
                imgDish.setImageResource(R.drawable.ben);
                break;
        }

//--------------------------------------------------------------------------------------------------
//Show last recorded data
        String data_today = showLastDataRecord();
        data_home_distance.setText(data_today);
        String data_today_distance = data_today.substring(data_today.lastIndexOf("Distance: "), (data_today.indexOf("Calories: ")-1));
        data_home_distance.setText(data_today_distance);
        String data_today_calories = data_today.substring(data_today.lastIndexOf("Calories: "), data_today.length());
        data_home_calories.setText(data_today_calories);
        String data_today_steps = data_today.substring(0, data_today.lastIndexOf("Distance: "));
        data_home_steps.setText(data_today_steps);


//--------------------------------------------------------------------------------------------------
// Goal
        if(!showEarliestGoal().equals(""))
        {

            String earliest_goal_data = showEarliestGoal();
            String earliest_goal_data_day0 = earliest_goal_data.substring(earliest_goal_data.lastIndexOf("Date:") + 5, earliest_goal_data.lastIndexOf(" Steps:") - 3);
            String earliest_goal_data_day = "Till " +  earliest_goal_data_day0;
            String earliest_goal_data_steps = earliest_goal_data.substring(earliest_goal_data.lastIndexOf(" Steps:") + 7, earliest_goal_data.length());

            int days_left = Integer.parseInt(earliest_goal_data_day0.substring(0,2))-day;
            int hours_left = abs(Integer.parseInt(earliest_goal_data_day0.substring(11, 12)) - hour);
            int minutes_left = abs(Integer.parseInt(earliest_goal_data_day0.substring(14, 15)) - minute);
            earliest_goal_left.setText("You have " + String.valueOf(days_left) + " days, " + String.valueOf(hours_left)+ "hours, " + String.valueOf(minutes_left) + "minutes");
            earliest_goal_number.setText(earliest_goal_data_steps);
            earliest_goal_text.setText(earliest_goal_data_day);

//--------------------------------------------------------------------------------------------------
// Reward

            if (days_left == 0)
            {
                String count_today_steps = data_today_steps.substring(6, data_today.lastIndexOf("Distance: ")-1);
                int count_steps = Integer.valueOf(count_today_steps);
                int done_steps = Integer.valueOf(earliest_goal_data_steps.toString());
                int achived_goal =  count_steps - done_steps;
                if ( achived_goal > 100)
                {
                    imgReward.setImageResource(R.drawable.chock);
                    reward_text.setText(R.string.Reward1);
                    earliest_goalSteps.setText("");
                    earliest_goal_left.setText("You are free for today");
                    earliest_goal_number.setText("");
                    earliest_goal_text.setText("");

                }
                else if ( achived_goal > 200)
                {
                    imgReward.setImageResource(R.drawable.beer);
                    reward_text.setText("Reward2");
                    earliest_goalSteps.setText("");
                    earliest_goal_left.setText("You are free for today");
                    earliest_goal_number.setText("");
                    earliest_goal_text.setText("");
                }
                else if ( achived_goal > 500)
                {
                    imgReward.setImageResource(R.drawable.cake);
                    reward_text.setText("Reward3");
                    earliest_goalSteps.setText("");
                    earliest_goal_left.setText("You are free for today");
                    earliest_goal_number.setText("");
                    earliest_goal_text.setText("");
                }
                else if ( achived_goal < 0)
                {

                    switch (lookPerson) {
                        case ("Angelina"):
                            imgReward.setImageResource(R.drawable.angelina);
                            break;
                        case ("Jessica"):
                            imgReward.setImageResource(R.drawable.jessica);
                            break;
                        case ("Muscular girl"):
                            imgReward.setImageResource(R.drawable.muscular);
                            break;
                        case ("Kiera"):
                            imgReward.setImageResource(R.drawable.kiera);
                            break;
                        case ("Victoria Secret model"):
                            imgReward.setImageResource(R.drawable.victoria);
                            break;
                        default:
                            imgReward.setImageResource(R.drawable.white);
                            break;

                    }
                    reward_text.setText("You shall train harder, "+ namePerson +", to look like " + lookPerson + "!\n" + "You just need to do " + String.valueOf(-achived_goal) + " steps more");
                }
                //reward_text.setText(String.valueOf(achived_goal) + " = - " + String.valueOf(count_steps) + " + " + String.valueOf(done_steps));
            }
            //String count_today_steps = data_today_steps.substring(6, data_today.lastIndexOf("Distance: "));
            //reward_text.setText(count_today_steps);
        }
        else
        {
            earliest_goal_left.setText("");
            earliest_goal_number.setText("0");
            earliest_goal_text.setText("You didn't set your goal yet");
            imgReward.setImageResource(R.drawable.nothing);
            reward_text.setText(R.string.Reward0);
            earliest_goalSteps.setText("");
        }

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the data and write it to database from bluetooth device
                UpdateData updateData = new UpdateData(getActivity());
                updateData.receiveAndUpdateData();

                String data_today = showLastDataRecord();
                data_home_distance.setText(data_today);
                String data_today_distance = data_today.substring(data_today.lastIndexOf("Distance: "), data_today.indexOf("Calories: ") - 1);
                data_home_distance.setText(data_today_distance);

                String data_today_calories = data_today.substring(data_today.lastIndexOf("Calories: "), data_today.length());
                data_home_calories.setText(data_today_calories);

                String data_today_steps = data_today.substring(0, data_today.lastIndexOf("Distance: "));
                data_home_steps.setText(data_today_steps);

            }
        });
        //recommendation.setText(String.valueOf(index));
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Intent service = new Intent(getActivity(), DeviceService.class);
        //getActivity().startService(service);
        //getActivity().stopService(service);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private String showLastDataRecord(){
        mDbHelper = new DataBase(getActivity());
        Cursor cursor = mDbHelper.getLastDataRecord();
        String result = new String();
        if ((cursor != null) && (cursor.getCount() > 0)){
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String steps = cursor.getString(cursor.getColumnIndex("steps"));
            String distance = cursor.getString(cursor.getColumnIndex("distance"));
            String calories = cursor.getString(cursor.getColumnIndex("calories"));
            result = "Steps:"+steps+"\n" +
                    "Distance: "+distance+"\n" +
                    "Calories: "+calories;
        }

        mDbHelper.close();
        return result;
    }
    private String showDiete() {
        mDbHelper = new DataBase(getActivity());
        Cursor cursor = mDbHelper.getAllTodos();
        String result = "";
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            String dieteData = cursor.getString(cursor.getColumnIndex("diete"));
            result = dieteData;
            Log.d("Home", "RESULT: " + result);
        }
        mDbHelper.close();
        return result;
    }

    private String showEarliestGoal(){
        mDbHelper = new DataBase(getActivity());
        Cursor cursor = mDbHelper.getEarliestGoalRecord();
        //Cursor cursor = mDbHelper.getAllGoals();
        String result = "";
        if ((cursor != null) && (cursor.getCount() > 0)){
            cursor.moveToFirst();
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String steps = cursor.getString(cursor.getColumnIndex("distance"));
            Date date_obj = new Date(Long.parseLong(date));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            result = "Date:"+format.format(date_obj) +
                    " Steps:"+steps;
            Log.d("Home", "RESULT: " + result);
        }

        mDbHelper.close();
        return result;
    }

    public void refresh_goal(){

    }
    private int mod(int x, int y)
    {
        int result = x % y;
        return result < 0? result + y : result;
    }

    public static int abs(int a) {
        return  (a < 0) ? -a : a;
    }

    private String ReadPersonalName(){
        mDbHelper = new DataBase(getActivity());
        Cursor cursor = mDbHelper.getAllTodos();
        String result = "";
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            String namePerson = cursor.getString(cursor.getColumnIndex("name"));
            result = namePerson;
            Log.d("Home", "RESULT: " + result);
        }
        mDbHelper.close();
        return result;
    }

    private String ReadPersonalAge(){
        mDbHelper = new DataBase(getActivity());
        Cursor cursor = mDbHelper.getAllTodos();
        String result = "";
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            String agePerson = cursor.getString(cursor.getColumnIndex("age"));
            result = agePerson;
            Log.d("Home", "RESULT: " + result);
        }
        mDbHelper.close();
        return result;
    }

    private String ReadPersonalWeight(){
        mDbHelper = new DataBase(getActivity());
        Cursor cursor = mDbHelper.getAllTodos();
        String result = "";
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            String weightPerson = cursor.getString(cursor.getColumnIndex("weight"));
            result = weightPerson;
            Log.d("Home", "RESULT: " + result);
        }
        mDbHelper.close();
        return result;
    }

    private String ReadPersonalHeiht(){
        mDbHelper = new DataBase(getActivity());
        Cursor cursor = mDbHelper.getAllTodos();
        String result = "";
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            String heihtPerson = cursor.getString(cursor.getColumnIndex("heiht"));
            result = heihtPerson;
            Log.d("Home", "RESULT: " + result);
        }
        mDbHelper.close();
        return result;
    }

    private String ReadPersonalLook(){
        mDbHelper = new DataBase(getActivity());
        Cursor cursor = mDbHelper.getAllTodos();
        String result = "";
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            String lookPerson = cursor.getString(cursor.getColumnIndex("look"));
            result = lookPerson;
            Log.d("Home", "RESULT: " + result);
        }
        mDbHelper.close();
        return result;
    }

    private void checkDatabaseForData(){
        mDbHelper = new DataBase(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        Cursor cursor = mDbHelper.getLastDataRecord();
        if ((cursor == null) || (cursor.getCount() < 29)) {
            //String rowId = cursor.getString(cursor.getColumnIndex("_id"));
            //Log.d("Stats", "" + rowId);
            TestDataMonth testData = new TestDataMonth();
            ArrayList<String[]> testDataArray = testData.createTestData();
            for (String[] testDay : testDataArray) {
                String date = testDay[0];
                String steps = testDay[1];
                String distance = testDay[2];
                String calories = testDay[3];
                mDbHelper.createNewDataRecord(date, steps, distance, calories);
            }
        }
        mDbHelper.close();
    }

}