package info.androidhive.materialdesign.activity;

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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import info.androidhive.materialdesign.R;

public class DisplayGoalsFragment extends Fragment implements View.OnClickListener{


    private DataBase mDbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_display_goals, container, false);
        mDbHelper = new DataBase(getActivity());
        ListView goalList=(ListView)rootView.findViewById(R.id.goalList);
        Cursor cursor = mDbHelper.getAllGoals();
        String[] fromFieldNames= new String[] {DataBase.GOAL_DATE,DataBase.GOAL_DISTANCE};
        SimpleCursorAdapter myCursorAdapter=new CustomCursorAdapter(this.getActivity(), R.layout.goals_row, cursor,fromFieldNames,new int[]{R.id.date_text,R.id.goal_text},0);
        /*myCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            public boolean setViewValue(View aView, Cursor aCursor, int aColumnIndex) {
                Log.d("ROW", "" + aColumnIndex);
                if (aColumnIndex == 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    long date_long = aCursor.getLong(aColumnIndex);
                    TextView textView = (TextView) aView.findViewById(R.id.date_text);
                    textView.setText(sdf.format(new Date(date_long)));
                    return true;
                }

                return false;
            }
        });*/
        goalList.setAdapter(myCursorAdapter);
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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
