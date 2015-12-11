package info.androidhive.materialdesign.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import info.androidhive.materialdesign.R;

/**
 * Created by Daria
 */


public class AboutFragment extends Fragment {

    private Spinner mDieteSpinner;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        mDieteSpinner = (Spinner) rootView.findViewById(R.id.spinnerDiet);
        final ImageView mImgView= (ImageView) rootView.findViewById(R.id.imageView);
        final TextView mtextDietsData = (TextView)rootView.findViewById(R.id.textDietsData);

        /*************************************************************************************************************************/
        ArrayAdapter<String> StringDiet = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new
                String[]{"No diet", "The Flat Belly Diet", "The Fast Food Diet", "The Grapefruit Diet"});
        mDieteSpinner.setAdapter(StringDiet);
        mDieteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.d("item selected", "item selected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
/*************************************************************************************************************************/
        mDieteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                String mPhoto = mDieteSpinner.getSelectedItem().toString();;
                switch (mPhoto) {
                    case "No diet":
                        mImgView.setImageResource(R.drawable.break1);
                        mtextDietsData.setText(R.string.AboutNoDiet);
                        break;
                    case "The Flat Belly Diet":
                        mImgView.setImageResource(R.drawable.fat_break1);
                        mtextDietsData.setText(R.string.AboutFat);
                        break;
                    case "The Fast Food Diet":
                        mImgView.setImageResource(R.drawable.fast_break1);
                        mtextDietsData.setText(R.string.AboutNoDiet);
                        break;
                    case "The Grapefruit Diet":
                        mImgView.setImageResource(R.drawable.greip_break1);
                        mtextDietsData.setText(R.string.AboutFast);
                        break;
                    default:
                        mImgView.setImageResource(R.drawable.white);
                        mtextDietsData.setText(R.string.AboutGrap);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                mImgView.setImageResource(R.drawable.white);
            }
        });




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
}
