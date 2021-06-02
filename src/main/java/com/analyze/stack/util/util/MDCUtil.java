package com.analyze.stack.util.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * 日志mdc操作
 *
 * @author guanjie@mgtv.com
 * @create 2020-06-05-9:16
 */
public class MDCUtil {

    public static final String LOG_TRACE_ID = "traceId";

    public static void setMdcLogUid() {
        MDC.put(LOG_TRACE_ID, createUid());
    }

    public static void setMdcLogUid(String uId) {
        MDC.put(LOG_TRACE_ID, uId);
    }

    public static void removeMdcLogUid() {
        MDC.remove(LOG_TRACE_ID);
    }

    private static String createUid() {
        return StringUtils.remove(UUID.randomUUID().toString(), '-').substring(10);
    }

    public static String getOrCreateCurrentMdcLogUid() {
        String traceId = MDC.get(LOG_TRACE_ID);
        return StringUtils.isEmpty(traceId) ? createUid() : traceId;
    }

}
