package com.analyze.stack.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.analyze.stack.pojo.model.StackDO;
import com.analyze.stack.service.StackService;
import com.analyze.stack.util.util.CollectionExtUtil;
import com.analyze.stack.util.util.HttpsClientUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author guanjie@mgtv.com
 * @create 2021-06-02-18:12
 */
@Component
@Slf4j
public class StackGetProcess {
    @Autowired
    private StackService stackService;

    public void getddTotal() {
        String strStart = "http://quotes.money.163.com/hs/service/diyrank.php?page=";
        String strEnd = "&query=STYPE:EQA;EXCHANGE:CNSESZ&fields=SNAME,CODE&sort=PERCENT&order=desc&count=100&type=query";
        for (int i = 0; i < 10000; i++) {
            try {
                String s = HttpsClientUtil.get(strStart + i + strEnd, 100000);
                Result result = JSON.parseObject(s, Result.class);
                if (result != null && CollectionUtils.isNotEmpty(result.getList())) {
                    for (Entry entry : result.getList()) {
                        if (StringUtils.startsWith(entry.getCode(), "1")) {
                            entry.setCode("sz" + StringUtils.substring(entry.getCode(), 1));
                        } else {

                            entry.setCode("sz" + entry.getCode());
                        }
                    }

                    LambdaQueryWrapper<StackDO> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.in(StackDO::getCode, CollectionExtUtil.toList(result.getList(), Entry::getCode));
                    List<StackDO> list = stackService.list(queryWrapper);
                    Set<String> stringSet = CollectionExtUtil.toSet(list, StackDO::getCode);
                    List<StackDO> records = new ArrayList<>();
                    for (Entry each : result.getList()) {
                        if (!stringSet.contains(each.getCode())) {
                            StackDO record = new StackDO();
                            record.setCode(each.getCode());
                            record.setName(each.getName());
                            records.add(record);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(records)) {
                        stackService.saveBatch(records);
                    }


                } else {
                    return;
                }
            } catch (Exception e) {
                log.info("", e);
            }

        }


    }

    @Data
    public static class Result {
        private List<Entry> list;
    }

    @Data
    public static class Entry {

        @JSONField(name = "CODE")
        private String code;

        @JSONField(name = "SNAME")
        private String name;


    }


}
