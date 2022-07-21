package com.zy.spring.mildware.test.spring.boot.service.impl;


import com.zy.spring.mildware.test.spring.boot.entity.ZtreeDTO;
import com.zy.spring.mildware.test.spring.boot.mapper.FileUrlMapper;
import com.zy.spring.mildware.test.spring.boot.service.IFileUrlService;
import com.zy.spring.mildware.test.spring.boot.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("fileUrlService")
public class ImplFileUrlService implements IFileUrlService {

    @Autowired
    private FileUrlMapper fileUrlMapper;

    // 此处加载两级节点
    @Override
    @Cacheable(value = "fileUrls")
    public List<ZtreeDTO> getFatherFileUrls() {
        List<String> list = fileUrlMapper.getFatherFileUrls();
        List<ZtreeDTO> ztree = new ArrayList<>();
        list.forEach(fileUrl -> {
            ztree.add(new ZtreeDTO(fileUrl + "/", "0", fileUrl + "/", true, true));
            getSecondFileUrls(ztree, fileUrl + "/");
        });
        return ztree;
    }

    @Override
    @Cacheable(value = "fileUrls")
    public List<ZtreeDTO> getSonFileUrls(String filePath) {
        Integer generationNum = StringTools.getCharCount(filePath, 47) + 1;
        List<String> list = fileUrlMapper.getSonFileUrls(generationNum, filePath);
        List<ZtreeDTO> ztree = new ArrayList<>();
        // 这里并未对最后一级子节点进行处理(最后一级子节点不需要加"/"
        list.forEach(fileUrl -> {
            String[] split = fileUrl.split("/");
            ztree.add(new ZtreeDTO(fileUrl + "/", filePath, split[split.length - 1], false, true));
        });
        return ztree;
    }

    // 加载二级子节点
    private void getSecondFileUrls(List<ZtreeDTO> ztree, String filePath) {
        List<String> list = fileUrlMapper.getSonFileUrls(2, filePath);
        // 这里并未对最后一级子节点进行处理(最后一级子节点不需要加"/"
        list.forEach(fileUrl -> {
            String[] split = fileUrl.split("/");
            ztree.add(new ZtreeDTO(fileUrl + "/", filePath, split[split.length - 1], false, true));
        });
    }

}
