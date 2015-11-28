package info.androidhive.materialdesign.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialdesign.R;

/**
 * Created by Ravi on 29/07/15.
 */
public class MessagesFragment extends Fragment implements View.OnClickListener{


    String[] StringDiet = {"No food", "Breakfast", "Banana diet", "Meat diet"};

    private Long mRowId;
    private TextView meditName;
    private TextView meditAge;
    private TextView meditWeight;
    private TextView meditHight;
    private TextView mDiete;
    private Spinner mCategory;

    private Cursor cursor;
    private DataBase mDbHelper;
    private SQLiteDatabase mDb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        Button saveButton = (Button) getActivity().findViewById(R.id.buttonEdit);
        saveButton.setOnClickListener(this);

        //TextView
        meditName = (TextView)getActivity().findViewById(R.id.Name);
        meditAge = (TextView) getActivity().findViewById(R.id.Age);
        meditWeight = (TextView) getActivity().findViewById(R.id.Weight);
        meditHight = (TextView) getActivity().findViewById(R.id.Height);
        mDiete = (TextView) getActivity().findViewById(R.id.Diete);

        mDbHelper = new DataBase(getActivity());

       /* mDbHelper.createNewTodo("","","","","");
        //mDbHelper.createNewTodo("mTitleText","mBodyText","meditWeight");

        mDb = mDbHelper.getWritableDatabase();

        fillData();

        // Inflate the layout for this fragment

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_SHORT).show();
                //createNewTask();
            }
        });*/



        return rootView;
    }

    public void onClick(View view)
    {
        //Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_SHORT).show();
        //createNewTask();
    }

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


    private void createNewTask() {
        Intent intent = new Intent(getActivity(), MessagesFragmentEdit.class);
        startActivityForResult(intent, 1);
    }

    private void fillData() {
        Log.d("filldata", "filldata");
        List<String> name = new ArrayList<String>();
        List<String> age = new ArrayList<String>();
        List<String> weight = new ArrayList<String>();
        List<String> heiht = new ArrayList<String>();
        List<String> diete = new ArrayList<String>();
        cursor = mDbHelper.getAllTodos();
        if (cursor.moveToFirst())
        {
            do
            {
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
            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

    }
}
