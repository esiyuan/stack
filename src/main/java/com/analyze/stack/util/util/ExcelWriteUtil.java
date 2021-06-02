package com.analyze.stack.util.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author guanjie@mgtv.com
 * @create 2021-04-02-15:03
 */
public class ExcelWriteUtil {

    public static void main(String[] args) {
        write(1, "test", new File("d://t.xlsx"), Lists.newArrayList("a", "b"), Lists.newArrayList(Lists.newArrayList(1, 1), Lists.newArrayList(1, 3), Lists.newArrayList(2, 3)));
    }


    public static void write(int sheetNo, String sheetName, File outFile, List<String> heads, List<List<Object>> values) {
        WriteSheet sheet = new WriteSheet();
        sheet.setSheetNo(sheetNo);
        List<List<String>> head = new ArrayList<>();
        for (String each : heads) {
            head.add(Lists.newArrayList(each));
        }
        sheet.setHead(head);
        sheet.setSheetName(sheetName);
        try {
            ExcelWriter writer = EasyExcel.write(new FileOutputStream(outFile)).build();
            writer.write(values, sheet);
            writer.finish();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
