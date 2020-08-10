package org.alvin.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import pdftable.PdfTableReader;
import pdftable.models.ParsedTablePage;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author 唐植超
 * @date 2020/05/21
 */
public class PDFTable {

    public static void main(String[] args) throws IOException {
//        System.loadLibrary("opencv_java342");
//        System.loadLibrary("opencv_java342");
//        String file = "E:\\tangzhichao\\chargepile-doc\\需求文档\\小蓝协议\\云快充平台协议v1.1.pdf";
//        String file = "E:\\tangzhichao\\chargepile-doc\\需求文档\\盛弘充电桩协议\\盛弘充电桩后台服务器通迅协议V4.5A07.pdf";
        String file = "E:\\tangzhichao\\chargepile-doc\\需求文档\\科旺充电桩协议\\1_科旺充电协议v3.5最终版本(2).pdf";
        PDDocument pdfDoc = PDDocument.load(new File(file));
        PdfTableReader reader = new PdfTableReader();
        List<ParsedTablePage > tablePages = reader.parsePdfTablePages(pdfDoc, 16,18);
        for(ParsedTablePage tablePage : tablePages) {
            List<ParsedTablePage.ParsedTableRow> rows = tablePage.getRows();
            for (ParsedTablePage.ParsedTableRow row : rows) {
                if("桢类型码".equals(row.getCell(0).trim())){
                    System.out.println("------------------------------------");
                }
                System.out.println(row.getCells());
            }
        }
    }
}
