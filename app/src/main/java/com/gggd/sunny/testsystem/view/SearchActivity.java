package com.gggd.sunny.testsystem.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
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

public class SearchActivity extends TitleActivity implements AdapterView.OnItemClickListener{

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
                .setItems(new String[]{"错题", "收藏",
                                "全部"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    case 0:
                                        type = 1;
                                        initData();
                                        initView();
                                        searchview.setFocusable(false);
                                        break;
                                    case 1:
                                        type = 2;
                                        initData();
                                        initView();
                                        searchview.setFocusable(false);
                                        break;
                                    case 2:
                                        type = 3;
                                        initData();
                                        initView();
                                        searchview.setFocusable(false);
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
        searchview.setFocusable(false);
        listv = (ListView) findViewById(R.id.lvshowquestion);
        adapter = new MyAdapter<Question>(this, list);
        listv.setAdapter(adapter);
        listv.setTextFilterEnabled(true);

        searchview.setQueryHint("请输入你要查询的关键字");
        searchview.setIconified(false);
        searchview.setIconifiedByDefault(false);
        listv.setOnItemClickListener(this);

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
                searchview.setFocusable(false);
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
            list = wrongAndCollectDB.getWrongandCollectNum(sql, librarynum);
        }
    }
    //监听返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setSelectItem(position);
        adapter.notifyDataSetInvalidated();
        Question question = list.get(position);
        String type =question.getType();
        String[] st;
        if(type.equals("1") || type.equals("2")) {
            st = new String[question.getScore()+2] ;
            Log.d("lz",question.getScore()+2+"  "+(question.getOption_c() == null)+
                    (question.getOption_e()== null));
            Log.d("lz",question.getScore()+3+"  "+("".equals(question.getOption_c()))+
                    ("".equals(question.getOption_e())));
            st[0] =  question.getTopic();
            st[1] = "A."+question.getOption_a();
            st[2] = "B."+question.getOption_b();
            if(!"".equals(question.getOption_c())){
                st[3] = "C."+question.getOption_c();
                if(!"".equals(question.getOption_d()) && null != question.getOption_d()){
                    st[4] = "D."+question.getOption_d();
                    if(!"".equals(question.getOption_e()) && null != question.getOption_e()){
                        st[5] = "E."+question.getOption_e();
                        if(!"".equals(question.getOption_f()) && null != question.getOption_f()){
                            st[6] = "F."+question.getOption_f();
                            st[7] = "正确答案:"+question.getOption_t();
                        }
                        else{
                            st[6] = "正确答案:"+question.getOption_t();
                        }
                    }
                    else{
                        st[5] = "正确答案:"+question.getOption_t();
                    }
                }
                else{
                    st[4] = "正确答案:"+question.getOption_t();
                }
            }else{
                st[3] = "正确答案:"+question.getOption_t();
            }

        }else{
            st = new String[2] ;
            st[0] =  question.getTopic();
            st[1] = "正确答案："+question.getOption_t();
        }
        new AlertDialog.Builder(SearchActivity.this, AlertDialog.THEME_HOLO_LIGHT)
                .setIcon(android.R.drawable.divider_horizontal_bright).
                setItems(st, null).show();
    }
}