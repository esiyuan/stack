package com.analyze.stack.mapper.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 从最小到大的收集收集器
 *
 * @author qusan
 * @create 2019-09-12-10:52
 */
@Slf4j
public class MinToMaxDataCollector {


    public static <T> List<T> collectData(MinToMaxDataCollectIntf<T> maxAndMinIntf, Integer pageSize) {
        MaxAndMinId maxAndMinId = maxAndMinIntf.selectMaxAndMinId(MaxAndMinId.EMPTY);
        if (maxAndMinId == null) {
            return Collections.emptyList();
        }
        log.info("maxAndMinIntf = {}", maxAndMinIntf.getClass().getName());
        log.info("扫描范围从 {} 到 {}", maxAndMinId.getMinId(), maxAndMinId.getMaxId());
        List<T> result = new ArrayList<>();
        long currentId = maxAndMinId.getMinId();
        while (currentId <= maxAndMinId.getMaxId()) {
            IdTimeRangeQueryBO param = new IdTimeRangeQueryBO();
            param.setStartId(currentId);
            param.setEndId(currentId + pageSize - 1);
            log.info("当前查询id范围 {} - {}", param.getStartId(), param.getEndId());
            List<T> list = maxAndMinIntf.selectBetween(param);
            if (CollectionUtils.isEmpty(list)) {
                MaxAndMinId curMaxAndMinId = maxAndMinIntf.selectMaxAndMinId(new MaxAndMinId(param.getEndId() + 1, maxAndMinId.getMaxId()));
                if (curMaxAndMinId == null) {
                    log.info("全部收集完成！");
                    break;
                }
                log.info("遇到id间隔， 重新确定id范围:从 {} 到 {} ", curMaxAndMinId.getMinId(), curMaxAndMinId.getMaxId());
                currentId = curMaxAndMinId.getMinId();
                continue;
            }
            currentId = param.getEndId() + 1;
            result.addAll(list);
        }
        return result;
    }

    public static <T> void doWithData(MinToMaxDataCollectIntf<T> maxAndMinIntf, Integer pageSize, ProcessData<T> processData) {
        MaxAndMinId maxAndMinId = maxAndMinIntf.selectMaxAndMinId(MaxAndMinId.EMPTY);
        if (maxAndMinId == null) {
            return;
        }
        log.info("maxAndMinIntf = {}", maxAndMinIntf.getClass().getName());
        log.info("扫描范围从 {} 到 {}", maxAndMinId.getMinId(), maxAndMinId.getMaxId());
        long currentId = maxAndMinId.getMinId();
        while (currentId <= maxAndMinId.getMaxId()) {
            IdTimeRangeQueryBO param = new IdTimeRangeQueryBO();
            param.setStartId(currentId);
            param.setEndId(currentId + pageSize - 1);
            log.info("当前查询id范围 {} - {}", param.getStartId(), param.getEndId());
            List<T> list = maxAndMinIntf.selectBetween(param);
            if (CollectionUtils.isEmpty(list)) {
                MaxAndMinId curMaxAndMinId = maxAndMinIntf.selectMaxAndMinId(new MaxAndMinId(param.getEndId() + 1, maxAndMinId.getMaxId()));
                if (curMaxAndMinId == null) {
                    log.info("处理完毕！");
                    break;
                }
                log.info("遇到id间隔， 重新确定id范围:从 {} 到 {} ", curMaxAndMinId.getMinId(), curMaxAndMinId.getMaxId());
                currentId = curMaxAndMinId.getMinId();
                continue;
            } else {
                processData.process(list);
            }
            currentId = param.getEndId() + 1;
        }
    }

    @FunctionalInterface
    public interface ProcessData<T> {

        void process(List<T> dataList);
    }
}
