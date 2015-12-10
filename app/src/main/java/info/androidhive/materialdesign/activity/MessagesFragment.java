package info.androidhive.materialdesign.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialdesign.R;

/**
 * Created by Daria, Roma, Alper
 */
public class MessagesFragment extends Fragment implements View.OnClickListener{


    //String[] StringDiet = {"No food", "Breakfast", "Banana diet", "Meat diet"};

    private Long mRowId;
    private EditText meditName;
    private EditText meditAge;
    private EditText meditWeight;
    private EditText meditHight;
    private TextView mDiete;
    private TextView mLook;
    private Spinner mDieteSpinner;
    private Spinner mLookSpinner;
    private ImageView imgLook;

    private Cursor cursor;
    private DataBase mDbHelper;
    private SQLiteDatabase mDb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_personal_data, container, false);

        Button saveButton = (Button) rootView.findViewById(R.id.buttonEdit);
        saveButton.setOnClickListener(this);

        Button editButton = (Button) rootView.findViewById(R.id.buttonSave);
        editButton.setOnClickListener(this);

        //TextView

        meditName = (EditText)rootView.findViewById(R.id.Name);
        meditAge = (EditText) rootView.findViewById(R.id.Age);
        meditWeight = (EditText) rootView.findViewById(R.id.Weight);
        meditHight = (EditText) rootView.findViewById(R.id.Height);
        mDiete = (TextView) rootView.findViewById(R.id.Diete);
        mLook = (TextView)rootView.findViewById(R.id.WantToBeLike);

        mDieteSpinner = (Spinner) rootView.findViewById(R.id.spinnerDiete);
        mDieteSpinner.setVisibility(View.GONE);
        mLookSpinner = (Spinner) rootView.findViewById(R.id.spinnerLook);
        mLookSpinner.setVisibility(View.GONE);

        ImageView imgLook= (ImageView) rootView.findViewById(R.id.imageLook);

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
        ArrayAdapter<String> StringLook = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new
                String[]{"Ben", "Tom"});
        mLookSpinner.setAdapter(StringLook);
        mLookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        meditName.getText().clear();
        mDbHelper = new DataBase(getActivity());

        //mDbHelper.createNewTodo("33","33","33","33","33","33");
        //mDbHelper.createNewTodo("mTitleText","mBodyText","meditWeight");

        mDb = mDbHelper.getWritableDatabase();

        fillData();

        // Inflate the layout for this fragment

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Button SAVE Clicked", Toast.LENGTH_SHORT).show();
                SaveTextData();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Button EDIT Clicked", Toast.LENGTH_SHORT).show();
                EditTextData();
                saveState();
                Log.d("push data", meditName.getText().toString());
                Log.d("push data", meditAge.getText().toString());
                String mPhoto = mLook.getText().toString();
            }
        });

        String mPhotot = mLook.getText().toString();
        switch (mPhotot) {
            case "Tom":
                imgLook.setImageResource(R.drawable.tom);
                break;
            case "Ben":
                imgLook.setImageResource(R.drawable.ben);
                break;
            default:
                imgLook.setImageResource(R.drawable.white);
                break;
        }

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


    private void SaveTextData() {
        meditName.setFocusable(false);
        meditName.setInputType(InputType.TYPE_NULL);
        meditName.setClickable(false);

        meditAge.setFocusable(false);
        meditAge.setClickable(false);

        meditWeight.setFocusable(false);
        meditWeight.setClickable(false);

        meditHight.setFocusable(false);
        meditHight.setClickable(false);

        mDiete.setFocusable(false);
        mDiete.setClickable(false);

        mDieteSpinner.setVisibility(View.GONE);
        mLookSpinner.setVisibility(View.GONE);

        mDiete.setVisibility(View.VISIBLE);
        mLook.setVisibility(View.VISIBLE);

        mDiete.setText(mDieteSpinner.getSelectedItem().toString());
        mLook.setText(mLookSpinner.getSelectedItem().toString());
    }

    private void EditTextData()
    {
        mDieteSpinner.setVisibility(View.VISIBLE);
        mLookSpinner.setVisibility(View.VISIBLE);

        meditName.setFocusable(true);
        meditName.setClickable(true);
        meditName.setEnabled(true);
        meditName.setFocusableInTouchMode(true);
        meditName.setInputType(InputType.TYPE_CLASS_TEXT);

        meditAge.setFocusable(true);
        meditAge.setClickable(true);
        meditAge.setEnabled(true);
        meditAge.setFocusableInTouchMode(true);
        meditAge.setInputType(InputType.TYPE_CLASS_TEXT);

        meditWeight.setFocusable(true);
        meditWeight.setClickable(true);
        meditWeight.setEnabled(true);
        meditWeight.setFocusableInTouchMode(true);
        meditWeight.setInputType(InputType.TYPE_CLASS_TEXT);

        meditHight.setFocusable(true);
        meditHight.setClickable(true);
        meditHight.setEnabled(true);
        meditHight.setFocusableInTouchMode(true);
        meditHight.setInputType(InputType.TYPE_CLASS_TEXT);

        mDiete.setVisibility(View.GONE);
        mLook.setVisibility(View.GONE);
        //mDiete.setFocusable(true);
        //mDiete.setClickable(true);
        //mDiete.setEnabled(true);
        //mDiete.setFocusableInTouchMode(true);
        //mDiete.setInputType(InputType.TYPE_CLASS_TEXT);
    }


    private void fillData() {
        Log.d("filldata", "filldata");
        List<String> name = new ArrayList<String>();
        List<String> age = new ArrayList<String>();
        List<String> weight = new ArrayList<String>();
        List<String> heiht = new ArrayList<String>();
        List<String> diete = new ArrayList<String>();
        List<String> look = new ArrayList<String>();
        cursor = mDbHelper.getAllTodos();
        if (cursor.moveToFirst())
        {
            do {
                name.add(cursor.getString(1));
                meditName.setText(name.toString().replace("[", "").replace("]", "").replace(",", ""));
                age.add(cursor.getString(2));
                meditAge.setText(age.toString().replace("[", "").replace("]", "").replace(",", ""));
                weight.add(cursor.getString(3));
                meditWeight.setText(weight.toString().replace("[", "").replace("]", "").replace(",", ""));
                heiht.add(cursor.getString(4));
                meditHight.setText(heiht.toString().replace("[", "").replace("]", "").replace(",", ""));
                diete.add(cursor.getString(5));
                mDiete.setText(diete.toString().replace("[", "").replace("]", "").replace(",", ""));
                look.add(cursor.getString(6));
                mLook.setText(look.toString().replace("[", "").replace("]", "").replace(",", ""));
            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

    }


    private void saveState()
    {
        String name = meditName.getText().toString();//(String) mCategory.getSelectedItem();
        String age = meditAge.getText().toString();
        String weight = meditWeight.getText().toString();
        String heiht = meditHight.getText().toString();
        String diete = mDieteSpinner.getSelectedItem().toString();
        String look = mLookSpinner.getSelectedItem().toString();
        mDbHelper.updateTodo(1, name, age, weight, heiht, diete, look);
    }


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
