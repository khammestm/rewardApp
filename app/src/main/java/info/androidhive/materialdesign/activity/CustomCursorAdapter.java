package info.androidhive.materialdesign.activity;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import info.androidhive.materialdesign.R;

public class CustomCursorAdapter extends SimpleCursorAdapter {

    private DataBase mDbHelper;

    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mDbHelper = new DataBase(context);
    }

    public View newView(Context _context, Cursor _cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(_context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.goals_row, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context Context, final Cursor cursor) {
        String date_text = cursor.getString(cursor.getColumnIndex("date"));
        int goal = cursor.getInt(cursor.getColumnIndex("distance"));
        TextView goal_textview = (TextView) view.findViewById(R.id.goal_text);
        goal_textview.setText(Integer.toString(goal));
        TextView goal_deadline = (TextView) view.findViewById(R.id.date_text);
        goal_deadline.setText(date_text);
        Button remove_button = (Button) view.findViewById(R.id.remove_button);
        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    Object obj = view.getTag();
                    mDbHelper.deleteGoalRow(cursor.getInt(cursor.getColumnIndex("_id")));
                    Cursor cursor = mDbHelper.getAllGoals();
                    changeCursor(cursor);
                    notifyDataSetChanged();
                }
            }
        });
    }
}