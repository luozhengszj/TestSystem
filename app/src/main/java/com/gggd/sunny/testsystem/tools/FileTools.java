package com.gggd.sunny.testsystem.tools;

import android.util.Log;

import com.gggd.sunny.testsystem.bean.Question;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import static java.lang.String.valueOf;

/**
 * Created by Sunny on 2017/10/13.
 */

public class FileTools {

    public LinkedList<Question> readExcel(String path){
        String postfix = path.substring(path.lastIndexOf(".") + 1, path.length());
        if ("xls".equals(postfix)) {
            return readXls(path);
        }
//        else if ("xlsx".equals(postfix)) {
//            return readXlsx(path);
//        }
        else {
            return null;
        }
    }

    /**
     * Read the Excel 2010
     *
     * @param path the path of the excel file
     * @return
     * @throws Exception
     */
//    public HashSet<Question> readXlsx(String path) throws IOException, InvalidFormatException {
//        InputStream is = new FileInputStream(new File(path));
//
//        Workbook xssfWorkbook = WorkbookFactory.create(new File(path));
//        if(xssfWorkbook == null)
//                xssfWorkbook =  new HSSFWorkbook (is);
////        OPCPackage pkg = OPCPackage.open(path);
////        Workbook xssfWorkbook = new XSSFWorkbook( pkg );
//
//        Question question;
//        HashSet<Question> list = new HashSet<Question>();
//        Log.d("lzlz", "HashSet");
//        // Read the Sheet
//        Sheet sheet = xssfWorkbook.getSheetAt(0); // 获得第一个表单
//        int n = sheet.getRow(0).getPhysicalNumberOfCells();
//        int count = sheet.getLastRowNum();
//
//        for (int rowNum = 1; rowNum <= count; rowNum++) {
//            Row xssfRow = sheet.getRow(rowNum);
//            if (xssfRow != null) {
//                Log.d("lzlz", "inxssfRow");
//                Cell topic = xssfRow.getCell(0);
//                Cell option_a = xssfRow.getCell(1);
//                Cell option_b = xssfRow.getCell(2);
//                Cell option_c = xssfRow.getCell(3);
//                Cell option_d = xssfRow.getCell(4);
//                Cell option_e = xssfRow.getCell(5);
//                Cell option_f = xssfRow.getCell(6);
//                Cell option_t = xssfRow.getCell(7);
//                question = new Question(topic.toString(), option_a.toString(), option_b.toString(), option_c.toString(),
//                        option_d.toString(), option_e.toString(), option_f.toString(), option_t.toString());
//                Log.d("lz", question.toString());
//                list.add(question);
//            }
//        }
//
//        return list;
//    }

    /**
     * Read the Excel 2003-2007
     *
     * @param path the path of the Excel
     * @return
     * @throws IOException
     */
    public LinkedList<Question> readXls(String path) {
        Log.d("lz",path);
        FileInputStream is = null;
        try {
            is = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HSSFWorkbook hssfWorkbook = null;
        try {
            hssfWorkbook = new HSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Question question = null;
        LinkedList<Question> list = new LinkedList<Question>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    HSSFCell topic = hssfRow.getCell(0);
                    HSSFCell option_a = hssfRow.getCell(1);
                    HSSFCell option_b = hssfRow.getCell(2);
                    HSSFCell option_c = hssfRow.getCell(3);
                    HSSFCell option_d = hssfRow.getCell(4);
                    HSSFCell option_e = hssfRow.getCell(5);
                    HSSFCell option_f = hssfRow.getCell(6);
                    HSSFCell option_t = hssfRow.getCell(7);
                    question = new Question(getValue(topic), getValue(option_a), getValue(option_b), getValue(option_c),
                            getValue(option_d), getValue(option_e), getValue(option_f), getValue(option_t));
                    Log.d("lz", question.toString());
                    list.add(question);
                }
            }
        }
        return list;
    }

//    @SuppressWarnings("static-access")
//    private String getValue(XSSFCell xssfRow) {
//        String cellValue = "";
//        switch (xssfRow.getCellType()) {
//            case XSSFCell.CELL_TYPE_STRING:
//                cellValue =xssfRow.getRichStringCellValue().getString().trim();
//                break;
//            case XSSFCell.CELL_TYPE_NUMERIC:
//                cellValue =valueOf(xssfRow.getNumericCellValue());
//                break;
//            case XSSFCell.CELL_TYPE_BOOLEAN:
//                cellValue = valueOf(xssfRow.getBooleanCellValue()).trim();
//                break;
//            case XSSFCell.CELL_TYPE_FORMULA:
//                cellValue =xssfRow.getCellFormula();
//                break;
//            default:
//                cellValue = "";
//        }
//        return cellValue;
//    }

    @SuppressWarnings("static-access")
    private String getValue(HSSFCell hssfCell) {
        String cellValue = "";
        if(hssfCell == null){
            cellValue = "";
        }else {
            switch (hssfCell.getCellType()) {
                case HSSFCell.CELL_TYPE_STRING:
                    cellValue = hssfCell.getRichStringCellValue().getString().trim();
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    cellValue = valueOf(hssfCell.getNumericCellValue());
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    cellValue = valueOf(hssfCell.getBooleanCellValue()).trim();
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    cellValue = hssfCell.getCellFormula();
                    break;
                default:
                    cellValue = "";
            }
        }
        return cellValue;
    }
    //判断行为空
//    private int CheckRowNull(HSSFRow hssfRow){
//        int num = 0;
//        Iterator<Cell> cellItr =hssfRow.iterator();
//        while(cellItr.hasNext()){
//            Cell c =cellItr.next();
//            if(c.getCellType() ==HSSFCell.CELL_TYPE_BLANK){
//                num++;
//            }
//        }
//        return num;
//    }
}
