package com.gggd.sunny.testsystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gggd.sunny.testsystem.bean.Library;
import com.gggd.sunny.testsystem.bean.Question;
import com.gggd.sunny.testsystem.dao.LibraryAndTestDB;
import com.gggd.sunny.testsystem.dao.SelectQuestionDB;
import com.gggd.sunny.testsystem.dao.WrongAndCollectDB;
import com.gggd.sunny.testsystem.tools.FileCreate;
import com.gggd.sunny.testsystem.tools.QRcodeDialog;
import com.gggd.sunny.testsystem.view.SearchActivity;
import com.gggd.sunny.testsystem.view.ShowTestsActivity;
import com.gggd.sunny.testsystem.view.SwitchLibraryActivity;
import com.gggd.sunny.testsystem.view.TestsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.gggd.sunny.testsystem.R.id.btnallquestiontitle;

public class MainActivity extends TitleActivity implements View.OnClickListener {
    private Button btninputlibrary;
    private TextView mtvlibraryname;
    private TextView mtvlibrarypercent;
    private Button btnbegintest;
    private Button mbtntestrecord;
    private Button mbtncollecttitle;
    private Button mbtnwrongtest;
    private Button mbtnwrongtitle;
    private Button mbtnallquestiontitle;
    private Button mbtnqiehuan;
    private Button mbtndeletelibrary;
    private Button moutquestion;
    private Button mbtnabout;

    private String libraryname;
    private LibraryAndTestDB libraryAndTestDB;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        setTitle("考试系统");
        showBackwardView(false);
        showForwardView(false);

        mtvlibraryname = (TextView) findViewById(R.id.tvlibraryname);
        mtvlibrarypercent = (TextView) findViewById(R.id.tvpercent);
        btnbegintest = (Button) findViewById(R.id.btnbegintest);
        mbtntestrecord = (Button) findViewById(R.id.btntestrecord);
        mbtncollecttitle = (Button) findViewById(R.id.btncollecttitle);
        mbtnwrongtitle = (Button) findViewById(R.id.btnwrongtitle);
        mbtnwrongtest = (Button) findViewById(R.id.btnwrongtest);
        mbtnallquestiontitle = (Button) findViewById(btnallquestiontitle);
        mbtnqiehuan = (Button) findViewById(R.id.btnqiehuan);
        mbtndeletelibrary = (Button) findViewById(R.id.btndeletelibrary);
        moutquestion = (Button) findViewById(R.id.outquestion);
        mbtnabout = (Button) findViewById(R.id.btnabout);

        verifyStoragePermissions(MainActivity.this);
        SharedPreferences sharedPreferences = getSharedPreferences("librarydata",
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        libraryname = sharedPreferences.getString("libraryname", "欢迎使用");


        libraryAndTestDB = new LibraryAndTestDB(this);
        libraryAndTestDB.existLibrary(libraryname);
        String tmpflag = libraryAndTestDB.existLibrary(libraryname);
        String tmppercent = "";
        if (tmpflag.equals("0"))
            tmppercent = "您好";
        else {
            tmppercent = libraryAndTestDB.getPercent(libraryname);
        }
        mtvlibraryname.setText(libraryname);
        mtvlibrarypercent.setText(tmppercent);


        btninputlibrary = (Button) findViewById(R.id.btninputlibrary);
        btninputlibrary.setOnClickListener(this);
        btnbegintest.setOnClickListener(this);
        mbtntestrecord.setOnClickListener(this);
        mbtncollecttitle.setOnClickListener(this);
        mbtnwrongtest.setOnClickListener(this);
        mbtnwrongtitle.setOnClickListener(this);
        mtvlibrarypercent.setOnClickListener(this);
        mtvlibraryname.setOnClickListener(this);
        mbtnallquestiontitle.setOnClickListener(this);
        mbtnqiehuan.setOnClickListener(this);
        mbtndeletelibrary.setOnClickListener(this);
        moutquestion.setOnClickListener(this);
        mbtnabout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        libraryname = mtvlibraryname.getText().toString();
        switch (v.getId()) {
            case R.id.btninputlibrary:
                intent = new Intent(MainActivity.this, InputLibraryActivity.class);
                startActivity(intent);
                break;
            case R.id.btnbegintest:
                checkBeginTest();
                break;
            case R.id.tvpercent:
                if (libraryname.equals("欢迎使用"))
                    Toast.makeText(this, "该题库已考完！或不存在", Toast.LENGTH_SHORT).show();
                else {
                    intent = new Intent(MainActivity.this, ShowTestsActivity.class);
                    intent.putExtra("libraryname", libraryname);
                    startActivity(intent);
                }
                break;
            case R.id.btntestrecord:
                if (libraryname.equals("欢迎使用"))
                    Toast.makeText(this, "该题库已考完！或不存在", Toast.LENGTH_SHORT).show();
                else {
                    intent = new Intent(MainActivity.this, ShowTestsActivity.class);
                    intent.putExtra("libraryname", libraryname);
                    startActivity(intent);
                }
                break;
            case R.id.btnwrongtest:
                wrongTest();
                break;
            case R.id.btnwrongtitle:
                intoSearch(1);
                break;
            case R.id.btncollecttitle:
                intoSearch(2);
                break;
            case R.id.btnallquestiontitle:
                intoSearch(3);
                break;
            case R.id.btnqiehuan:
                showADialog();
                break;
            case R.id.btndeletelibrary:
                deleteLibrary();
                break;
            case R.id.outquestion:
                outquestion();
                break;
            case R.id.btnabout:
                anboutOus();
                break;
            case R.id.tvlibraryname:
                showLibrarInfo();
                break;
        }
    }

    //点击开始考试
    public void checkBeginTest() {
        Intent intent;
        String sql = "select * from library where name=? and flag='0' limit 1";
        libraryAndTestDB = new LibraryAndTestDB(this);
        ArrayList<Library> list = libraryAndTestDB.getTestList(sql, new String[]{libraryname});
        if (list.isEmpty()) {
            Toast.makeText(this, "该题库已考完！或不存在", Toast.LENGTH_SHORT).show();
        } else {
            //查找试卷
            Log.d("showlz", list.get(0).toString());
            SelectQuestionDB selectquestiondb = new SelectQuestionDB(this);
            String sql1 = "select * from question where test_id=?";
            Log.d("lz", list.get(0).getId() + "");
            ArrayList<Question> questionlist = selectquestiondb.selectAllQuestion(sql1, list.get(0).getId() + "");
            intent = new Intent(MainActivity.this, TestsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("questionlist", questionlist);
            bundle.putInt("test_id", list.get(0).getId());
            bundle.putString("libraryname", libraryname);
            intent.putExtras(bundle);
            startActivityForResult(intent, 100);
        }
    }

    //错题考试，查找出错题试卷并且跳转到考试界面。
    //点击开始考试
    public void wrongTest() {
        if (libraryname.equals("欢迎使用"))
            Toast.makeText(this, "该题库已考完！或不存在", Toast.LENGTH_SHORT).show();
        else {
            Intent intent;
            SelectQuestionDB selectquestiondb = new SelectQuestionDB(this);
            ArrayList<Question> wronglist = selectquestiondb.selectWrongQuestion(libraryname);
            if (wronglist.isEmpty()) {
                Toast.makeText(this, "暂无错题！", Toast.LENGTH_SHORT).show();
            } else {
                intent = new Intent(MainActivity.this, TestsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("questionlist", wronglist);
                bundle.putInt("test_id", -2);
                bundle.putString("libraryname", libraryname);
                intent.putExtras(bundle);
                startActivityForResult(intent, 100);
            }
        }
    }

    //查找
    public void intoSearch(int type) {
        WrongAndCollectDB wrongAndCollectDB = new WrongAndCollectDB(this);
        int librarynum = wrongAndCollectDB.getLibraryNum(libraryname);
        if (librarynum != -1) {
            Intent intent11 = new Intent(MainActivity.this, SearchActivity.class);
            intent11.putExtra("librarynum", librarynum);
            intent11.putExtra("type", type);
            startActivity(intent11);
        } else {
            Toast.makeText(this, "先增加题库", Toast.LENGTH_SHORT).show();
        }
    }

    //切换题库
    public void showADialog() {
        if (libraryname.equals("欢迎使用"))
            Toast.makeText(this, "先增加题库", Toast.LENGTH_SHORT).show();
        else {
            Intent intent11 = new Intent(MainActivity.this, SwitchLibraryActivity.class);
            intent11.putExtra("librarname", libraryname);
            startActivityForResult(intent11, 20);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    String returnedData = data.getStringExtra("beginerror");
                    Toast.makeText(MainActivity.this, returnedData, Toast.LENGTH_LONG).show();
                }
                break;
            case 20:
                if (data != null) {
                    String name = data.getStringExtra("libraryname");
                    String testok = data.getStringExtra("testok");
                    String testnot = data.getStringExtra("testnot");
                    SharedPreferences mySharedPreferences = this.getSharedPreferences("librarydata",
                            Activity.MODE_PRIVATE);
                    //实例化SharedPreferences.Editor对象（第二步）
                    SharedPreferences.Editor editor = mySharedPreferences.edit();

                    editor.putString("testcount", Integer.parseInt(testok) + Integer.parseInt(testnot) + "");
                    editor.putString("libraryname", name);
                    editor.putString("testless", testok);
                    //提交当前数据
                    editor.commit();
                    mtvlibraryname.setText(name);
                    int tmpcount = Integer.parseInt(testok) + Integer.parseInt(testnot);
                    mtvlibrarypercent.setText(testok + "/" + tmpcount);
                }
                break;
            default:
        }
    }

    //删除题库
    public void deleteLibrary() {
        libraryAndTestDB = new LibraryAndTestDB(this);
        final EditText et = new EditText(this);
        final SharedPreferences mySharedPreferences = this.getSharedPreferences("librarydata",
                Activity.MODE_PRIVATE);
        new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setTitle("输入需要删除的题库名")
                .setIcon(null)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        int flag = libraryAndTestDB.deleteLibrary(input);
                        if (flag == -1) {
                            Toast.makeText(MainActivity.this, "不存在该题库", Toast.LENGTH_SHORT).show();
                        } else {
                            String tmplibrayname = libraryAndTestDB.deleteLibrary2(flag);
                            Toast.makeText(MainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                            if (input.equals(libraryname)) {
                                //实例化SharedPreferences.Editor对象（第二步）
                                SharedPreferences.Editor editor = mySharedPreferences.edit();
                                if (tmplibrayname.equals(" ")) {
                                    libraryname = "欢迎使用";
                                    mtvlibraryname.setText("欢迎使用");
                                    mtvlibrarypercent.setText("");
                                    editor.putString("libraryname", "欢迎使用");
                                    editor.commit();
                                } else {
                                    String tmppercent = libraryAndTestDB.getPercent(tmplibrayname);
                                    libraryname = tmplibrayname;
                                    mtvlibraryname.setText(tmplibrayname);
                                    mtvlibrarypercent.setText(tmppercent);
                                    editor.putString("libraryname", tmplibrayname);
                                    editor.commit();
                                }
                            }
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    //导出题库
    public void outquestion() {
        new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT)
                .setIcon(android.R.drawable.divider_horizontal_bright)
                .setItems(new String[]{ "收藏(.txt)",
                                "错题(.txt)","  ","收藏(.xls)",
                                "错题(.xls)"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    case 0:
                                        outQuestion(0);
                                        break;
                                    case 1:
                                        outQuestion(1);
                                        break;
                                    case 3:
                                        outQuestion(3);
                                    case 4:
                                        outQuestion(4);
                                        break;
                                }
                            }
                        }).show();
    }

    public void outQuestion(int type) {
        if (libraryname.equals("欢迎使用"))
            Toast.makeText(this, "先增加题库", Toast.LENGTH_SHORT).show();
        else {
            SelectQuestionDB selectdb = new SelectQuestionDB(MainActivity.this);
            int tmpnum = selectdb.getLibrarynum(libraryname);
            String sql;
            ArrayList<Question> qlist = new ArrayList<>();
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            SimpleDateFormat sDateFormat = new SimpleDateFormat("MM-dd hh-mm");
            String date = sDateFormat.format(curDate);
            FileCreate f = new FileCreate();
            int daochunum = 0;
            if (type == 0) {
                sql = "select * from question where library_num=" + tmpnum + " and id in(select question_id from collect_wrong where collect_flag='1')";
                qlist = selectdb.getQuestion(sql);
                daochunum = f.createTXT(qlist, libraryname + " " + date);
            } else if(type == 1) {
                sql = "select * from question where library_num=" + tmpnum + " and id in(select question_id from collect_wrong where wrong_flag='1')";
                qlist = selectdb.getQuestion(sql);
                daochunum = f.createTXT(qlist, libraryname + " " + date);
            }else if(type == 3) {
                sql = "select * from question where library_num=" + tmpnum + " and id in(select question_id from collect_wrong where collect_flag='1')";
                qlist = selectdb.getQuestion(sql);
                daochunum = f.createExcel(qlist, libraryname + " " + date);
            } else {
                sql = "select * from question where library_num=" + tmpnum + " and id in(select question_id from collect_wrong where wrong_flag='1')";
                qlist = selectdb.getQuestion(sql);
                daochunum = f.createExcel(qlist, libraryname + " " + date);
            }


            Toast.makeText(MainActivity.this, "导出了" + daochunum + "条数据", Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT)
                    .setIcon(android.R.drawable.divider_horizontal_bright)
                    .setItems(new String[]{"保存在SD>TestSystem>."}, null).show();
        }
    }

    //关于我们
    public void anboutOus() {
        new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT)
                .setIcon(android.R.drawable.divider_horizontal_bright)
                .setItems(new String[]{"使用说明",
                                "1.导入文件仅接受.xls",
                                "2.格式:题目、选项(最多六个)、答案;excel第一行即为导入第一道题;格式:题目、A、B、C、D、E、F、正确答案各一列",
                                "3.正确答案格式：A（单选）、AB（多选）、对或错（判断）",
                                "4.excel第一行即为导入第一道题，导出文件于SD卡/TestSystem/",
                                "5.GitHub name,luozhengszj.",
                                "二维码 （失效）   --end"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 6 ){
                                    Resources res=getResources();
                                    Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.hechengqr);// 这里是获取图片Bitmap，也可以传入其他参数到Dialog中
                                    QRcodeDialog.Builder dialogBuild = new QRcodeDialog.Builder(MainActivity.this);
                                    dialogBuild.setImage(bmp);
                                    QRcodeDialog dialog1 = dialogBuild.create();
                                    dialog1.setCanceledOnTouchOutside(true);// 点击外部区域关闭
                                    dialog1.show();
                                }
                            }
                        }).show();
    }
    //题库信息
    public void showLibrarInfo(){
        String[] ss;
        if(libraryname.equals("欢迎使用") ){
            ss = new String[]{
                    "GitHub name,luozhengszj.",
                    "by sunny"};
        }else{
            libraryAndTestDB = new LibraryAndTestDB(this);
            ss = libraryAndTestDB.getLibraryInfo(libraryname);
            if(ss == null){
                ss = new String[]{
                        "GitHub name,luozhengszj.",
                        "by sunny"};
            }
        }
        new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT)
                .setIcon(android.R.drawable.divider_horizontal_bright)
                .setItems(ss,null).show();
    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //监听返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long clickTime = 0;

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再次返回退出程序", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            Intent intent = new Intent("com.gggd.sunny.activity");
            intent.putExtra("closeAll", 1);
            sendBroadcast(intent);//发送广播
            finish();
        }
    }

}
