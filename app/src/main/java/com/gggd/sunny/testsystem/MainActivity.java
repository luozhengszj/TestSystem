package com.gggd.sunny.testsystem;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gggd.sunny.testsystem.bean.Library;
import com.gggd.sunny.testsystem.bean.Question;
import com.gggd.sunny.testsystem.dao.LibraryAndTestDB;
import com.gggd.sunny.testsystem.dao.SelectQuestionDB;
import com.gggd.sunny.testsystem.view.ShowTestsActivity;
import com.gggd.sunny.testsystem.view.TestsActivity;

import java.util.ArrayList;

import static java.lang.String.valueOf;


public class MainActivity extends TitleActivity  implements View.OnClickListener{
    private Button btninputlibrary;
    private TextView mtvlibraryname ;
    private TextView mtvlibrarypercent;
    private Button btnbegintest;
    private Button mbtntestrecord;
    private Button mbtncollecttitle;
    private Button mbtnwrongtest;
    private Button mbtnwrongtitle;

    private String libraryname;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

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

        verifyStoragePermissions(MainActivity.this);
        SharedPreferences sharedPreferences= getSharedPreferences("librarydata",
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        libraryname =sharedPreferences.getString("libraryname", "欢迎使用!");
        String testcount =sharedPreferences.getString("testcount", "您好");
        String testxg =sharedPreferences.getString("testxg", "");
        String testless =sharedPreferences.getString("testless", "");
        mtvlibraryname.setText(libraryname);
        mtvlibrarypercent.setText(testless+testxg+testcount);

        btninputlibrary = (Button) findViewById(R.id.btninputlibrary);
        btninputlibrary.setOnClickListener(this);
        btnbegintest.setOnClickListener(this);
        mbtntestrecord.setOnClickListener(this);
        mbtncollecttitle.setOnClickListener(this);
        mbtnwrongtest.setOnClickListener(this);
        mbtnwrongtitle.setOnClickListener(this);
        mtvlibrarypercent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btninputlibrary:
                intent =new Intent(MainActivity.this,InputLibraryActivity.class);
                startActivity(intent);
                break;
            case R.id.btnbegintest:
                checkBeginTest();
                break;
            case R.id.tvpercent:
                intent =new Intent(MainActivity.this,ShowTestsActivity.class);
                intent.putExtra("libraryname",libraryname);
                startActivity(intent);
                break;
            case R.id.btntestrecord:
                intent =new Intent(MainActivity.this,ShowTestsActivity.class);
                intent.putExtra("libraryname",libraryname);
                startActivity(intent);
                break;
            case R.id.btncollecttitle:
                break;
            case R.id.btnwrongtest:
                wrongTest();
                break;
            default:
                break;
        }
    }
    //点击开始考试
    public void checkBeginTest(){
        Intent intent;
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
            intent = new Intent(MainActivity.this, TestsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("questionlist", questionlist);
            bundle.putInt("test_id",list.get(0).getId());
            bundle.putString("libraryname",libraryname);
            intent.putExtras(bundle);
            startActivityForResult(intent, 100);
        }
    }
    //错题考试，查找出错题试卷并且跳转到考试界面。
    //点击开始考试
    public void wrongTest(){
        Intent intent;
        SelectQuestionDB selectquestiondb = new SelectQuestionDB(this);
        ArrayList<Question> wronglist = selectquestiondb.selectWrongQuestion(libraryname);
        intent = new Intent(MainActivity.this, TestsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("questionlist", wronglist);
        bundle.putInt("test_id",-2);
        bundle.putString("libraryname",libraryname);
        intent.putExtras(bundle);
        startActivityForResult(intent, 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    String returnedData = data.getStringExtra("beginerror");
                    Toast.makeText(MainActivity.this,returnedData , Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
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
        }
    }

}
