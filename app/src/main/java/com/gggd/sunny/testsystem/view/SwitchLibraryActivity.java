package com.gggd.sunny.testsystem.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gggd.sunny.testsystem.MainActivity;
import com.gggd.sunny.testsystem.R;
import com.gggd.sunny.testsystem.TitleActivity;
import com.gggd.sunny.testsystem.bean.Library;
import com.gggd.sunny.testsystem.dao.LibraryAndTestDB;
import com.gggd.sunny.testsystem.tools.MyArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sunny on 2017/11/4.
 */

public class SwitchLibraryActivity extends TitleActivity implements AdapterView.OnItemClickListener {
    private ArrayList<Map<String,String>> arrayList;
    private ArrayList<Library> librarylist;
    private ListView listView;

    private TextView mtvshowtestshow1;
    private TextView mtvshowtestshow2;
    private TextView mtvshowtestshow3;
    private TextView mbutton_backward;

    private LibraryAndTestDB libraryAndTestDB;
    private String librarname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_tests_layout);
        setTitle("题库列表");
        showForwardView(false);
        mbutton_backward = (TextView) findViewById(R.id.button_backward);
        mbutton_backward.setText("返回");

        mtvshowtestshow1 = (TextView) findViewById(R.id.tvshowtestshow1);
        mtvshowtestshow2 = (TextView) findViewById(R.id.tvshowtestshow2);
        mtvshowtestshow3 = (TextView) findViewById(R.id.tvshowtestshow3);
        mtvshowtestshow1.setText("题库名");
        mtvshowtestshow2.setText("未完成");
        mtvshowtestshow3.setText("已完成");

        Intent intent = getIntent();
        librarname = intent.getStringExtra("librarname");

        listView = (ListView) findViewById(R.id.lvshowtests);               //获取id是list的ListView
        listView.setAdapter(new MyArrayAdapter(this,initAdapter()));                //自动为id是list的ListView设置适配器
        listView.setOnItemClickListener(this);
    }

    private ArrayList<Map<String, String>> initAdapter() {
        libraryAndTestDB = new LibraryAndTestDB(this);
        ArrayList<Library> tmplist;
        tmplist = libraryAndTestDB.getAllLibrary();
        if(tmplist.isEmpty()){
            Intent it = new Intent(SwitchLibraryActivity.this, MainActivity.class);
            startActivity(it);
        }else{
            librarylist = new ArrayList<>();
            librarylist = libraryAndTestDB.getLibraryInfo(tmplist);
            arrayList = new ArrayList<>();
            for (int i = 0;i<librarylist.size();i++){
                Map<String,String> map = new HashMap<>();
                map.put("number",librarylist.get(i).getLibrary_name());
                map.put("time",librarylist.get(i).getTime());
                map.put("score",librarylist.get(i).getScore());
                arrayList.add(map);
            }
        }
        return arrayList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
        final Library library = librarylist.get(position);
        if(library.getLibrary_name().equals(librarname))
            Toast.makeText(this, "当前题库即为"+librarname, Toast.LENGTH_SHORT).show();
        else {
            new AlertDialog.Builder(SwitchLibraryActivity.this, AlertDialog.THEME_HOLO_LIGHT).setTitle("是否切换到" + library.getLibrary_name())
                    .setIcon(android.R.drawable.divider_horizontal_bright)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击“确认”后的操作
                            Intent it = new Intent();
                            it.putExtra("libraryname", library.getLibrary_name());
                            it.putExtra("testok", library.getTime());
                            it.putExtra("testnot", library.getScore());
                            //当前Activity销毁时，data这个意图就会传递给启动当前Activity的那个Activity
                            setResult(1, it);
                            //销毁当前Activity
                            finish();
                        }
                    })
                    .setNegativeButton("取消", null).show();
        }
    }
}
