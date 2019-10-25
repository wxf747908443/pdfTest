package com.wang;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {

        System.out.println("Hello World!");

        addPngWatermark("E://Sample.pdf",
                "E://Sample1.pdf",
                "内部使用");


    }


    /**
     * 需要添加水印的pdf文件
     *
     * @param oldPdf        需要加水印的pdf路径
     * @param outputPdf     加完水印后的pdf文件路径
     * @param textWatermark 水印文字
     * @throws IOException
     * @throws DocumentException
     */
    public static void addPngWatermark(String oldPdf, String outputPdf, String textWatermark) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(oldPdf);
        reader.unethicalreading = true;
        // 页数
        int n = reader.getNumberOfPages();
        BaseFont baseFont = BaseFont.createFont(getChineseFont(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        // 设置字体大小
        Font f = new Font(baseFont, 48);
        Phrase p = new Phrase(textWatermark, f);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputPdf));
        // transparency
        PdfGState gs1 = new PdfGState();
        // 设置水印透明度
        gs1.setFillOpacity(0.3f);
        // properties
        PdfContentByte over;
        Rectangle pagesize;
        float x, y;
        for (int i = 1; i <= n; i++) {
            pagesize = reader.getPageSizeWithRotation(i);
            x = (pagesize.getLeft() + pagesize.getRight()) / 2;
            y = (pagesize.getTop() + pagesize.getBottom()) / 2;
            over = stamper.getOverContent(i);
            over.saveState();
            over.setGState(gs1);
            over.setColorFill(BaseColor.RED);
            ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, x, y, 45);
            over.restoreState();
        }

        stamper.close();
        reader.close();
    }

    /**
     * 获取字体
     *
     * @return
     */
    private static String getChineseFont() {
        //黑体--在windows下
        String font = "C:/Windows/Fonts/simhei.ttf";

        //判断系统类型，加载字体文件
        java.util.Properties prop = System.getProperties();
        String osName = prop.getProperty("os.name").toLowerCase();
        // System.out.println(osName);
        if (osName.indexOf("linux") > -1) {
            font = "/usr/share/fonts/simhei.ttf";
        }
        if (!new File(font).exists()) {
            throw new RuntimeException("字体文件不存在,影响导出pdf中文显示！" + font);
        }
        return font;
    }

    /**
     * 【功能描述：添加图片和文字水印】 【功能详细描述：功能详细描述】
     *
     * @param srcFile    待加水印文件
     * @param destFile   加水印后存放地址
     * @param text       加水印的文本内容
     * @param textWidth  文字横坐标
     * @param textHeight 文字纵坐标
     * @throws Exception
     */
    public static void addWaterMark(String srcFile, String destFile, String text,
                                    int textWidth, int textHeight) throws Exception {
        // 待加水印的文件
        PdfReader reader = new PdfReader(srcFile);
        // 加完水印的文件
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                destFile));
        int total = reader.getNumberOfPages() + 1;
        PdfContentByte content;
        // 设置字体
        BaseFont font = BaseFont.createFont();
        // 循环对每页插入水印
        for (int i = 1; i < total; i++) {
            // 水印的起始
            content = stamper.getUnderContent(i);
            // 开始
            content.beginText();
            // 设置颜色 默认为蓝色
            content.setColorFill(BaseColor.BLUE);
            // content.setColorFill(Color.GRAY);
            // 设置字体及字号
            content.setFontAndSize(font, 38);
            // 设置起始位置
            // content.setTextMatrix(400, 880);
            content.setTextMatrix(textWidth, textHeight);
            // 开始写入水印
            content.showTextAligned(Element.ALIGN_LEFT, text, textWidth,
                    textHeight, 45);
            content.endText();
        }
        stamper.close();
    }


}
