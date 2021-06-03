package com.analyze.stack.process;

import com.analyze.stack.mapper.base.MinToMaxDataCollector;
import com.analyze.stack.pojo.dto.StackDetailRespDTO;
import com.analyze.stack.pojo.model.StackDO;
import com.analyze.stack.pojo.model.StackDetailDO;
import com.analyze.stack.pojo.result.BooleanResult;
import com.analyze.stack.service.StackDetailService;
import com.analyze.stack.util.util.ExcelWriteUtil;
import com.analyze.stack.util.util.HttpsClientUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

/**
 * @author guanjie@mgtv.com
 * @create 2021-06-02-16:37
 */
@Component
@Slf4j
public class CalStackProcess {
    @Autowired
    private StackDetailService stackDetailService;
    @Autowired
    private StackCollector stackCollector;
    @Value("${stackDetail}")
    private String detailUrl;


    public void calcDay(String day) {
        LambdaQueryWrapper<StackDetailDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StackDetailDO::getCurDate, day);
        List<StackDetailDO> list = stackDetailService.list(queryWrapper);
        list.sort(Comparator.comparing(StackDetailDO::getTotalPrice).reversed());

        Map<Long, Integer> totalPriceMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            totalPriceMap.put(list.get(i).getId(), i + 1);
        }

        list.sort(Comparator.comparing(StackDetailDO::getCurTotal).reversed());

        Map<Long, Integer> currontMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            currontMap.put(list.get(i).getId(), i + 1);
        }
        list.sort(Comparator.comparing(StackDetailDO::getRise).reversed());

        List<List<Object>> lists = new ArrayList<>();
        for (StackDetailDO stackDetailDO : list) {
            lists.add(Lists.newArrayList(stackDetailDO.getName(), stackDetailDO.getCode(), stackDetailDO.getCurPrice(),
                    stackDetailDO.getRise()/100.0, stackDetailDO.getCurTotal(), currontMap.get(stackDetailDO.getId()), stackDetailDO.getTotalPrice(), totalPriceMap.get(stackDetailDO.getId())));
        }

        ExcelWriteUtil.write(1, "当前行情", new File("d://当前行情.xlsx"),
                Lists.newArrayList("股票名称", "股票代码", "当前价格", "涨幅", "成交量(万)", "成交量排名", "市值(亿)", "市值排名"), lists);

    }


    public void calcStack() {
        String curData = DateTime.now().toString("yyyy-MM-dd");
        List<StackDO> stackDOS = MinToMaxDataCollector.collectData(stackCollector, 1000);
        List<StackDetailDO> stackDetailDOS = new ArrayList<>();
        Set<String> codeSet = new HashSet<>();
        for (StackDO stackDO : stackDOS) {
            if (codeSet.add(stackDO.getCode())) {
                BooleanResult<StackDetailRespDTO> stackDetail = getStackDetail(stackDO.getCode());
                if (stackDetail.isSucceed()) {
                    StackDetailRespDTO stackDetailData = stackDetail.getData();
                    StackDetailDO record = new StackDetailDO();
                    record.setCurDate(curData);
                    record.setCode(stackDO.getCode());
                    record.setName(stackDO.getName());
                    record.setCurPrice(stackDetailData.getCurPrice());
                    record.setRise(stackDetailData.getRise());
                    record.setCurTotal(stackDetailData.getCurTotal());
                    record.setTotalPrice(stackDetailData.getTotalPrice());
                    stackDetailDOS.add(record);

                }
            }

        }
        if (CollectionUtils.isNotEmpty(stackDetailDOS)) {
            stackDetailService.saveBatch(stackDetailDOS, 200);
        }
    }


    public BooleanResult<StackDetailRespDTO> getStackDetail(String codeName) {
        try {
            //v_s_sz003033="51~征和工业~003033~35.23~1.04~3.04~44031~15388~~28.80~GP-A";
            System.out.println(detailUrl + codeName);
            String resp = HttpsClientUtil.get(detailUrl + codeName, 5000);
            String substringAfter = StringUtils.substringAfter(resp, "=");
            String remove = StringUtils.remove(substringAfter, '"');
            String[] eachs = StringUtils.split(remove, '~');
            StackDetailRespDTO stackDetailRespDTO = new StackDetailRespDTO();
            stackDetailRespDTO.setName(eachs[1]);
            stackDetailRespDTO.setCurPrice((int) (Doubles.tryParse(eachs[3]) * 100));
            stackDetailRespDTO.setRise((int) (Doubles.tryParse(eachs[5]) * 100));
            stackDetailRespDTO.setCurTotal(Ints.tryParse(eachs[7]));
            stackDetailRespDTO.setTotalPrice((int) (Doubles.tryParse(eachs[8]) * 100));
            return BooleanResult.success(stackDetailRespDTO);
        } catch (Exception e) {
            log.info("获取票信息异常", e);
            return BooleanResult.fail(e.getMessage());
        }
    }


}
