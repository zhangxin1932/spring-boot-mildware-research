package com.zy.spring.mildware.files.pdf;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * https://github.com/tabulapdf/tabula-java
 * 解析表格，带边框
 */
public class PdfExtractTable {

    public static void main(String[] args) throws Exception {
        f1();
    }


    private static void f1() throws Exception {
        File file = new File("D:/tmp/pdf/环泊酚注射液（CXHS2200006） 申请上市技术审评报告.pdf");
        // InputStream in = PdfExtractTable.class.getResourceAsStream();
        try (PDDocument document = PDDocument.load(file)) {
            Map<String, String> resultMap = Maps.newHashMap();
            List<String> keys = new ArrayList<>();
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
            PageIterator pi = new ObjectExtractor(document).extract();
            while (pi.hasNext()) {
                // iterate over the pages of the document
                Page page = pi.next();
                List<Table> table = sea.extract(page);
                // iterate over the tables of the page
                for(Table tables: table) {
                    List<List<RectangularTextContainer>> rows = tables.getRows();
                    // iterate over the rows of the table

                    for (List<RectangularTextContainer> cells : rows) {
                        if (cells.size() != 2) {
                            break;
                        }
                        String key = StringUtils.trimToNull(cells.get(0).getText());
                        String value = StringUtils.trimToNull(cells.get(1).getText());
                        if (StringUtils.isBlank(key)) {
                            if (!keys.isEmpty()) {
                                String s = keys.get(keys.size() - 1);
                                String v = resultMap.get(s);
                                resultMap.put(s, v + " " + value);
                            } else {
                                continue;
                            }
                        } else {
                            resultMap.put(key, value);
                            keys.add(key);
                        }

                        // print all column-cells of the row plus linefeed
                        /*for (RectangularTextContainer content : cells) {
                            // Note: Cell.getText() uses \r to concat text chunks
                            String text = content.getText().replace("\r", " ");
                            System.out.print(text + "|");
                        }*/
                    }
                }
            }

            System.out.println("---------------------");
            resultMap.forEach((k,v) -> {
                System.out.println(k + " ------> " + v);
            });
        }
    }
}
