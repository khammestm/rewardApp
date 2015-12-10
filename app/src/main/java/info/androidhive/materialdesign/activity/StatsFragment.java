package info.androidhive.materialdesign.activity;
/**
 * Created by Daria, Roma, Alper
 */
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import info.androidhive.materialdesign.auxilary.TestDataMonth;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import info.androidhive.materialdesign.R;


public class StatsFragment extends Fragment {
    private Cursor cursor;
    private DataBase mDbHelper;
    private SQLiteDatabase mDb;
    GraphView graph1;
    GraphView graph2;

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
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
                e.printStackTrace();
            }
            i++;
        }
        dataPoints[i] = new DataPoint(i+1, 0);
        Log.d("Requested ",Integer.toString(i));
        mDbHelper.close();
        return dataPoints;
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
