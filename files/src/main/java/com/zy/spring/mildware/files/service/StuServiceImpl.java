package com.zy.spring.mildware.files.service;

import com.zy.spring.mildware.files.easyexcel.StuScoreExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StuServiceImpl {

    public void saveStuScore(List<StuScoreExcel> list) {
        log.info("begin to saveStuScore.");
    }

    public List<StuScoreExcel> getAllScore() {
        return new ArrayList<>();
    }
}
