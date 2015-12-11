package info.androidhive.materialdesign.activity;
/**
 * Created by Daria, Roma, Alper
 */
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.materialdesign.auxilary.TestDataMonth;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import info.androidhive.materialdesign.R;

public class StatsFragment extends Fragment {
    private Cursor cursor;
    private DataBase mDbHelper;
    private SQLiteDatabase mDb;
    private GraphView graph1;
    private GraphView graph2;
    private Button mButtonStart;
    private Button mButtonEnd;
    private TextView mWeekData;
    public static Calendar mCalendar;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private int mDay;
    private int mMonth;
    private int mYear;
    private int mPosition;
    private GregorianCalendar mStartDate;
    private GregorianCalendar mEndDate;

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDatabaseForData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        /*
        Spinner (choice measurements type)
         */
        mWeekData = (TextView) rootView.findViewById(R.id.text_week_data);
        Spinner graphSpinner = (Spinner) rootView.findViewById(R.id.graph_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.graph_choice, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graphSpinner.setAdapter(adapter);
        graphSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.d("item selected", "item selected");
                changeGraphs(position);
                mPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        /*
        DatePickers` buttons
        */
        mButtonStart = (Button) rootView.findViewById(R.id.start_date);
        mButtonStart.setText("Set start date");
        mButtonEnd = (Button) rootView.findViewById(R.id.end_date);
        mButtonEnd.setText("Set end date");


        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        updateButtonStart();
                        setCustomDateGraph();
                    }
                };
                final Calendar c = Calendar.getInstance();
                DatePickerDialog d = new DatePickerDialog(getActivity(),
                        R.style.Base_Theme_AppCompat, mDateSetListener, c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                d.show();
            }
        });

        mButtonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        updateButtonEnd();
                        setCustomDateGraph();
                    }
                };
                final Calendar c = Calendar.getInstance();
                DatePickerDialog d = new DatePickerDialog(getActivity(),
                        R.style.Base_Theme_AppCompat, mDateSetListener, c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                d.show();
            }
        });

         /*
         Weekly graph
         */
        graph1 = (GraphView) rootView.findViewById(R.id.graph1);
        DataPoint[] weeklyPoints = this.createDataPoints(7,1);

        BarGraphSeries<DataPoint> seriesWeek = new BarGraphSeries<DataPoint>(weeklyPoints);
        graph1.addSeries(seriesWeek);

        // styling
        seriesWeek.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });

        seriesWeek.setSpacing(50);
        seriesWeek.setDrawValuesOnTop(true);
       // seriesWeek.setValuesOnTopColor(Color.RED);
       // seriesWeek.setValuesOnTopSize(50);

        /*
         Monthly graph
         */
        graph2 = (GraphView) rootView.findViewById(R.id.graph2);
        DataPoint[] monthlyPoints = this.createDataPoints(30,1);
        graph2.getViewport().setMaxX(32);

        BarGraphSeries<DataPoint> seriesMonth = new BarGraphSeries<DataPoint>(monthlyPoints);
        graph2.addSeries(seriesMonth);

        // styling
        seriesMonth.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });

        seriesMonth.setSpacing(30);
        seriesMonth.setDrawValuesOnTop(false);
        seriesMonth.setValuesOnTopColor(Color.RED);

        //graph1.getViewport().setMaxX(9);

        //graph1.getViewport().setXAxisBoundsManual(true);
        //graph2.getViewport().setXAxisBoundsManual(true);

        seriesWeek.setDrawValuesOnTop(true);
        // Inflate the layout for this fragment
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

    private void changeGraphs(int position){
        BarGraphSeries<DataPoint> seriesWeek = new BarGraphSeries<DataPoint>(createDataPoints(7, position));
        BarGraphSeries<DataPoint> seriesMonth = new BarGraphSeries<DataPoint>(createDataPoints(30, position));

        graph1.removeAllSeries();
        graph1.addSeries(seriesWeek);

        // styling
        seriesWeek.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });
        seriesWeek.setSpacing(50);
        seriesWeek.setDrawValuesOnTop(true);
        seriesWeek.setSpacing(30);
        //graph1.getViewport().setMaxX(7);
        //graph1.getViewport().setXAxisBoundsManual(true);


        graph2.removeAllSeries();
        graph2.addSeries(seriesMonth);
        // styling
        seriesMonth.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });
        graph2.getViewport().setMaxX(32);
        mWeekData.setText("Statistics for the Week time");
       // graph2.getViewport().setXAxisBoundsManual(true);
        //seriesMonth.setSpacing(50);
    }

    private DataPoint[] createDataPoints(int days, int position){
        //Choice parameter
        String parameter = "distance";
        switch (position){
            case 0:
                parameter = "distance";
                break;
            case 1:
                parameter = "steps";
                Log.d("Steps choice", Integer.toString(position));
                break;
            case 2:
                parameter = "calories";
                break;
        }
        mDbHelper = new DataBase(getActivity());
        mDb = mDbHelper.getReadableDatabase();
        DataPoint[] dataPoints = new DataPoint[days+1];
        Cursor mCursor = mDbHelper.getLastNDataRecord(days);
        int i = 0;

        while(mCursor.moveToNext()){
            String distance = mCursor.getString(mCursor.getColumnIndex(parameter));
            //String day_month = mCursor.getString(mCursor.getColumnIndex("date")).substring(5, 10);
            try {
                dataPoints[i] = new DataPoint(i+1, Integer.parseInt(distance));
            } catch (NullPointerException e) {
                dataPoints[i] = new DataPoint(i+1, 1);
                e.printStackTrace();
            }
            i++;
        }
        dataPoints[i] = new DataPoint(i+1, 0);
        Log.d("Requested ", Integer.toString(i));
        mDbHelper.close();
        return dataPoints;
    }

    private DataPoint[] createCustomDataPoints(int position){
        GregorianCalendar mToday = new GregorianCalendar();
        int differenceStartDayToday = dayDifference(mStartDate,mToday);
        int differenceEndDayToday = dayDifference(mEndDate,mToday);
        if (differenceEndDayToday < 0 || differenceStartDayToday < 0) {
            Toast.makeText(getActivity(), "Chose date earlier than today", Toast.LENGTH_SHORT).show();
        }
        int days = differenceStartDayToday;
        //Choice parameter
        String parameter = "distance";
        switch (mPosition){
            case 0:
                parameter = "distance";
                break;
            case 1:
                parameter = "steps";
                Log.d("Steps choice", Integer.toString(position));
                break;
            case 2:
                parameter = "calories";
                break;
        }
        mDbHelper = new DataBase(getActivity());
        mDb = mDbHelper.getReadableDatabase();
        DataPoint[] dataPoints = new DataPoint[days+1];
        Cursor mCursor = mDbHelper.getLastNDataRecord(days);
        int i = 0; int j = 0;
        while(mCursor.moveToNext()){
            String distance = mCursor.getString(mCursor.getColumnIndex(parameter));
            //String day_month = mCursor.getString(mCursor.getColumnIndex("date")).substring(5, 10);
            try {
                dataPoints[i] = new DataPoint(i+1, Integer.parseInt(distance));
            } catch (NullPointerException e) {
                e.printStackTrace();
                dataPoints[i] = new DataPoint(i+1, 0);
            }
            i++;
        }
        mDbHelper.close();
        DataPoint[] zeros = new DataPoint[j];
        DataPoint[] dataCustomPoints = Arrays.copyOfRange(dataPoints, 0, days-differenceEndDayToday);
        return dataCustomPoints;
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

    private void setCustomDateGraph(){
        if(mStartDate != null && mEndDate != null){
            if (mEndDate.compareTo(mStartDate) > 0) {
                dayDifference(mStartDate, mEndDate);
                BarGraphSeries<DataPoint> seriesWeek = new BarGraphSeries<DataPoint>(createCustomDataPoints(1));

                graph1.removeAllSeries();
                graph1.addSeries(seriesWeek);
                mWeekData.setText("Statistics for the time range");

                // styling
                seriesWeek.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                    @Override
                    public int get(DataPoint data) {
                        return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
                    }
                });
                seriesWeek.setSpacing(50);
                seriesWeek.setDrawValuesOnTop(true);
                seriesWeek.setSpacing(30);

            } else {
                Toast.makeText(getActivity(), "End date is earlier than start date", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            if (mStartDate != null) {
                Toast.makeText(getActivity(), "Choice start date", Toast.LENGTH_SHORT).show();
            }
            if (mEndDate != null) {
                Toast.makeText(getActivity(), "Choice end date", Toast.LENGTH_SHORT).show();
            }
         }
    }

    private void updateButtonStart() {
        GregorianCalendar c = new GregorianCalendar(mYear, mMonth, mDay);
        mStartDate = c;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        mButtonStart.setText(sdf.format(c.getTime()));
    }

    private void updateButtonEnd() {
        GregorianCalendar c = new GregorianCalendar(mYear, mMonth, mDay);
        mEndDate = c;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        mButtonEnd.setText(sdf.format(c.getTime()));
    }

    private int dayDifference(GregorianCalendar cal1,GregorianCalendar cal2){

        Date d1=cal1.getTime();
        Date d2=cal2.getTime();

        long diff=d2.getTime()-d1.getTime();
        int diffDays=(int)(diff/(1000*24*60*60));

        Toast.makeText(getActivity(), "Day difference "+Integer.toString(diffDays), Toast.LENGTH_SHORT).show();

        return diffDays;
    }
}