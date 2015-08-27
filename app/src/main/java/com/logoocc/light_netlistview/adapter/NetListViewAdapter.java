package com.logoocc.light_netlistview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.logoocc.light_netlistview.R;
import com.logoocc.light_netlistview.bean.Person;
import com.logoocc.light_netlistview.utils.HttpHelper;

import java.util.List;

/**
 * Created by samchen on 8/26/15.
 */
public class NetListViewAdapter extends BaseAdapter {

    private LayoutInflater lif;
    private List<Person> persons;
    private Context context;
    private ImageLoader loader;

    public NetListViewAdapter(Context context, List<Person> persons) {
        this.persons = persons;
        this.context = context;
        this.lif = LayoutInflater.from(context);
        loader = HttpHelper.getInstance(context).getLoader();
    }


    @Override
    public int getCount() {
        return persons != null ? persons.size() : 0;
    }

    @Override
    public Person getItem(int position) {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return persons.get(position).getId();
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        ViewHolder holder = null;
        if (v == null) {
            holder = new ViewHolder();
            v = lif.inflate(R.layout.listview_item, null);
            holder.tvName = (TextView) v.findViewById(R.id.item_name);
            holder.tvAge = (TextView) v.findViewById(R.id.item_age);
            holder.tvAddress = (TextView) v.findViewById(R.id.item_address);
            holder.tvPhone = (TextView) v.findViewById(R.id.item_phone);
            holder.img = (NetworkImageView) v.findViewById(R.id.item_photo);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        Person p = getItem(position);

        holder.tvName.setText(p.getName());
        holder.tvAddress.setText(p.getAddress());
        holder.tvAge.setText("" + p.getAge());
        holder.tvPhone.setText(p.getPhone());
        String url ="http://d.hiphotos.baidu.com/image/pic/item/43a7d933c895d1435efd25f277f082025baf075c.jpg";
        holder.img.setImageUrl(url, loader);


        return v;
    }

    public void clear() {
        this.persons.clear();

    }

    public void add(List<Person> persons) {
        this.persons.addAll(persons);
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tvName;
        TextView tvAge;
        TextView tvAddress;
        TextView tvPhone;
        NetworkImageView img;
    }


}
