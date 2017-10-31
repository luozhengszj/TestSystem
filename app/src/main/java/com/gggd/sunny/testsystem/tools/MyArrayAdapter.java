package com.gggd.sunny.testsystem.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gggd.sunny.testsystem.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Sunny on 2017/10/30.
 */
public class MyArrayAdapter extends BaseAdapter {

    private List<Map<String, String>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public MyArrayAdapter(Context context, List<Map<String, String>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }
    /**
     * 组件集合，对应list.xml中的控件
     * @author Administrator
     */
    public final class Zujian{
        public TextView mtvonetestscore;
        public TextView mtvonetesttime;
        public TextView mtvonetestnum;
    }
    @Override
    public int getCount() {
        return data.size();
    }
    /**
     * 获得某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Zujian zujian = null;
        if(convertView==null){
            zujian=new Zujian();
            //获得组件，实例化组件
            convertView=layoutInflater.inflate(R.layout.one_test_show, null);
            zujian.mtvonetestnum=(TextView)convertView.findViewById(R.id.tvonetestnum);
            zujian.mtvonetestscore=(TextView)convertView.findViewById(R.id.tvonetestscore);
            zujian.mtvonetesttime=(TextView)convertView.findViewById(R.id.tvonetesttime);
            convertView.setTag(zujian);
        }else{
            zujian=(Zujian)convertView.getTag();
        }
        //绑定数据
        zujian.mtvonetestnum.setText(data.get(position).get("number"));
        zujian.mtvonetesttime.setText(data.get(position).get("time"));
        zujian.mtvonetestscore.setText(data.get(position).get("score"));
        return convertView;
    }
}
