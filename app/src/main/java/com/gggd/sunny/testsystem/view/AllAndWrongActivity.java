package com.gggd.sunny.testsystem.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gggd.sunny.testsystem.MainActivity;
import com.gggd.sunny.testsystem.R;
import com.gggd.sunny.testsystem.TitleActivity;
import com.gggd.sunny.testsystem.bean.Question;
import com.gggd.sunny.testsystem.dao.WrongAndCollectDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 2017/11/1.
 */

public class AllAndWrongActivity extends TitleActivity implements
        AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {
    private Button mbtnbutton_forward;
    private Button mbtnbutton_back;
    private TextView mtvquestiontruetext;
    private TextView mtvquestiontrue;

    private Button mbtnabovetquestion;
    private Button mbtnnextquestion;
    private CheckBox mcbncollectquestion;
    private TextView mtvquestionlist;
    private TextView mtvtopic;
    private RadioGroup mrgquestionsingle;
    private RadioButton mrbquestionsingle1;
    private RadioButton mrbquestionsingle2;
    private RadioButton mrbquestionsingle3;
    private RadioButton mrbquestionsingle4;
    private RadioGroup mrgquestionjedge;
    private RadioButton mrbquestionjedge1;
    private RadioButton mrbquestionjedge2;
    private View mlayoutcheck;
    private CheckBox mcbquestionmultiple1;
    private CheckBox mcbquestionmultiple2;
    private CheckBox mcbquestionmultiple3;
    private CheckBox mcbquestionmultiple4;
    private CheckBox mcbquestionmultiple5;
    private CheckBox mcbquestionmultiple6;

    private ArrayList<Question> list;
    private ListView mlvquestionlist;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private List<CheckBox> checkBoxList;

    private int test_id;
    private String libraryname;
    private int nownum;
    private int size;
    private Question questionnow;
    private boolean flag;
    private boolean backflag = false;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.begintest_layout);
        mbtnbutton_forward = (Button) findViewById(R.id.button_forward);
        mtvquestionlist = (TextView) findViewById(R.id.tvquestionlist);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        list = bundle.getParcelableArrayList("questionlist");
        libraryname = bundle.getString("libraryname");
        setTitle(libraryname);
        showForwardView(true);
        test_id = bundle.getInt("test_id");
        backflag = bundle.getBoolean("flag");
        nownum = 1;
        size = list.size();

        mbtnabovetquestion = (Button) findViewById(R.id.btnabovetquestion);
        mbtnnextquestion = (Button) findViewById(R.id.btnnextquestion);
        mtvquestionlist = (TextView) findViewById(R.id.tvquestionlist);
        mtvtopic = (TextView) findViewById(R.id.tvtopic);
        mrgquestionsingle = (RadioGroup) findViewById(R.id.rgquestionsingle);
        mrbquestionsingle1 = (RadioButton) findViewById(R.id.rbquestionsingle1);
        mrbquestionsingle2 = (RadioButton) findViewById(R.id.rbquestionsingle2);
        mrbquestionsingle3 = (RadioButton) findViewById(R.id.rbquestionsingle3);
        mrbquestionsingle4 = (RadioButton) findViewById(R.id.rbquestionsingle4);
        mrgquestionjedge = (RadioGroup) findViewById(R.id.rgquestionjedge);
        mrbquestionjedge1 = (RadioButton) findViewById(R.id.rbquestionjedge1);
        mrbquestionjedge2 = (RadioButton) findViewById(R.id.rbquestionjedge2);
        mlayoutcheck = findViewById(R.id.layoutcheck);
        mcbquestionmultiple1 = (CheckBox) findViewById(R.id.cbquestionmultiple1);
        mcbquestionmultiple2 = (CheckBox) findViewById(R.id.cbquestionmultiple2);
        mcbquestionmultiple3 = (CheckBox) findViewById(R.id.cbquestionmultiple3);
        mcbquestionmultiple4 = (CheckBox) findViewById(R.id.cbquestionmultiple4);
        mcbquestionmultiple5 = (CheckBox) findViewById(R.id.cbquestionmultiple5);
        mcbquestionmultiple6 = (CheckBox) findViewById(R.id.cbquestionmultiple6);
        mcbncollectquestion = (CheckBox) findViewById(R.id.cbcollect);

        mtvquestiontrue = (TextView) findViewById(R.id.tvquestiontrue);

        checkBoxList = new ArrayList<>();
        checkBoxList.add(mcbquestionmultiple1);
        checkBoxList.add(mcbquestionmultiple2);
        checkBoxList.add(mcbquestionmultiple3);
        checkBoxList.add(mcbquestionmultiple4);
        checkBoxList.add(mcbquestionmultiple5);
        checkBoxList.add(mcbquestionmultiple6);
        //事件
        mbtnabovetquestion.setOnClickListener(this);
        mbtnnextquestion.setOnClickListener(this);
        mtvquestionlist.setOnClickListener(this);
        mbtnbutton_forward.setOnClickListener(this);
        mcbncollectquestion.setOnCheckedChangeListener(this);

        //拉出抽屉

        //test_id == 0,这是错题查看、所有题目查看的时候
        //浏览题目，只需要上一题、下一题、题目跳转、根据答案选题目
        //所以事件只需要上一题、下一题、题目跳转，不需要监控选项变化
        //同时设置“交卷”为“返回”，返回（包括返回键）即返回到主页
        //test_id !=0的时候，说明是考试（在这里就是错题考试）
        mlvquestionlist = (ListView) findViewById(R.id.test_drawer);
        if (test_id == 0) {
            flag = true;
            for (int i = 0; i < size; i++) {
                questionnow = list.get(i);
                if (!questionnow.getAnswer().equals(questionnow.getOption_t())) {
                    nownum = i + 1;
                    isCollect(questionnow.getCollect_flag());
                    mtvquestiontrue.setText(questionnow.getOption_t());
                    selectTypeShow(questionnow);
                    break;
                }
            }

        } else{
            flag = false;
            selectTypeShow(list.get(0));
            isCollect(list.get(0).getCollect_flag());
            mtvquestiontrue.setText(list.get(0).getOption_t());
        }

        mlvquestionlist.setOnItemClickListener(this);
        mtvquestionlist.setOnClickListener(this);
        mlvquestionlist.addHeaderView(new View(this));
        arrayList = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            arrayList.add(i + "  " + list.get(i - 1).getAnswer());
        }
        adapter = new ArrayAdapter<>(AllAndWrongActivity.this, android.R.layout.simple_list_item_1, arrayList);
        mlvquestionlist.setAdapter(adapter);

        mtvquestionlist.setText(nownum + "/" + size);
        showBackwardView(true);
        mbtnbutton_forward.setText("主页");
        mtvquestiontruetext = (TextView) findViewById(R.id.tvquestiontruetext);
        mbtnbutton_back = (Button) findViewById(R.id.button_backward);
        mbtnbutton_back.setOnClickListener(this);
        mtvquestiontruetext.setVisibility(View.VISIBLE);
        mtvquestiontrue.setVisibility(View.VISIBLE);
        cannotSelect();

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.testdrawer_layout);
        drawerLayout.setOnTouchListener(this);//将主容器的监听交给本activity，本activity再交给mGestureDetector
        drawerLayout.setLongClickable(true);   //必需设置这为true 否则也监听不到手势
        mGestureDetector = new GestureDetector(this, myGestureListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnabovetquestion:
                if (nownum == 1) {
                    break;
                } else {
                    if (flag) {
                        nownum = nownum - 1;
                        for (int i = nownum; i > 0; i--) {
                            questionnow = list.get(i - 1);
                            if (!questionnow.getAnswer().equals(questionnow.getOption_t())) {
                                nownum = i;
                                mtvquestionlist.setText(nownum + "/" + size);
                                selectTypeShow(questionnow);
                                break;
                            }
                        }
                    } else {
                        nownum--;
                        questionnow = list.get(nownum - 1);
                        mtvquestionlist.setText(nownum + "/" + size);
                        selectTypeShow(questionnow);
                    }
                    break;
                }
            case R.id.btnnextquestion:
                if (nownum == size) {
                    break;
                } else {
                    if (flag) {
                        nownum = nownum + 1;
                        for (int i = nownum; i <= size; i++) {
                            questionnow = list.get(i - 1);
                            if (!questionnow.getAnswer().equals(questionnow.getOption_t())) {
                                nownum = i;
                                mtvquestionlist.setText(nownum + "/" + size);
                                selectTypeShow(questionnow);
                                break;
                            }
                        }
                    } else {
                        nownum = nownum + 1;
                        questionnow = list.get(nownum - 1);
                        mtvquestionlist.setText(nownum + "/" + size);
                        selectTypeShow(questionnow);
                    }
                    break;
                }
            case R.id.button_forward:
                updateCollect(list);
                Intent intent = new Intent(AllAndWrongActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.button_backward:
                updateCollect(list);
                super.onClick(v);
                break;
        }
    }

    //这是题目的显示
    public void selectTypeShow(Question question) {
        String type = question.getType();
        String selectTypeShowanswer = question.getAnswer();
        mtvquestiontrue.setText(question.getOption_t());
        if ("1".equals(question.getCollect_flag())) {
            mcbncollectquestion.setChecked(true);
        } else {
            mcbncollectquestion.setChecked(false);
        }
        if (type.equals("1")) {
            mlayoutcheck.setVisibility(View.GONE);
            mrgquestionjedge.setVisibility(View.GONE);
            mrgquestionsingle.clearCheck();
            mrgquestionsingle.setVisibility(View.VISIBLE);
            mtvtopic.setText(nownum + "." + question.getTopic());
            mrbquestionsingle1.setText(question.getOption_a());
            mrbquestionsingle2.setText(question.getOption_b());
            mrbquestionsingle3.setText(question.getOption_c());
            mrbquestionsingle4.setText(question.getOption_d());
            if (selectTypeShowanswer.equals("A")) {
                mrbquestionsingle1.setChecked(true);
            } else if (selectTypeShowanswer.equals("B")) {
                mrbquestionsingle2.setChecked(true);
            } else if (selectTypeShowanswer.equals("C")) {
                mrbquestionsingle3.setChecked(true);
            } else if (selectTypeShowanswer.equals("D")) {
                mrbquestionsingle4.setChecked(true);
            }
        } else if (type.equals("3")) {
            mlayoutcheck.setVisibility(View.GONE);
            mrgquestionsingle.setVisibility(View.GONE);
            mrgquestionjedge.clearCheck();
            mrgquestionjedge.setVisibility(View.VISIBLE);
            mtvtopic.setText(nownum + "." + question.getTopic());
            mrbquestionjedge1.setText(question.getOption_a());
            mrbquestionjedge2.setText(question.getOption_b());
            if (selectTypeShowanswer.equals("对")) {
                mrbquestionjedge1.setChecked(true);
            } else if (selectTypeShowanswer.equals("错")) {
                mrbquestionjedge2.setChecked(true);
            }
        } else if (type.equals("2")) {
            mrgquestionjedge.setVisibility(View.GONE);
            mrgquestionsingle.setVisibility(View.GONE);
            mcbquestionmultiple1.setChecked(false);
            mcbquestionmultiple2.setChecked(false);
            mcbquestionmultiple3.setChecked(false);
            mcbquestionmultiple4.setChecked(false);
            mcbquestionmultiple5.setChecked(false);
            mcbquestionmultiple6.setChecked(false);
            mlayoutcheck.setVisibility(View.VISIBLE);
            mtvtopic.setText(nownum + "." + question.getTopic());

            mcbquestionmultiple1.setText(question.getOption_a());
            mcbquestionmultiple2.setText(question.getOption_b());
            mcbquestionmultiple3.setText(question.getOption_c());
            mcbquestionmultiple4.setText(question.getOption_d());
            mcbquestionmultiple5.setText(question.getOption_e());
            mcbquestionmultiple6.setText(question.getOption_f());
            int checkBoxListsize = checkBoxList.size();
            if (!selectTypeShowanswer.equals(" ")) {
                String[] sss = selectTypeShowanswer.split("");
                for (int i = 1; i < sss.length; i++) {
                    checkBoxList.get((sss[i].charAt(0) - 65)).setChecked(true);
                }
            }
            for (int i = checkBoxListsize - 1; i < 6; i++) {
                checkBoxList.get(i).setVisibility(View.GONE);
            }
            for (int i = 0; i < checkBoxListsize; i++) {
                checkBoxList.get(i).setVisibility(View.VISIBLE);
            }
        }
    }

    //不是做题的时候，是查看错题的时候，检查是否为收藏的题目
    public void isCollect(String collect_flag) {
        if ("1".equals(collect_flag)) {
            mcbncollectquestion.setChecked(true);
        } else {
            mcbncollectquestion.setChecked(false);
        }
    }

    //查看题目（错题），禁止选项被动
    public void cannotSelect() {
        mcbquestionmultiple1.setEnabled(false);
        mcbquestionmultiple2.setEnabled(false);
        mcbquestionmultiple3.setEnabled(false);
        mcbquestionmultiple4.setEnabled(false);
        mcbquestionmultiple5.setEnabled(false);
        mcbquestionmultiple6.setEnabled(false);
        mrbquestionsingle1.setEnabled(false);
        mrbquestionsingle2.setEnabled(false);
        mrbquestionsingle3.setEnabled(false);
        mrbquestionsingle4.setEnabled(false);
        mrbquestionjedge1.setEnabled(false);
        mrbquestionjedge2.setEnabled(false);
    }

    //监听返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            updateCollect(list);
            Log.d("lz",backflag+"");
            if(backflag)
            {
                this.finish();
            }else {
                Intent it = new Intent(AllAndWrongActivity.this, ShowGrageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("questionlist", list);
                bundle.putString("libraryname", libraryname);
                bundle.putInt("test_id", test_id);
                it.putExtras(bundle);
                AllAndWrongActivity.this.startActivity(it);
                this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mtvquestionlist.setText(position + "/" + size);
        Question questiontmp = list.get(position-1);
        if(flag) {
            if (!questiontmp.getAnswer().equals(questiontmp.getOption_t())) {
                selectTypeShow(questiontmp);
                nownum = position ;
            }
            else{
                Toast.makeText(this, "该题不是错题，请对照题号！", Toast.LENGTH_SHORT).show();
            }
        }else {
            nownum = position ;
            selectTypeShow(questiontmp);
        }
    }

    //是否收藏，这是对
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            list.get(nownum - 1).setCollect_flag("1");
        } else {
            list.get(nownum - 1).setCollect_flag("0");
        }
    }

    //对于浏览题目时候的收藏，也要添加到数据库
    //两种退出都要收藏
    public void updateCollect(ArrayList<Question> list) {
        WrongAndCollectDB wrongAndCollectDB = new WrongAndCollectDB(this);
        wrongAndCollectDB.updateCollectQuestion(list);
    }
    private static final int FLING_MIN_DISTANCE = 50;   //最小距离
    private static final int FLING_MIN_VELOCITY = 0;  //最小速度
    GestureDetector.SimpleOnGestureListener myGestureListener = new GestureDetector.SimpleOnGestureListener(){
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float x = e1.getX()-e2.getX();
            float x2 = e2.getX()-e1.getX();
            //往左滑动
            if(x>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY){
                if (nownum != size) {
                    nownum = nownum + 1;
                    questionnow = list.get(nownum - 1);
                    mtvquestionlist.setText(nownum + "/" + size);
                    selectTypeShow(questionnow);
                }
            }
            //往右滑动
            else if(x2>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY){
                if (nownum != 1) {
                    nownum = nownum - 1;
                    questionnow = list.get(nownum - 1);
                    mtvquestionlist.setText(nownum + "/" + size);
                    selectTypeShow(questionnow);
                }
            }
            return false;
        }
    };
    //下面是实现OnTouch方法 并将处理touch时间交给mGestureDetector
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return mGestureDetector.onTouchEvent(event);
    }
}
