package info.androidhive.materialdesign.activity;

/**
 * Created by Ravi on 29/07/15.
 */
import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import info.androidhive.materialdesign.R;


public class HomeFragment extends Fragment {
    private DataBase mDbHelper;
    private SQLiteDatabase mDb;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        TextView txtCurrentDate= (TextView)rootView.findViewById(R.id.Data);
        TextView txtCurrentTime= (TextView)rootView.findViewById(R.id.Time);
        TextView recommendation= (TextView)rootView.findViewById(R.id.MOVE);
        TextView earliest_goal = (TextView)rootView.findViewById(R.id.earliest_goal);
        earliest_goal.setText("You don't have a goal yet.");
        final TextView deviceData= (TextView)rootView.findViewById(R.id.data_home);
        Button updateData = (Button) rootView.findViewById(R.id.read_result);

        Date d = new Date();
        CharSequence time = DateFormat.format("yyyy-MM-dd", d.getTime());
        CharSequence date = DateFormat.format("hh:mm", d.getTime());
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        txtCurrentTime.setText(time);
        txtCurrentDate.setText(date);

        switch (hour) {
            case (10):
                recommendation.setText(R.string.string1);
                break;
            case (12):
                recommendation.setText(R.string.string2);
                break;
            case (18):
                recommendation.setText(R.string.string3);
                break;
            case (22):
                recommendation.setText(R.string.string4);
                break;
        }

        deviceData.setText(showLastDataRecord());
        if(!showEarliestGoal().equals("")){
            earliest_goal.setText(showEarliestGoal());
        }
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceData.setText(showLastDataRecord());
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
            result = "Date:"+date+"\n" +
                    "Steps:"+steps+"\n" +
                    "Distance: "+distance+"\n" +
                    "Calories: "+calories;
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
}
