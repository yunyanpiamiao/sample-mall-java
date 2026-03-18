package com.example.mall.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

@UtilityClass
public class ListUtils {

    /***
     * 大小分段，一个大list 根据指定大小 分割为 多个小list
     *
     * @param data 源list
     * @param splitSize 分割大小
     * @return {@link List <  List <T>>}
     * @date 2021/6/9
     */
    public <T> List<List<T>> split(List<T> data, int splitSize) {
        int len = data.size();
        if (len <= splitSize) {
            List<List<T>> list = new ArrayList<>();
            list.add(data);
            return list;
        }

        List<List<T>> list = new ArrayList<>((len / splitSize) + 1); //
        for (int i = 0; i < len; i += splitSize) {
            int limit = i + splitSize;
            limit = limit > len ? len : limit;
            list.add(data.subList(i, limit));
        }
        return list;
    }


    public <T> Optional<T> getLast(List<T> list) {
        if (list == null || list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(list.size() - 1));
    }

    public <T> Optional<T> getFirst(List<T> list) {
        if (list == null || list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    public <K, T> Map<K, T> toMap(List<T> list, Function<T, K> keyMapper) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return toMap(list, keyMapper, Function.identity());
    }


    public <K, T, V> Map<K, V> toMap(List<T> list, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(keyMapper, valueMapper, (m1, m2) -> m2));
    }


    public <T, V> List<V> map(List<T> list, Function<T, V> mapper) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(mapper).collect(Collectors.toList());
    }

    public <K, T,A,V> Map<K, V> groupBy(List<T> list, Function<T, K> keyMapper,Collector< T, A, V> downstream) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.groupingBy(keyMapper,downstream));
    }


    public <K, T> Map<K, List<T>> groupBy(List<T> list,
                                          Function<T, K> keyMapper) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.groupingBy(keyMapper));
    }

    public <T, P> List<T> doGetList(List<P> paramList, Function<List<P>, List<T>> supplier) {
        if (CollectionUtils.isEmpty(paramList)) {
            return Collections.emptyList();
        }
        return supplier.apply(paramList);
    }
    @SuppressWarnings("unchecked")
    public <T, P> List<T> doGetList(List<P> paramList) {
        if (CollectionUtils.isEmpty(paramList)) {
            return Collections.emptyList();
        }
        return (List<T>) doGetList(paramList, Function.identity());
    }

    public <T, P> List<T> doGetAndConvert(List<P> paramList, Function<P, T> supplier) {
        return doGetList(paramList, p -> p.stream().map(supplier).collect(Collectors.toList()));
    }




}
