package com.zy.spring.mildware.files.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.zy.spring.mildware.files.service.StuServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StuScoreListener extends AnalysisEventListener<StuScoreExcel> {
    /**
     * 这里需要将 mapper 注入进来, 因为直接给 StuScoreListener 加 @Component 获取不到 mapper
     */
    private final StuServiceImpl stuService;

    public StuScoreListener(StuServiceImpl stuService) {
        this.stuService = stuService;
    }

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    private List<StuScoreExcel> list = new ArrayList<>();

    /**
     * 这个每一条数据解析都会来调用
     * @param data
     * @param analysisContext
     */
    @Override
    public void invoke(StuScoreExcel data, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        list.add(data);
        // 达到 BATCH_COUNT 了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", list.size());
        try {
            stuService.saveStuScore(list);
            log.info("存储数据库成功！");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("存储数据库失败!");
        }
    }
}
