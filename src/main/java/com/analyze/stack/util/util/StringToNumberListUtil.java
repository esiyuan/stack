package com.analyze.stack.util.util;

import com.google.common.base.Splitter;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author guanjie@mgtv.com
 * @create 2020-08-05-11:37
 */
public class StringToNumberListUtil {

    public static List<Integer> stringToIntegerList(String param, char separator) {
        if (StringUtils.isBlank(param)) {
            return Collections.EMPTY_LIST;
        }
        List<String> list = Splitter.on(separator).omitEmptyStrings().trimResults().splitToList(param);
        return CollectionUtils.isEmpty(list) ? Collections.EMPTY_LIST : CollectionExtUtil.toList(list, Ints::tryParse);
    }
    public static List<Long> stringToLongList(String param, char separator) {
        if (StringUtils.isBlank(param)) {
            return Collections.EMPTY_LIST;
        }
        List<String> list = Splitter.on(separator).omitEmptyStrings().trimResults().splitToList(param);
        return CollectionUtils.isEmpty(list) ? Collections.EMPTY_LIST : CollectionExtUtil.toList(list, Longs::tryParse);
    }


}
