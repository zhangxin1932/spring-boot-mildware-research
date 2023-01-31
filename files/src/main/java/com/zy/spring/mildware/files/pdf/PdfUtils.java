package com.zy.spring.mildware.files.pdf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import technology.tabula.CommandLineApp;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Objects;

/**
 * https://iowiki.com/pdfbox/pdfbox_quick_guide.html
 * <p>
 * https://gitee.com/joy-li/pdf_freemark_springboot/tree/master/src/main/resources
 * <p>
 * https://iowiki.com/pdfbox/pdfbox_quick_guide.html
 * <p>
 * https://pdfbox.apache.org/
 * <p>
 * https://blog.csdn.net/qq_21434959/article/details/83120286
 * <p>
 * https://blog.csdn.net/Feb_kylin/article/details/79105630
 */
public class PdfUtils {

    private static final String PATH1 = "D:\\tmp\\pdf\\m1.pdf";
    private static final String CDE_PDF_PATH = "D:\\tmp\\pdf\\CXSS2101059-62增加适应症技术审评报告.pdf";
    private static final String CDE_PDF_PATH_2 = "D:\\tmp\\pdf\\注射用苯磺酸瑞马唑仑（CXHS2101053）申请上市技术审评报告.pdf";
    private static final String CDE_PDF_OUTPUT_PATH_BY_PDFBOX = "D:\\tmp\\pdf\\重组人生长激素注射液（CXSS2101059-62）增加适应症技术审评报告_pdfbox.txt";
    private static final String CDE_PDF_OUTPUT_PATH_BY_SPIRE = "D:\\tmp\\pdf\\重组人生长激素注射液（CXSS2101059-62）增加适应症技术审评报告_spire.txt";
    private static final String CDE_PDF_OUTPUT_PATH_BY_TABULA = "D:\\tmp\\pdf\\重组人生长激素注射液（CXSS2101059-62）增加适应症技术审评报告_tabula.txt";

    public static void main(String[] args) throws Exception {
        f2();
    }

    private static void f7() throws Exception {
        writeImg2Pdf(null, "D:\\tmp\\pdf\\hi.pdf", "D:\\tmp\\img\\excel1.jpg");
    }

    /**
     * 读取表格的文本：推荐！！！！！！！！！！
     * https://blog.csdn.net/u012998680/article/details/123189163
     *
     *  需要注意的是：如果表格两边没有框，就解析不了！
     *
     * @throws Exception
     */
    private static void f6() throws Exception {
        // -f 导出格式，默认 csv
        // -p 只导出哪一页，all是所有
        // path
        // -l 强制使用点阵提取 pdf， 关键参数
        String[] args = {
                "-f=JSON"
                , "-p=all"
                , CDE_PDF_PATH_2
                , "-l"
        };
        DefaultParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(CommandLineApp.buildOptions(), args);
        StringBuilder builder = new StringBuilder();
        new CommandLineApp(builder, commandLine).extractTables(commandLine);
        System.out.println(builder.toString());
        JSONArray array = JSON.parseArray(builder.toString());
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(array.get(i)));
            JSONArray data = jsonObject.getJSONArray("data");
            for (int j = 0; j < data.size(); j++) {
                JSONArray subArray = JSON.parseArray(JSON.toJSONString(data.get(j)));
                for (int k = 0; k < subArray.size(); k++) {
                    JSONObject js = JSON.parseObject(JSON.toJSONString(subArray.get(k)));
                    String text = normalizeStr(js.getString("text"));
                    if (StringUtils.equalsIgnoreCase(text, "上市许可持有人")) {
                        String txt = normalizeStr(JSON.parseObject(JSON.toJSONString(subArray.get(k + 1))).getString("text"));
                        System.out.println(txt);
                        break;
                    }
                    if (StringUtils.equalsIgnoreCase(text, "通用名")) {
                        String txt = normalizeStr(JSON.parseObject(JSON.toJSONString(subArray.get(k + 1))).getString("text"));
                        System.out.println(txt);
                        break;
                    }
                    if (StringUtils.equalsIgnoreCase(text, "完成的临床试验内容")) {
                        String txt = normalizeStr(JSON.parseObject(JSON.toJSONString(subArray.get(k + 1))).getString("text"));
                        System.out.println(txt);
                        break;
                    }
                    if (StringUtils.equalsIgnoreCase(text, "优先审评审批")) {
                        String txt = normalizeStr(JSON.parseObject(JSON.toJSONString(subArray.get(k + 1))).getString("text"));
                        System.out.println(txt);
                        break;
                    }
                }
            }
        }
    }

    private static void f5() throws Exception {
        // -f 导出格式，默认 csv
        // -p 只导出哪一页，all是所有
        // path
        // -l 强制使用点阵提取 pdf， 关键参数
        String[] args = {
                "-f=JSON"
                , "-p=all"
                , CDE_PDF_PATH
        };
        DefaultParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(CommandLineApp.buildOptions(), args);
        StringBuilder builder = new StringBuilder();
        new CommandLineApp(builder, commandLine).extractTables(commandLine);
        System.out.println(builder.toString());
        JSONArray array = JSON.parseArray(builder.toString());
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(array.get(i)));
            JSONArray data = jsonObject.getJSONArray("data");
            for (int j = 0; j < data.size(); j++) {
                JSONArray subArray = JSON.parseArray(JSON.toJSONString(data.get(j)));
                for (int k = 0; k < subArray.size(); k++) {
                    JSONObject js = JSON.parseObject(JSON.toJSONString(subArray.get(k)));
                    String text = normalizeStr(js.getString("text"));
                    if (StringUtils.equalsIgnoreCase(text, "上市许可持有人")) {
                        String txt = normalizeStr(JSON.parseObject(JSON.toJSONString(subArray.get(k + 1))).getString("text"));
                        System.out.println(txt);
                        break;
                    }
                    if (StringUtils.equalsIgnoreCase(text, "通用名")) {
                        String txt = normalizeStr(JSON.parseObject(JSON.toJSONString(subArray.get(k + 1))).getString("text"));
                        System.out.println(txt);
                        break;
                    }
                    if (StringUtils.equalsIgnoreCase(text, "完成的临床试验内容")) {
                        String txt = normalizeStr(JSON.parseObject(JSON.toJSONString(subArray.get(k + 1))).getString("text"));
                        System.out.println(txt);
                        break;
                    }
                    if (StringUtils.equalsIgnoreCase(text, "优先审评审批")) {
                        String txt = normalizeStr(JSON.parseObject(JSON.toJSONString(subArray.get(k + 1))).getString("text"));
                        System.out.println(txt);
                        break;
                    }
                }
            }
        }
    }

    private static String ctCheckbox() {
        String str = "境内\uF052I期\uF052II期\uF052III期境外□I期\uF052II期□III期其他:";
        String[] imports = str.split("境外");
        for (String anImport : imports) {
            String[] split = anImport.split("\uF052");
            for (String s : split) {
                System.out.println(s);
            }
        }
        return null;
    }

    private static String normalizeStr(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return str.replaceAll(" ", "")
                .replaceAll("\r\n", "")
                .replaceAll("\n", "")
                .replaceAll("\r", "");
    }

    /**
     * 读取表格的文本
     * https://blog.csdn.net/u012998680/article/details/123189163
     *
     * @throws Exception
     */
    private static void f4() throws Exception {
        //加载PDF文档
        /*PdfDocument pdf = new PdfDocument();
        pdf.loadFromFile(CDE_PDF_PATH);

        //创建StringBuilder类的实例
        StringBuilder builder = new StringBuilder();

        //抽取表格
        PdfTableExtractor extractor = new PdfTableExtractor(pdf);
        PdfTable[] tableLists ;
        for (int page = 0; page < pdf.getPages().getCount(); page++) {
            tableLists = extractor.extractTable(page);
            if (tableLists != null && tableLists.length > 0) {
                for (PdfTable table : tableLists)
                {
                    int row = table.getRowCount();
                    int column = table.getColumnCount();
                    for (int i = 0; i < row; i++)
                    {
                        for (int j = 0; j < column; j++)
                        {
                            String text = table.getText(i, j);
                            builder.append(text+" ");
                        }
                        builder.append("\r\n");
                    }
                }
            }
        }

        //将提取的表格内容写入txt文档
        FileWriter fileWriter = new FileWriter(CDE_PDF_OUTPUT_PATH_BY_SPIRE);
        fileWriter.write(builder.toString());
        fileWriter.flush();
        fileWriter.close();*/
    }

    /**
     * 读取表格的文本
     * https://blog.csdn.net/u012998680/article/details/123189163
     *
     * @throws Exception
     */
    private static void f3() throws Exception {
        String result = readPDF2(CDE_PDF_PATH_2);
        System.out.println(result);

        // 将提取的表格内容写入txt文档
        FileWriter fileWriter = new FileWriter(CDE_PDF_OUTPUT_PATH_BY_PDFBOX);
        fileWriter.write(result);
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * 读取指定位置的文本
     *
     * @throws Exception
     */
    private static void f2() throws Exception {
        PDDocument pdDocument = PDDocument.load(new FileInputStream(new File(PATH1)));
        if (pdDocument.isEncrypted()) {
            System.out.println("encrypted-------");
        }
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
        Rectangle rectangle = new Rectangle(0, 0, 1000, 1000);
        stripper.addRegion("paper", rectangle);
        PDPageTree pages = pdDocument.getDocumentCatalog().getPages();
        for (PDPage page : pages) {
            stripper.extractRegions(page);
            String paper = stripper.getTextForRegion("paper");
            String str = "   ";
            System.out.println(paper);
        }
    }

    /**
     * 读取所有文本
     *
     * @throws Exception
     */
    private static void f1() throws Exception {
        PDDocument pdDocument = PDDocument.load(new FileInputStream(new File(CDE_PDF_PATH)));
        PDDocumentInformation information = pdDocument.getDocumentInformation();
        System.out.println("----------------------------------------");
        // 获取 pdf 标题
        String title = information.getTitle();
        System.out.println(title);
        System.out.println(information.getTrapped());
        System.out.println(information.getKeywords());
        System.out.println(information.getMetadataKeys());
        System.out.println(information.getProducer());
        System.out.println(information.getSubject());
        System.out.println(information.getCOSObject());
        System.out.println("----------------------------------------");

        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        String text = pdfTextStripper.getText(pdDocument);
        System.out.println(text);
    }

    /**
     * 用来读取pdf文件
     *
     * @param filePath
     * @return
     */
    public static String readPDF(String filePath) {
        String buffer = "";
        try {
            File input = new File(filePath);
            if (input != null && input.exists()) {
                PDDocument pd = PDDocument.load(input);
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setSortByPosition(false);
                buffer = stripper.getText(pd);
                pd.close();
            } else
                buffer = "read failed";
        } catch (Exception e) {
            e.printStackTrace();
            return "read failed";
        }
        return buffer;
    }

    /**
     * 读取 pdf 文件
     * @param fileName
     * @return
     */
    public static String readPDF2(String fileName) {
        String result = "";
        File file = new File(fileName);
        try (FileInputStream in = new FileInputStream(fileName)){
            // 新建一个PDF解析器对象
            PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
            // 对PDF文件进行解析
            parser.parse();
            // 获取解析后得到的PDF文档对象
            PDDocument pdfDocument = parser.getPDDocument();
            // 新建一个PDF文本剥离器
            PDFTextStripper stripper = new PDFTextStripper();
            // sort:设置为true 则按照行进行读取，默认是false
            stripper.setSortByPosition(false);
            // 从PDF文档对象中剥离文本
            result = stripper.getText(pdfDocument);
        } catch (Exception e) {
            System.out.println("读取PDF文件" + file.getAbsolutePath() + "生失败！" + e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将图片写入 pdf
     * @param pdfTemplatePath
     * @param targetPdfPath
     * @param imagePath
     * @throws Exception
     */
    public static void writeImg2Pdf(String pdfTemplatePath, String targetPdfPath, String imagePath) throws Exception {
        try (PDDocument doc = Objects.nonNull(pdfTemplatePath) ? PDDocument.load(new File(pdfTemplatePath)) : new PDDocument()) {
            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
            PDPage pdPage = new PDPage();
            doc.addPage(pdPage);
            try (PDPageContentStream contentStream = new PDPageContentStream(doc, pdPage, PDPageContentStream.AppendMode.APPEND, true, true)) {
                float scale = 0.5f;
                contentStream.drawImage(pdImage, 20, 20,
                        pdImage.getWidth() * scale,
                        pdImage.getHeight() * scale);
                // contentStream 注意要在保存前关闭 否则会报错
            }
            doc.save(targetPdfPath);
        }
    }
}

