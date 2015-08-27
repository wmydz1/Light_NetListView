package com.logoocc.light_netlistview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.logoocc.light_netlistview.adapter.NetListViewAdapter;
import com.logoocc.light_netlistview.bean.Person;
import com.logoocc.light_netlistview.bean.Result;
import com.logoocc.light_netlistview.utils.HttpHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NetListView lv;
    private NetListViewAdapter adapter;
    private HttpHelper helper;
    private int page = 1;
    private static String Tag = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = HttpHelper.getInstance(getApplicationContext());

        lv = (NetListView) findViewById(R.id.main_lv);
        adapter = new NetListViewAdapter(this, new ArrayList<Person>());
        lv.getListViewInstance().setAdapter(adapter);
        lv.getListViewInstance().setOnRefreshListener(lis2);

        lv.getListViewInstance().setOnItemClickListener(itemClicLis);
        loadData(false);

    }

    private AdapterView.OnItemClickListener itemClicLis = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent in = new Intent(MainActivity.this, AddUpdateActivity.class);
            in.putExtra("isAdd", false);
            in.putExtra("id", (int) id);
            startActivityForResult(in, 10);
        }
    };

    private PullToRefreshBase.OnRefreshListener2<ListView> lis2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        //下拉刷新
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            page = 1;
            loadData(true);

        }

        // 上拉加载
        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            loadData(false);
        }
    };


    // 加载数据
    public void loadData(final boolean isFlush) {
        String params = "{action:200,page:" + page + ",size:5}";
        helper.loadPersons(params, new Response.Listener<Result<List<Person>>>() {


            public void onResponse(Result<List<Person>> listResult) {
                lv.getListViewInstance().onRefreshComplete();

                if (listResult != null) {
                    Toast.makeText(MainActivity.this, "" + listResult.msg, Toast.LENGTH_SHORT).show();
                }
                if (listResult.status == Result.STATUS_SUC) {
                    List<Person> persons = listResult.data;
                    if (persons != null && persons.size() > 0) {


                        if (isFlush) {
                            adapter.clear();
                        }

                        adapter.add(persons);
                        page++;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "结果为空", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                lv.getListViewInstance().onRefreshComplete();

                Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                if (volleyError.networkResponse != null) {
                    Log.e(Tag, volleyError.networkResponse.statusCode + "");
                    Log.e(Tag, new String(volleyError.networkResponse.data) + "");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 50) {
            page = 1;
            loadData(true);
        }
    }


}
