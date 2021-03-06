package com.gggd.sunny.testsystem;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Method;

import static com.gggd.sunny.testsystem.R.id.button_forward;
import static com.gggd.sunny.testsystem.R.id.tvsinglefilepath;


public class InputLibraryActivity extends TitleActivity implements View.OnClickListener {
    private Button mbtnselectok;
    private Button mbtnselectreset;
    private TextView mtvsinglefilepath;
    private TextView mtvmultiplefilepath;
    private TextView mtvjudgefilepath;

    private int flag = 0;
    private Button mbuttonbackward;
    private Button mbuttonforward;
    private TextView mtvinputfile;

    private String path;
    private String pathtype;
    private String filetype;
    private File file;
    private String rootpath;
    private String systemtype;
    private boolean typeflag;
    private String[] ss;

    public static String getProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(c, key, "unknown"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_library);
        setTitle("导入题库");
        showForwardView(true);
        mbuttonbackward = (Button) findViewById(R.id.button_backward);
        mbuttonforward = (Button) findViewById(button_forward);
        mtvinputfile = (TextView) findViewById(R.id.tvinputfile);
        mtvinputfile.setText("请手动选择文件");
        mbuttonforward.setText("切换");
        SharedPreferences sharedPreferences = getSharedPreferences("librarydata",
                Activity.MODE_PRIVATE);
        systemtype = sharedPreferences.getString("systemtype", getProperty("ro.build.version.emui", ""));

        if ("Em".equals(systemtype.substring(0, 2))) {
            typeflag = false;
        } else {
            typeflag = true;
        }
        rootpath = "/storage/emulated/0";
        pathtype = sharedPreferences.getString("pathtype", rootpath +
                "/tencent/MicroMsg/Download/");
        filetype = sharedPreferences.getString("filetype", "application/vnd.ms-excel");
        ss = new String[3];

        mbuttonforward.setOnClickListener(this);

        mbtnselectok = (Button) findViewById(R.id.btnselectok);
        mbtnselectreset = (Button) findViewById(R.id.btnselectreset);
        mbtnselectok.setOnClickListener(this);
        mbtnselectreset.setOnClickListener(this);
        mbuttonbackward.setOnClickListener(this);
        mtvsinglefilepath = (TextView) findViewById(tvsinglefilepath);
        mtvmultiplefilepath = (TextView) findViewById(R.id.tvmultiplefilepath);
        mtvjudgefilepath = (TextView) findViewById(R.id.tvjudgefilepath);
        mtvsinglefilepath.setOnClickListener(this);
        mtvmultiplefilepath.setOnClickListener(this);
        mtvjudgefilepath.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (!typeflag) {
            ss[0] = "您的系统暂不支持跳转";
            ss[1] = "微信下载文件在/tencent/MicroMsg/Download/下";
            ss[2] = "QQ下载文件在/tencent/QQfile_recv/下";
            mbuttonforward.setText("说明");
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/*,excel/*");//无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        } else{
            ss[0] = "微信文件夹";
            ss[1] = "QQ文件夹";
            ss[2] = "根目录";
            mbuttonforward.setVisibility(View.VISIBLE);
            file = new File(pathtype);
            if (file == null || !file.exists()) {
                file = new File(rootpath);
                pathtype = rootpath;
                filetype = "file/*";
            }
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setDataAndType(Uri.fromFile(file), filetype);
        }
        switch (v.getId()) {
            case R.id.tvsinglefilepath:
                flag = 1;
                startActivityForResult(intent, flag);
                break;
            case R.id.tvmultiplefilepath:
                flag = 2;
                startActivityForResult(intent, flag);
                break;
            case R.id.tvjudgefilepath:
                flag = 3;
                startActivityForResult(intent, flag);
                break;
            case R.id.btnselectreset:
                mtvsinglefilepath.setText("");
                mtvmultiplefilepath.setText("");
                mtvjudgefilepath.setText("");
                break;
            case R.id.btnselectok:
                if (mtvjudgefilepath.getText() == "" && mtvmultiplefilepath.getText()
                        == "" && mtvsinglefilepath.getText() == "")
                    Toast.makeText(this, "必须有一种题型！", Toast.LENGTH_SHORT).show();
                else {
                    SharedPreferences mySharedPreferences = InputLibraryActivity.this.getSharedPreferences("librarydata",
                            Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putString("filetype", rootpath + "/tencent/MicroMsg/Download/");
                    editor.putString("pathtype", "file/*");
                    //提交当前数据
                    editor.commit();
                    setMbtnselect(v);
                }
                break;
            case R.id.button_backward:
                super.onClick(v);
                break;
            case button_forward:
                qiehuanpath();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();
                selectBtnClick(path);
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(this, uri);
                selectBtnClick(path);
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
                selectBtnClick(path);
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 判断哪个按钮点击，并判断后缀名
     */

    private void selectBtnClick(String path) {
        int end = path.lastIndexOf(".");
        String suffixname = path.substring(end + 1);
        if ("xls".equals(suffixname)) {
            if (flag == 1)
                mtvsinglefilepath.setText(path);
            else if (flag == 2)
                mtvmultiplefilepath.setText(path);
            else if (flag == 3)
                mtvjudgefilepath.setText(path);
            Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "仅接受.xls后缀名的文件！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 跳转Activity,并传递路径
     */
    public void setMbtnselect(View v) {
        //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
        Intent intent = new Intent(InputLibraryActivity.this, MakeTestModeActivity.class);
        //用Bundle携带数据
        Bundle bundle = new Bundle();
        //userInput
        bundle.putString("singlepath", String.valueOf(mtvsinglefilepath.getText()));
        bundle.putString("multiplefilepath", String.valueOf(mtvmultiplefilepath.getText()));
        bundle.putString("judgefilepath", String.valueOf(mtvjudgefilepath.getText()));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void qiehuanpath() {
        SharedPreferences mySharedPreferences = InputLibraryActivity.this.getSharedPreferences("librarydata",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor;
        new AlertDialog.Builder(InputLibraryActivity.this, AlertDialog.THEME_HOLO_LIGHT)
                .setIcon(android.R.drawable.divider_horizontal_bright)
                .setItems(ss,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    case 0:
                                        if (typeflag) {
                                            pathtype = rootpath + "/tencent/MicroMsg/Download/";
                                            filetype = "application/vnd.ms-excel";
                                            mtvinputfile.setText("微信文件夹");
                                        }
                                        break;
                                    case 1:
                                        if (typeflag) {
                                            pathtype = rootpath + "/tencent/QQfile_recv/";
                                            filetype = "application/vnd.ms-excel";
                                            mtvinputfile.setText("QQ文件夹");
                                        }
                                        break;
                                    case 2:
                                        if (typeflag) {
                                            pathtype = rootpath;
                                            filetype = "file/*";
                                            mtvinputfile.setText("根目录");
//                                        Toast.makeText(InputLibraryActivity.this, "请到comtop.im/../userdatas/FileRecv/下", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                }
                            }
                        }).show();
        editor = mySharedPreferences.edit();
        editor.putString("filetype", filetype);
        editor.putString("pathtype", pathtype);
        //提交当前数据
        editor.commit();
    }
}
