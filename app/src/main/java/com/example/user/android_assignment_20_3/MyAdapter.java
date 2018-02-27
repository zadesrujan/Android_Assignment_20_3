package com.example.user.android_assignment_20_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 19-02-2018.
 */

public class MyAdapter extends BaseAdapter {
    //public is a method and fields can be accessed by the members of any class.
    //class is a collections of objects.
    //created MainActivity and extends with AppCompatActivity which is Parent class.

    LayoutInflater inflater;
    private ListView listView;
    private ArrayList<String> al_contactName,al_contactNumber;
    Context context;
    //declaring the values.

    MyAdapter(Context context, ArrayList<String> al_contactName, ArrayList<String> al_contactNumber) {
        //defining the methods of myadapter and having the array list,contact name and number
        this.context = context;
        this.al_contactName = al_contactName;
        //giving contact name
        this.al_contactNumber = al_contactNumber;
        //giving contact number
    }
    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public int getCount() {
        return al_contactName.size();
    }
    //getting contact name size
    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public Object getItem(int position) {
        return null;
    }
    //getting the position to null
    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public long getItemId(int position) {
        return 0;
    }
    //getting position to 0
    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public View getView(int position, View convertView, ViewGroup parent) {
        //method of base adapter for positin and viewing purpose
        ViewHolder holder;
        //declaring the values
        inflater = LayoutInflater.from(context);
        //it will take the information which is conatin in context
        if(convertView == null){
            //if convertview is given to null
            convertView = inflater.inflate(R.layout.n_and_c,parent,false);
            //then the
            holder = new ViewHolder();
            //created new view holder
            holder.bindView(convertView);
            //binding holder to conertview
            convertView.setTag(holder);
            //Log.e("Main_ACTIVITY","convertView is NULL");
        }
        else {
            holder = (ViewHolder)convertView.getTag();
            //it will get the tag
        }
        holder.contactName.setText(al_contactName.get(position));
        //given position of the name
        holder.contactNumber.setText(al_contactNumber.get(position));
        //given position of the number
        return convertView;
    }

    public class ViewHolder {
        //cretaed class as view holder
        TextView contactName, contactNumber;
        //declared the values
        void bindView(View convertView) {
            //Log.e("ViewHolder => bindView", "Wow! its called...");
            contactName = (TextView) convertView.findViewById(R.id.contact_name);
            //given conatct name from the layout file
            contactNumber = (TextView) convertView.findViewById(R.id.contact_number);
            //given number name from the layout file
        }
    }

}