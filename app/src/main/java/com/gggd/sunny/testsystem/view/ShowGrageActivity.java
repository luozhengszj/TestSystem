package com.gggd.sunny.testsystem.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gggd.sunny.testsystem.MainActivity;
import com.gggd.sunny.testsystem.R;
import com.gggd.sunny.testsystem.TitleActivity;
import com.gggd.sunny.testsystem.bean.Library;
import com.gggd.sunny.testsystem.bean.Question;
import com.gggd.sunny.testsystem.dao.LibraryAndTestDB;
import com.gggd.sunny.testsystem.dao.SelectQuestionDB;
import com.gggd.sunny.testsystem.dao.WrongAndCollectDB;

import java.util.ArrayList;

import static java.lang.String.valueOf;

/**
 * Created by Sunny on 2017/10/25.
 */

public class ShowGrageActivity extends TitleActivity {
    private ArrayList<Question> list;
    private ArrayList<Question> falselist;

    private TextView mtvshowgradetrue;
    private TextView mtvshowgradefalse;
    private TextView mtvtestgrade;
    private Button mbtnbutton_forward;
    private Button mbtnshowgradeall;
    private Button mbtnshowgradefalse;
    private Button mbtnshowgradecontinue;

    private String libraryname;
    private int test_id;
    private String time;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_grade_layout);
        showBackwardView(false);
        showForwardView(true);

        mtvshowgradetrue = (TextView) findViewById(R.id.tvshowgradetrue);
        mtvshowgradefalse = (TextView) findViewById(R.id.tvshowgradefalse);
        mtvtestgrade = (TextView) findViewById(R.id.tvtestgrade);
        mbtnbutton_forward = (Button) findViewById(R.id.button_forward);
        mbtnshowgradeall = (Button) findViewById(R.id.btnshowgradeall);
        mbtnshowgradefalse = (Button) findViewById(R.id.btnshowgradefalse);
        mbtnshowgradecontinue = (Button) findViewById(R.id.btnshowgradecontinue);
        mbtnbutton_forward.setOnClickListener(this);
        mbtnshowgradeall.setOnClickListener(this);
        mbtnshowgradefalse.setOnClickListener(this);
        mbtnshowgradecontinue.setOnClickListener(this);

        WrongAndCollectDB wrongAndCollectDB = new WrongAndCollectDB(this);

        mbtnbutton_forward.setText("主页");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        list = bundle.getParcelableArrayList("questionlist");
        libraryname = bundle.getString("libraryname");
        test_id = bundle.getInt("test_id");
        time = bundle.getString("date");
        setTitle(libraryname+"测验结果");

        int truenum = 0;
        int falsenum = 0;
        int count = 0;
        falselist = new ArrayList<>();
        //-----------------------------
            for (Question q : list) {
                if (q.getAnswer().equals(q.getOption_t())) {
                    truenum++;
                    count = count + q.getScore();
                    q.setWrong_flag(0);
                } else {
                    falsenum++;
                    q.setWrong_flag(1);
                    falselist.add(q);
                }
            }
        if(test_id != 0) {
            Library library = new Library();
            library.setId(test_id);
            library.setTime(time);
            library.setScore(""+count);
            wrongAndCollectDB.updateWrongQuestion(list,library);
            SharedPreferences sharedPreferences = getSharedPreferences("librarydata",
                    Activity.MODE_PRIVATE);
            // 使用getString方法获得value，注意第2个参数是value的默认值
            libraryname = sharedPreferences.getString("libraryname", "欢迎使用!");
            String testcount =sharedPreferences.getString("testcount", "您好");
            String testless =sharedPreferences.getString("testless", ""+0);
            //实例化SharedPreferences.Editor对象（第二步）
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //用putString的方法保存数据
            editor.putString("testless", Integer.parseInt(testless)+1+"");
            //提交当前数据
            editor.commit();
        }
        mtvshowgradetrue.setText(valueOf(truenum));
        mtvshowgradefalse.setText(valueOf(falsenum));
        mtvtestgrade.setText(valueOf(count));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_forward:
                intent = new Intent(ShowGrageActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btnshowgradeall:
                intent = new Intent(ShowGrageActivity.this,TestsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("questionlist", list);
                bundle.putString("libraryname",libraryname);
                bundle.putInt("test_id",0);
                intent.putExtras(bundle);
                ShowGrageActivity.this.startActivity(intent);
                break;
            case R.id.btnshowgradefalse:
                if (falselist.isEmpty()){
                    Toast.makeText(this, "本次考试并无错题！", Toast.LENGTH_SHORT).show();
                }else {
                    intent = new Intent(ShowGrageActivity.this, TestsActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putParcelableArrayList("questionlist", falselist);
                    bundle1.putString("libraryname", libraryname);
                    bundle1.putInt("test_id", 0);
                    intent.putExtras(bundle1);
                    ShowGrageActivity.this.startActivity(intent);
                }
                break;
            case R.id.btnshowgradecontinue:
                testContinue();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ShowGrageActivity.this, MainActivity.class);
            startActivity(intent);
            onDestroy();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //点击继续考试
    public void testContinue(){
        String sql = "select * from library where name=? and flag='0' limit 1";
        LibraryAndTestDB db = new LibraryAndTestDB(this);
        ArrayList<Library> list = db.getTestList(sql, new String[]{libraryname});
        if(list.isEmpty()){
            Toast.makeText(this, "该题库已考完！或不存在", Toast.LENGTH_SHORT).show();
        }else{
            //查找试卷
            Log.d("showlz",list.get(0).toString());
            SelectQuestionDB selectquestiondb = new SelectQuestionDB(this);
            String sql1 = "select * from question where test_id=?";
            ArrayList<Question> questionlist = selectquestiondb.selectAllQuestion(sql1, valueOf(list.get(0).getId()));
            Intent intent = new Intent(ShowGrageActivity.this, TestsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("questionlist", questionlist);
            bundle.putInt("test_id",list.get(0).getId());
            bundle.putString("libraryname",libraryname);
            intent.putExtras(bundle);
            startActivityForResult(intent, 100);
        }
    }
}
