package info.androidhive.materialdesign.activity;
/**
 * Created by Daria, Roma, Alper
 */
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

import info.androidhive.materialdesign.R;

public class StepsAddGoalFragment extends Fragment implements View.OnClickListener{


    String[] StringDiet = {"No food", "Breakfast", "Banana diet", "Meat diet"};

    private Cursor cursor;
    private DataBase mDbHelper;
    private SQLiteDatabase mDb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static long getDateFromDatePickers(DatePicker datePicker, TimePicker timePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, timePicker.getCurrentHour(), timePicker.getCurrentMinute());

        return calendar.getTimeInMillis();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_step_goals_add, container, false);
        Button goal_add_button = (Button) rootView.findViewById(R.id.goal_add_button);
        final DatePicker picker = (DatePicker) rootView.findViewById(R.id.dpResult);
        final TimePicker tpicker = (TimePicker) rootView.findViewById(R.id.tpResult);
        final EditText goal_distance = (EditText) rootView.findViewById(R.id.goal_distance);
        //final Button goal_add_button = (Button) rootView.findViewById(R.id.goal_add_button);

        mDbHelper = new DataBase(getActivity());

        goal_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String message = "Entered distance: " + Integer.parseInt(goal_distance.getText().toString()) + ".";
                    message += "Entered date: " + getDateFromDatePickers(picker, tpicker);

                    Calendar c = Calendar.getInstance();
                    if(c.getTimeInMillis() > getDateFromDatePickers(picker,tpicker)){
                        Toast.makeText(getActivity(), "Please enter a date from future!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mDbHelper.insertGoal(getDateFromDatePickers(picker,tpicker), goal_distance.getText().toString());
                    Log.d("Goal", "Added : " + message);
                    Log.d("Goal", "Current number of goals" + mDbHelper.getAllGoals().getCount());
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, new HomeFragment());
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                    fragmentTransaction.commit();


                }catch (NumberFormatException exp){
                    Toast.makeText(getActivity(), "Don't forget to enter distance in number and a date!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    public void onClick(View view) {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /*public void onResume(){
        super.onResume();
        fillData();
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //saveState();
        //outState.putSerializable(ToDoDatabase.COLUMN_ID, mRowId);
    }

    @Override
    public void onPause() {
        super.onPause();
        //saveState();
    }

    @Override
    public void onResume() {
        super.onResume();
        //populateFields();
    }

}
