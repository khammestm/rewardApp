package info.androidhive.materialdesign.activity;

/**
 * Created by Ravi on 29/07/15.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import info.androidhive.materialdesign.R;


public class HomeFragment extends Fragment {

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
}
