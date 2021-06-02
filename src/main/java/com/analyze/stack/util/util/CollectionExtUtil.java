package com.analyze.stack.util.util;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 集合拓展类
 *
 * @author guanjie@mgtv.com
 * @create 2020-04-13-16:16
 */
public class CollectionExtUtil {

    /**
     * 更改名字, 使其匹配更多的场景,  转换为其它对象的 list
     *
     * @param c
     * @param mapper
     * @param <T>
     * @param <F>
     * @param <C>
     * @return
     */
    public static <T, F, C extends Collection<F>> List<T> toList(C c, Function<? super F, ? extends T> mapper) {
        return c.stream().map(mapper).collect(Collectors.toList());
    }


    public static <T> List<T> filterList(List<T> list, Predicate<T> predicate) {
        return (list == null || list.size() == 0) ? Collections.EMPTY_LIST :
                list.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <T, D> Map<T, List<D>> groupBy(List<D> list, Function<D, T> function) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        Map<T, List<D>> result = new HashMap<>();
        for (D d : list) {
            T apply = function.apply(d);
            List<D> dList = result.get(apply);
            if (dList == null) {
                result.put(apply, Lists.newArrayList(d));
            } else {
                dList.add(d);
            }
        }
        return result;
    }

    public static <T, D, F> Map<T, List<F>> groupBy(List<D> list, Function<D, T> function, Function<D, F> valueF) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        Map<T, List<F>> result = new HashMap<>();
        for (D d : list) {
            T apply = function.apply(d);
            List<F> dList = result.get(apply);
            if (dList == null) {
                result.put(apply, Lists.newArrayList(valueF.apply(d)));
            } else {
                dList.add(valueF.apply(d));
            }
        }
        return result;
    }


    /**
     * 更改名字, 使其匹配更多的场景,  转换为其它对象的 set
     *
     * @param c
     * @param mapper
     * @param <T>
     * @param <F>
     * @param <C>
     * @return
     */
    public static <T, F, C extends Collection<F>> Set<T> toSet(C c, Function<? super F, ? extends T> mapper) {
        return c.stream().map(mapper).collect(Collectors.toSet());
    }

    /**
     * 根据集合中某一对象属性 组件 map
     * 替代  receiveAddressList.stream().collect(Collectors.toMap(OrderReceiveAddress::getId, v -> v));
     *
     * @param collection
     * @param fun
     * @param <T>
     * @param <F>
     * @param <C>
     * @return
     */
    public static <T, F, C extends Collection<F>> Map<T, F> toMap(C collection, Function<? super F, ? extends T> fun) {
        if (CollectionUtils.isEmpty(collection)) {
            return Collections.emptyMap();
        }
        return collection.stream().collect(Collectors.toMap(fun, v -> v, (a, b) -> a));
    }

    public static <F, K, V, C extends Collection<F>> Map<K, V> toMap(C collection, Function<? super F, ? extends K> keyFun, Function<? super F, ? extends V> valueFun) {
        if (CollectionUtils.isEmpty(collection)) {
            return Collections.emptyMap();
        }
        return collection.stream().collect(Collectors.toMap(keyFun, valueFun, (a, b) -> a));
    }
}
