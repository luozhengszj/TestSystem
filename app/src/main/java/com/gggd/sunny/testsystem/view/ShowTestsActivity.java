package com.gggd.sunny.testsystem.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gggd.sunny.testsystem.R;
import com.gggd.sunny.testsystem.TitleActivity;
import com.gggd.sunny.testsystem.bean.Library;
import com.gggd.sunny.testsystem.bean.Question;
import com.gggd.sunny.testsystem.dao.LibraryAndTestDB;
import com.gggd.sunny.testsystem.dao.SelectQuestionDB;
import com.gggd.sunny.testsystem.tools.MyArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;


/**
 * Created by Sunny on 2017/10/30.
 */

public class ShowTestsActivity extends TitleActivity implements AdapterView.OnItemClickListener {

    private ArrayList<Map<String,String>> arrayList;
    private ArrayList<Library> librarylist;
    private String libraryname;
    private LibraryAndTestDB librarydb;

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_tests_layout);
        setTitle("考试记录");
        showForwardView(false);
        Intent intent = getIntent();
        libraryname = intent.getStringExtra("libraryname");
        listView = (ListView) findViewById(R.id.lvshowtests);               //获取id是list的ListView

        listView.setAdapter(new MyArrayAdapter(this,initAdapter()));                //自动为id是list的ListView设置适配器
        listView.setOnItemClickListener(this);
    }

    /**
     * 初始化适配器
     */
    private ArrayList<Map<String, String>> initAdapter() {
        librarydb = new LibraryAndTestDB(this);
        String sql = "select id,time,score from library where flag = 1 and name=?";
        arrayList = new ArrayList<>();
        librarylist =librarydb.getTestsList(sql,new String[]{libraryname});
        int size = librarylist.size();
        for(int i = 0;i<size;i++){
            Map<String,String> map = new HashMap<>();
            map.put("number","试题 "+ valueOf(i+1));
            map.put("time",librarylist.get(i).getTime());
            map.put("score",librarylist.get(i).getScore());
            arrayList.add(map);
        }
        return arrayList;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        int type = -2;

        Library library = librarylist.get(position);
        String sql1 = "select * from question where test_id=?";
        String sql2 = "select * from question where test_id=? and wrong_flag = 1";
        SelectQuestionDB selectquestiondb = new SelectQuestionDB(this);

        ArrayList<Question> questionlist1 = selectquestiondb.selectAllQuestion(sql1, valueOf(library.getId()));
        ArrayList<Question> questionlist2 = selectquestiondb.selectAllQuestion(sql2, valueOf(library.getId()));
        final ArrayList<Question> finalQuestionlist1 = questionlist1;
        final ArrayList<Question> finalQuestionlist2 = questionlist2;
        new AlertDialog.Builder(ShowTestsActivity.this, AlertDialog.THEME_HOLO_LIGHT).setTitle("可点击屏幕外回到列表")
                .setIcon(android.R.drawable.divider_horizontal_bright)
                .setPositiveButton("查看全部", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent it = new Intent(ShowTestsActivity.this, AllAndWrongActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("questionlist", finalQuestionlist1);
                        bundle.putString("libraryname",libraryname);
                        Log.d("lz",""+finalQuestionlist1.size());
                        bundle.putInt("test_id",-1);
                        bundle.putBoolean("flag",true);
                        it.putExtras(bundle);
                        ShowTestsActivity.this.startActivity(it);
                    }
                })
                .setNegativeButton("只看错题", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent it = new Intent(ShowTestsActivity.this, AllAndWrongActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("questionlist", finalQuestionlist2);
                        bundle.putString("libraryname",libraryname);
                        Log.d("lz",""+finalQuestionlist2.size());
                        bundle.putInt("test_id",-1);
                        bundle.putBoolean("flag",true);
                        it.putExtras(bundle);
                        ShowTestsActivity.this.startActivity(it);
                    }
                }).show();
    }
}
