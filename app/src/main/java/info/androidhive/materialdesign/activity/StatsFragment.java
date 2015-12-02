package info.androidhive.materialdesign.activity;

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
import info.androidhive.materialdesign.auxilary.TestDataMonth;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

import info.androidhive.materialdesign.R;


public class StatsFragment extends Fragment {
    private Cursor cursor;
    private DataBase mDbHelper;
    private SQLiteDatabase mDb;

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

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        mDbHelper = new DataBase(getActivity());
        mDb = mDbHelper.getReadableDatabase();
        DataPoint[] dataPoints = new DataPoint[30];
        Cursor mCursor = mDbHelper.getLastNDataRecord(30);
        int i = 0;
        while(mCursor.moveToNext()){
            String distance = mCursor.getString(mCursor.getColumnIndex("distance"));
            String day_month = mCursor.getString(mCursor.getColumnIndex("date")).substring(5,10);
            try {
                dataPoints[i] = new DataPoint(i, Integer.parseInt(distance));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            i++;
        }

        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dataPoints);
        graph.addSeries(series);

        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });

        series.setSpacing(50);

        // draw values on top
        series.setDrawValuesOnTop(false);
        series.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);

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
