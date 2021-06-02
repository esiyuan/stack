package com.analyze.stack.util.util.validate;

import lombok.Data;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 校验工具类
 *
 * @author qusan
 * @create 2019-08-21-20:14
 */
public class ValidationUtil {

    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * 校验对象
     *
     * @param t      bean
     * @param groups 校验组
     * @return ValidResult
     */
    public static <T> ValidResult validateBean(T t, Class<?>... groups) {
        ValidResult result = new ValidResult();
        Set<ConstraintViolation<T>> violationSet = validator.validate(t, groups);
        boolean hasError = violationSet != null && violationSet.size() > 0;
        if (hasError) {
            result.setHasErrors(hasError);
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> violation : violationSet) {
                sb.append(violation.getPropertyPath().toString()).append(violation.getMessage());
            }
            result.setErrorMsg(sb.toString());
        }
        return result;
    }

    @Data
    private static class Person {
        @NotNull
        private String name;
    }

    @Data
    private static class Demo {
        @NotEmpty
        @Valid
        List<Person> list;

    }

    public static void main(String[] args) {
        Demo t = new Demo();
        ArrayList<Person> list = new ArrayList<>();
        list.add(new Person());
        t.setList(list);
        System.out.println(validateBean(t));
    }

}
