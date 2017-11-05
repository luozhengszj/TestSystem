package com.gggd.sunny.testsystem.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.gggd.sunny.testsystem.MainActivity;
import com.gggd.sunny.testsystem.R;
import com.gggd.sunny.testsystem.TitleActivity;
import com.gggd.sunny.testsystem.bean.Question;
import com.gggd.sunny.testsystem.dao.WrongAndCollectDB;
import com.gggd.sunny.testsystem.tools.MyAdapter;

import java.util.ArrayList;

/**
 * Created by Sunny on 2017/11/3.
 */

public class SearchActivity extends TitleActivity {

    private SearchView searchview;
    private ArrayList<Question> list = new ArrayList<Question>();
    private ListView listv;
    private MyAdapter<Question> adapter;
    private int type;
    private int librarynum;

    private Button mbtn_farward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrong_collect_layout);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        librarynum = intent.getIntExtra("librarynum", -1);
        showForwardView(true);
        if (type == 1)
            setTitle("我的错题");
        else if (type == 2) {
            setTitle("我的收藏");
        } else {
            setTitle("全部题目");
        }
        mbtn_farward = (Button) findViewById(R.id.button_forward);
        mbtn_farward.setText("切换");
        initData();
        initView();

        mbtn_farward.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_forward:
                showADialog();
                break;
            case R.id.button_backward:
                super.onClick(v);
                break;
        }
    }

    public void showADialog() {
        new AlertDialog.Builder(SearchActivity.this, AlertDialog.THEME_HOLO_LIGHT)
                .setIcon(android.R.drawable.divider_horizontal_bright)
                .setItems(new String[]{"                        错      题", "                        收      藏",
                                "                        全      部"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    case 0:
                                        type = 1;
                                        initData();
                                        initView();
                                        break;
                                    case 1:
                                        type = 2;
                                        initData();
                                        initView();
                                        break;
                                    case 2:
                                        type = 3;
                                        initData();
                                        initView();
                                        break;
                                }
                            }
                        }).show();
    }

    private void initView() {
        // TODO Auto-generated method stub
        if (type == 1)
            setTitle("我的错题");
        else if (type == 2) {
            setTitle("我的收藏");
        } else {
            setTitle("全部题目");
        }
        searchview = (SearchView) findViewById(R.id.searchView);
        listv = (ListView) findViewById(R.id.lvshowquestion);
        adapter = new MyAdapter<Question>(this, list);
        listv.setAdapter(adapter);
        listv.setTextFilterEnabled(true);

        searchview.setQueryHint("请输入你要查询的关键字");
        searchview.setIconified(false);
        searchview.setIconifiedByDefault(false);

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryText) {
                if (queryText.length() >= 2) {
                    adapter.getFilter().filter(queryText);
                }
                return true;
            }
        });

    }

    private void initData() {
        if (librarynum == -1) {
            Intent intent11 = new Intent(SearchActivity.this, MainActivity.class);
            startActivity(intent11);
        } else {
            String sql;
            WrongAndCollectDB wrongAndCollectDB;
            if (type == 1) {
                sql = "select * from question where library_num=? and id in(select question_id from collect_wrong where wrong_flag=1)";
            } else if (type == 2) {
                sql = "select * from question where library_num=? and id in(select question_id from collect_wrong where collect_flag=1)";
            } else {
                sql = "select * from question where library_num=?";
            }
            wrongAndCollectDB = new WrongAndCollectDB(this);
            list = wrongAndCollectDB.getWrongandCollect(sql, librarynum);
        }
    }
}