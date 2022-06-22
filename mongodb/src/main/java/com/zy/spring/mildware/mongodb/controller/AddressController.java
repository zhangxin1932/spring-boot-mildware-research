package com.zy.spring.mildware.mongodb.controller;

import com.google.common.collect.Lists;
import com.zy.spring.mildware.mongodb.entity.Address;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/mongodb/address/")
public class AddressController {


    @Resource(name = "mongoTemplateDb1")
    private MongoTemplate mongoTemplateDb1;

    /**
     * 模糊匹配
     * https://blog.csdn.net/weixin_30472035/article/details/98145115?utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~aggregatepage~first_rank_ecpm_v1~rank_v31_ecpm-3-98145115.pc_agg_new_rank&utm_term=mongotemplate%E6%A8%A1%E7%B3%8A%E6%9F%A5%E8%AF%A2%E4%BB%A5%E4%BB%80%E4%B9%88%E5%BC%80%E5%A4%B4&spm=1000.2123.3001.4430
     * @param starts
     * @return
     */
    @RequestMapping("query")
    public Object query(String starts) {
        Query query = new Query(Criteria.where("_id").regex("^" + starts + ".*$"));
        Address db1 = mongoTemplateDb1.findOne(query, Address.class, "db1");
        List<Address> db11 = mongoTemplateDb1.find(query, Address.class, "db1");
        return db11;
    }

    @PostMapping("update")
    public void upsertBatch(@RequestBody List<Address> addressList) {
        BulkOperations ops = mongoTemplateDb1.bulkOps(BulkOperations.BulkMode.UNORDERED, "db1");
        List<Pair<Query, Update>> pairs = Lists.newArrayList();
        addressList.forEach(e -> {
            String id = e.getId();
            Query query = new Query(Criteria.where("_id").is(id));
            Update update = new Update();
            String provinceId = e.getProvinceId();
            List<String> cityIds = e.getCityIds();
            if (!StringUtils.isEmpty(provinceId)) {
                update.set("cityIds", e.getCityIds());
            }
            if (!CollectionUtils.isEmpty(cityIds)) {
                update.set("provinceId", e.getProvinceId());
            }
            pairs.add(Pair.of(query, update));
        });
        ops.upsert(pairs);
        ops.execute();
    }

}
