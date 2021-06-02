package com.analyze.stack.process;

import com.analyze.stack.mapper.base.MinToMaxDataCollector;
import com.analyze.stack.pojo.dto.StackDetailRespDTO;
import com.analyze.stack.pojo.model.StackDO;
import com.analyze.stack.pojo.model.StackDetailDO;
import com.analyze.stack.pojo.result.BooleanResult;
import com.analyze.stack.service.StackDetailService;
import com.analyze.stack.util.util.HttpsClientUtil;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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


    public void calcStack() {
        String curData = DateTime.now().toString("yyyy-MM-dd");
        List<StackDO> stackDOS = MinToMaxDataCollector.collectData(stackCollector, 1000);
        List<StackDetailDO> stackDetailDOS = new ArrayList<>();
        for (StackDO stackDO : stackDOS) {
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
            if (CollectionUtils.isNotEmpty(stackDetailDOS)) {
                stackDetailService.saveBatch(stackDetailDOS, 200);
            }
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
