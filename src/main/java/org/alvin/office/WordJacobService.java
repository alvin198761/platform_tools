/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alvin.office;

import com.google.common.collect.Lists;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JTextArea;

/**
 * @author tangzhichao
 */
public class WordJacobService extends AbstractJacobService {

    public void initApplication() {
        ComThread.InitSTA();
        app = new ActiveXComponent("Word.Application");
        app.setProperty("Visible", new Variant(false));
        documents = app.getProperty("Documents").toDispatch();
    }

    public String getText(Dispatch table, int r, int c) {
        Dispatch cell = Dispatch.call(table, "Cell", new Variant(r), new Variant(c)).toDispatch();
        Dispatch Range = Dispatch.get(cell, "Range").toDispatch();
        String text = Dispatch.get(Range, "Text").getString().trim();
        return text;
    }

    public void parseObj(File paperFile) throws Exception {
        openDoc(paperFile.getAbsolutePath());
        Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();

        int tableCount = 53; //8 + 11 + 16 + 6 + 3 + 4
        int beforTableIndex = 6;
        File dist = new File("dist");
        dist.mkdirs();
        for (int t = beforTableIndex; t <= tableCount; t++) {
            System.out.println(t + "---------");
            Dispatch cmdTable = Dispatch.call(tables, "Item", new Variant(t)).toDispatch();
            Dispatch rows = Dispatch.call(cmdTable, "Rows").toDispatch();
            Dispatch columns = Dispatch.call(cmdTable, "Columns").toDispatch();
            int rowCount = Dispatch.get(rows, "Count").getInt();
//            int colCount = Dispatch.get(columns, "Count").getInt();

            ParamObject obj = new ParamObject();
            obj.setVendor("Xblue");
            obj.setCmd(getText(cmdTable, 1, 2));
            obj.setInterval(getText(cmdTable, 1, 4));
            obj.setRemark(getText(cmdTable, 2, 2));
            obj.setExample(getText(cmdTable, 3, 2));
            obj.setAuth("唐植超");
            obj.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            List<ParamItem> items = Lists.newArrayList();
            for (int r = 6; r <= rowCount; r += 1) {
                ParamItem item = new ParamItem();
                item.setNo(getText(cmdTable, r, 1));
                item.setParamName(getText(cmdTable, r, 2));
                item.setDataType(getText(cmdTable, r, 3));
                try {
                    item.setLength(Integer.parseInt(getText(cmdTable, r, 4).trim()));
                } catch (Exception e) {
                    item.setLength(0);
                }
                item.setRemark(getText(cmdTable, r, 5));
                items.add(item);
            }
            if (items.isEmpty()) {
                break;
            }
            System.out.println(obj.getRemark());
            obj.setfList(items);
            String classContent = VelocityUtil.parse("/templates/ModelParamer_java.vm", obj, VelocityUtil.classPathVelocityEngine());
            String name = obj.getVendor() + obj.getCmd() + "Param.java";
            Files.write(Paths.get("dist", name), classContent.getBytes());
        }

    }


    public static void main(String[] args) throws Exception {
        try (WordJacobService jacob = new WordJacobService()) {
            jacob.parseObj(new File("E:\\tangzhichao\\chargepile-doc\\需求文档\\小蓝协议\\云快充平台协议V1.2.docx"));
        }
    }

}
