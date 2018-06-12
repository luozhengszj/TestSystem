package com.gggd.sunny.testsystem.tools;

import android.util.Log;

import com.gggd.sunny.testsystem.bean.Question;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sunny on 2017/11/5.
 */

public class FileCreate {
    private final static String fileroot="/sdcard/TestSystem/";
    //创建文件
    public int createExcel(ArrayList<Question> list,String libraryname) {
        //写出的个数
        int num = 0;
        // 声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        //声明一个单子并命名
        Sheet sheet = wb.createSheet(libraryname);
        //给单子名称一个长度
        sheet.setDefaultColumnWidth((short)15);
        // 生成一个样式
        CellStyle style = wb.createCellStyle();
//        //样式字体居中
//        style.setAlignment(CellStyle.ALIGN_CENTER);
        String[] headers = new String[]{"序号","题目","A","B","C","D","E","F","正确答案"};
        //产生表格标题行
        Row row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        for(int i = 0;i<list.size();i++){
            num++;
            row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(num);
            row.createCell(1).setCellValue(list.get(i).getTopic());
            row.createCell(2).setCellValue(list.get(i).getOption_a());
            row.createCell(3).setCellValue(list.get(i).getOption_b());
            row.createCell(4).setCellValue(list.get(i).getOption_c());
            row.createCell(5).setCellValue(list.get(i).getOption_d());
            row.createCell(6).setCellValue(list.get(i).getOption_e());
            row.createCell(7).setCellValue(list.get(i).getOption_f());
            row.createCell(8).setCellValue(list.get(i).getOption_t());
        }
        try {
            makeRootDirectory(fileroot,fileroot+libraryname+".xls");
            FileOutputStream out = new FileOutputStream(fileroot+libraryname+".xls");
            wb.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return num;
    }
    public int createTXT(ArrayList<Question> list,String libraryname) {
        int num = 0;
        try {
            makeRootDirectory(fileroot,fileroot+libraryname+".txt");
            FileWriter out = new FileWriter(fileroot+libraryname+".txt",true);
            for(Question q:list){
                num++;
                if(q.getType().equals("3")) {
                    String str = num + "." + q.getTopic() + "\n" + "正确答案：" + q.getOption_t()+"\n";
                    out.write(str);
                    out.flush();
                }else{
                    String str = num + "." + q.getTopic() + "\nA." + q.getOption_a() + "\nB." + q.getOption_b() + "\n";
                    if (q.getOption_c() != null && !q.getOption_c().equals("")) {
                        str = str + "C." +  q.getOption_c() + "\n";
                    } else if (q.getOption_d() != null && !q.getOption_d().equals("")) {
                        str = str + "D." +  q.getOption_d() + "\n";
                    } else if (q.getOption_e() != null && !q.getOption_e().equals("")) {
                        str = str + "E." +  q.getOption_e() + "\n";
                    } else if (q.getOption_f() != null && !q.getOption_f().equals("")) {
                        str = str + "F." +  q.getOption_f() + "\n";
                    }
                    str = str + "正确答案：" + q.getOption_t()+"\n";
                    out.write(str);
                    out.flush();
                }
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return num;
    }
    // 生成文件夹
    public static void makeRootDirectory(String filePath,String pfile) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(pfile);
            if(!file.exists()){
                file.delete();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }
}
