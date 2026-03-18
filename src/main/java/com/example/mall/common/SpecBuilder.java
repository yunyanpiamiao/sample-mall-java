package com.example.mall.common;


import java.util.ArrayList;
import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.tuple.ImmutablePair;
import java.util.function.BiFunction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * 查询条件构造工具 用来构建简单的查询条件
 */

public class SpecBuilder<T> {

    private final List<Condition> list = new ArrayList<>();
    private final List<List<Condition>> orlist = new ArrayList<>();

    public static <T> SpecBuilder<T> ins(Class<T> tClass) {
        return new SpecBuilder<T>();
    }


    public SpecBuilder<T> addEq(String column, Object v) {
        if (isEmpty(v)) {
            return this;
        }
        list.add(SC.ins(Op.EQ, column, v));
        return this;
    }

    public SpecBuilder<T> addNotNullEq(String column, Object v) {
        if (isEmpty(v)) {
            list.add(SC.ins(Op.EQ, column, "-1"));
            return this;
        }
        list.add(SC.ins(Op.EQ, column, v));
        return this;
    }

    public SpecBuilder<T> addNotNull(String column) {
        list.add(SC.ins(Op.NOT_NULL, column));
        return this;
    }


    /**
     * if (StringUtil.isNotEmpty(name)) { orlist.add(criteriaBuilder.like(root.get("columnId"), "%"
     * + EscapeUtils.escapeStr(name) + "%"));
     * orlist.add(criteriaBuilder.like(root.get("columnName"), "%" + EscapeUtils.escapeStr(name) +
     * "%")); Predicate[] orArray = new Predicate[list.size()];
     * list.add(criteriaBuilder.or(orlist.toArray(orArray))); }
     *
     * @param
     * @param
     * @return
     */
    public SpecBuilder<T> addEqOr(Map<String, Object> mps) {
        if (isEmpty(mps)) {
            return this;
        }
        List<Condition> orlist = new ArrayList<>();
        for (Map.Entry<String, Object> entry : mps.entrySet()) {
            orlist.add(SC.ins(Op.EQ, entry.getKey(), entry.getValue()));
        }
        this.orlist.add(orlist);
        return this;
    }

    public SpecBuilder<T> addOr(SC... scs) {
        if (isEmpty(scs)) {
            return this;
        }
        orlist.add(Lists.newArrayList(scs));
        return this;
    }

    public SpecBuilder<T> addNotEq(String column, Object v) {
        if (isEmpty(v)) {
            return this;
        }
        list.add(SC.ins(Op.NOT_EQ, column, v));
        return this;
    }

    public SpecBuilder<T> addLike(String column, Object v) {
        if (isEmpty(v)) {
            return this;
        }
        list.add(SC.ins(Op.LIKE, column, v));
        return this;
    }

    public <M> SpecBuilder<T> addIn(String column, List<M> v) {
        if (isEmpty(v)) {
            return this;
        }
        list.add(SC.ins(Op.IN, column, v));
        return this;
    }

    public <M> SpecBuilder<T> addIn(String column, Set<M> v) {
        if (isEmpty(v)) {
            return this;
        }
        list.add(SC.ins(Op.IN, column, v));
        return this;
    }

    public SpecBuilder<T> addGreaterThanOrEqual(String column, Object v) {
        if (isEmpty(v)) {
            return this;
        }
        list.add(SC.ins(Op.GREATER_THAN_EQUAL, column, v));
        return this;
    }

    public SpecBuilder<T> addGreaterThan(String column, Object v) {
        if (isEmpty(v)) {
            return this;
        }
        list.add(SC.ins(Op.GREATER_THAN, column, v));
        return this;
    }

    public SpecBuilder<T> addLessThan(String column, Object v) {
        if (isEmpty(v)) {
            return this;
        }
        list.add(SC.ins(Op.LESS_THAN, column, v));
        return this;
    }

    public SpecBuilder<T> addLessThanOrEqual(String column, Object v) {
        if (isEmpty(v)) {
            return this;
        }
        list.add(SC.ins(Op.LESS_THAN_EQUAL, column, v));
        return this;
    }

    public SpecBuilder<T> addBetween(String column, Object a, Object b) {
        if (isEmpty(a) || isEmpty(b)) {
            return this;
        }
        ImmutablePair<Object, Object> pair = ImmutablePair.of(a, b);
        list.add(SC.ins(Op.BETWEEN, column, pair));
        return this;
    }


    private boolean isEmpty(Object v) {
        if (Objects.isNull(v)) {
            return true;
        }
        if (v instanceof String) {
            if (StringUtils.isEmpty(v)) {
                return true;
            }
        }
        return false;
    }

    public Specification<T> builder() {
        return new Spec<T>(list, orlist);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnumV {

        Expression e;
        Object v;
    }

    public enum Op {
        /**
         * 添加等于的条件
         */
        EQ((cb, o) -> cb.equal(o.getE(), o.getV())),

        /**
         * 添加等于的条件
         */
        NOT_NULL((cb, o) -> cb.isNotNull(o.getE())),

        /**
         * 添加not eq条件
         */
        NOT_EQ((cb, o) -> cb.notEqual(o.getE(), o.getV())),

        /**
         * 添加like的条件
         */
        LIKE((cb, o) -> cb.like(o.getE(), "%" + o.getV() + "%")),

        /**
         * 添加in的条件
         */
        IN((cb, o) -> {
            Path<Object> e = (Path<Object>) o.getE();
            CriteriaBuilder.In<Object> in = cb.in(e);
            in.value(o.getV());
            return in;
        }),

        GREATER_THAN_EQUAL((cb, o) -> {
            Expression e = o.getE();
            Object v = o.getV();
            if (v instanceof Comparable) {
                Comparable v00 = (Comparable) v;
                return cb.greaterThanOrEqualTo(e, v00);
            }
            return null;
        }),

        LESS_THAN_EQUAL((cb, o) -> {
            Expression e = o.getE();
            Object v = o.getV();
            if (v instanceof Comparable) {
                Comparable v00 = (Comparable) v;
                return cb.lessThanOrEqualTo(e, v00);
            }
            return null;
        }),

        GREATER_THAN((cb, o) -> {
            Expression e = o.getE();
            Object v = o.getV();
            if (v instanceof Comparable) {
                Comparable v00 = (Comparable) v;
                return cb.greaterThan(e, v00);
            }
            return null;
        }),
        LESS_THAN((cb, o) -> {
            Expression e = o.getE();
            Object v = o.getV();
            if (v instanceof Comparable) {
                Comparable v00 = (Comparable) v;
                return cb.lessThan(e, v00);
            }
            return null;
        }),

        BETWEEN((cb, o) -> {
            Expression e = o.getE();
            Object v = o.getV();
            if (v instanceof ImmutablePair) {
                ImmutablePair v00 = (ImmutablePair) v;
                Object left = v00.getLeft();
                Object right = v00.getRight();
                if (left instanceof Comparable && right instanceof Comparable) {
                    Comparable left_1 = (Comparable) left;
                    Comparable right_1 = (Comparable) right;
                    return cb.between(e, left_1, right_1);
                }
            }
            return null;
        }),
        ;

        private BiFunction<CriteriaBuilder, EnumV, Predicate> f;

        Op(BiFunction<CriteriaBuilder, EnumV, Predicate> f) {
            this.f = f;
        }

    }


    public static class Spec<T> implements Specification<T> {

        private final List<Condition> conditionList;

        private final List<List<Condition>> orConditionList;

        public Spec() {
            this(Collections.emptyList());
        }

        public Spec(List<Condition> oList) {
            Objects.requireNonNull(oList);
            this.conditionList = oList;
            this.orConditionList = Collections.emptyList();
        }

        public Spec(List<Condition> oList, List<List<Condition>> orConditionList) {
            Objects.requireNonNull(oList);
            this.conditionList = oList;
            this.orConditionList = orConditionList;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery,
                CriteriaBuilder criteriaBuilder) {
            List<Predicate> list = new ArrayList<>();
            conditionList.forEach(c -> list.add(c.getPredicate(root, criteriaBuilder)));

            orConditionList.forEach(orList -> {
                List<Predicate> orPredicates = new ArrayList<>();
                orList.forEach(c -> orPredicates.add(c.getPredicate(root, criteriaBuilder)));
                list.add(criteriaBuilder.or(
                        orPredicates.toArray(new Predicate[orPredicates.size()])));
            });
            Predicate[] array = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(array));
        }
    }

    public SpecBuilder add() {
        return this;
    }


    public interface Condition {

        /**
         * 获取判断
         *
         * @param root
         * @param criteriaBuilder
         * @param <T>
         * @return
         */
        <T> Predicate getPredicate(Root<T> root, CriteriaBuilder criteriaBuilder);

    }

    /**
     * SingleCondition
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SC implements Condition {

        private Op op;

        private ImmutablePair<String, Object> v;


        @Override
        public <T> Predicate getPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
            Op op = this.getOp();
            ImmutablePair<String, Object> v = this.getV();
            return op.f.apply(criteriaBuilder, new EnumV(root.get(v.getLeft()), v.getRight()));
        }

        public static SC ins(Op op, String col, Object o) {
            Object escape = Escape.escape(o);
            return new SC(op, ImmutablePair.of(col, escape));
        }

        public static SC ins(Op op, String col) {
            return new SC(op, ImmutablePair.of(col, null));
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrCondition implements Condition {

        private List<SC> list = Lists.newArrayList();

        public void addSingleCon(Op op, String col, Object o) {
            if (Objects.isNull(o)) {
                return;
            }
            list.add(SC.ins(op, col, o));
        }

        @Override
        public <T> Predicate getPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
            List<Predicate> orlist = new ArrayList<Predicate>();
            list.forEach(l -> orlist.add(l.getPredicate(root, criteriaBuilder)));
            Predicate[] orArray = new Predicate[list.size()];
            return criteriaBuilder.or(orlist.toArray(orArray));
        }
    }


    public static class Escape {

        public static Object escape(Object o) {
            if (o instanceof String) {
                String s0 = (String) o;
                return escapeStr(s0);

            }
            return o;
        }

        /**
         * 转义字符%和_
         *
         * @param str 目标字符串
         * @return
         */
        public static String escapeStr(String str) {
            if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
                return str;
            }
            if (str.startsWith("%") || str.startsWith("_")) {
                str = "\\" + str;
            }

            if (str.endsWith("_")) {
                int index = str.indexOf("_");
                str = str.substring(0, index) + "\\" + "_";
            }

            if (str.endsWith("%")) {
                int index = str.indexOf("%");
                str = str.substring(0, index) + "\\" + "%";
            }
            return str;
        }
    }

}