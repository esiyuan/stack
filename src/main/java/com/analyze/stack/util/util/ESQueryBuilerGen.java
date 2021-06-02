package com.analyze.stack.util.util;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

/**
 * es 查询条件生成
 * <br>
 * if (StringUtils.isNotBlank(pageQueryBO.getParticipantName())){queryBuilder.must(QueryBuilders.matchPhraseQuery("participantName",pageQueryBO.getParticipantName()));}
 *
 * @author qusan
 * @create 2019-07-30-21:00
 */
public class ESQueryBuilerGen {

    /**
     * 参与人姓名
     */
    private String participantName;
    /**
     * 签约状态
     *
     * @link com.ggj.center.seller.enums.shop.ShopSignStatusEnum
     */
    private Integer signStatus;

    /**
     * 合同编号
     */
    private String contractNumber;

    /**
     * 合同模板编号
     */
    private String templateNo;

    /**
     * 签约主体
     */
    private Integer signPartyId;

    /**
     * 创建时间起始时间
     * 大于等于
     */
    private String createTimeStart;
    /**
     * 创建时间截止时间
     * 小于
     */
    private String createTimeEnd;


    /**
     * 合同业务类型 1-店铺入驻，2-商家贷款 3-个人贷款
     *
     * @link com.ggj.center.seller.enums.sign.ContractBusinessTypeEnum
     */
    private Integer contractBusinessType;

    /**
     * 合同关系类型 1-公司->店铺 2-公司-个人
     *
     * @link com.ggj.center.seller.enums.sign.ContractRelationTypeEnum
     */
    private Integer contractRelationType;

    public static void main(String[] args) {

        Field[] fields = FieldUtils.getAllFields(ESQueryBuilerGen.class);
        for (Field field : fields) {
            String str = "";
            if (field.getType().getSuperclass() == Number.class) {
                str += "if(null != pageQueryBO.get" + convert(field.getName()) + "()){";
            } else {
                str += "if (StringUtils.isNotBlank(pageQueryBO.get" + convert(field.getName()) + "())){";
            }
            str += "queryBuilder.must(QueryBuilders.matchPhraseQuery(\"" + field.getName() + "\"";
            str += "," + "pageQueryBO.get" + convert(field.getName()) + "()));";
            str += "}";
            System.out.println(str);
        }

    }


    private static String convert(String str) {
        String st1 = str.substring(0, 1);
        String st2 = str.substring(1);
        return st1.toUpperCase() + st2;
    }

}
