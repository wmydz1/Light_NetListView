package com.logoocc.light_netlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.logoocc.light_netlistview.bean.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samchen on 8/26/15.
 */
public class NetListView extends LinearLayout {

    private PullToRefreshListView lv;


    public NetListView(Context context) {
        super(context);
        init(null);
    }

    public NetListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NetListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.light_listview, this);
        lv = (PullToRefreshListView) findViewById(R.id.light_lv);
    }

    public  PullToRefreshListView getListViewInstance() {
        return lv;
    }

    public static List<Person> testData() {
        List<Person> persons = new ArrayList<Person>();

        for (int i = 0; i < 30; i++) {
            Person p = new Person();
            p.setPhone("10080-" + i);
            p.setAge(2 * i);
            p.setAddress("Mars" + i);
            p.setName("logoocc " + i);
            p.setPhotourl("xxxx");
            persons.add(p);
        }
        return persons;
    }


}
