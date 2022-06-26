package com.zy.spring.mildware.files.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuScoreExcel implements Serializable {
    private static final long serialVersionUID = -5160952135284303981L;
    /**
     * 这些 注解说明下:
     * 当上传 excel 时, 可以不用这些注解, 但若用, 则注解里的 value 必须与 excel 表格的表头一模一样, 否则解析不到
     * 当下载 excel 时, 需要添加这些注解
     */
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("语文")
    private Double chineseScore;
    @ExcelProperty("数学")
    private Double mathScore;
    @ExcelProperty("英语")
    private Double englishScore;
    @ExcelProperty("总分")
    private Double totalScore;
    @ExcelProperty("班级名次")
    private Integer classRanking;
    @ExcelProperty("学校名次")
    private Integer schoolRanking;
    @ExcelProperty("考试日期")
    private Date examDate;
}
