package com.zzqangel.ddd;

import com.zzqangel.ddd.model.Entity;
import com.zzqangel.ddd.reflect.AnnotationUtils;
import com.zzqangel.ddd.reflect.ClassUtils;
import com.zzqangel.ddd.reflect.FieldUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

import java.lang.reflect.Field;
import java.util.List;

@Component
public class EntitySetter implements ApplicationContextAware, EmbeddedValueResolverAware {

    private ApplicationContext context;

    private StringValueResolver stringValueResolver;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public <T extends Entity> T setEntity(T t) {
        List<Field> fieldList = FieldUtils.getDeclaredFieldList(t.getClass());
        if(fieldList != null && !fieldList.isEmpty()) {
            fieldList.stream().forEach(field -> {
                //Value
                Value value = AnnotationUtils.getFiledAnnotation(field, Value.class);
                if(value != null) {
                    String val = value.value();
                    String setValue = this.getValue(val);
                    if(setValue == null) throw new RuntimeException("Field " + field + " is not be set init value");
                    FieldUtils.setField(field, t, setValue);
                    return;
                }
                //Autowired
                Autowired autowired = AnnotationUtils.getFiledAnnotation(field, Autowired.class);
                if(autowired != null) {
                    //是否存在Qualifier注解,如果存在，且注解类是当前类或者当前类的超类，则直接注入
                    Qualifier qualifier = AnnotationUtils.getFiledAnnotation(field, Qualifier.class);
                    if(qualifier != null) {
                        String s = qualifier.value();
                        Object o = context.getBean(s);
                        if(o == null) throw new RuntimeException("Qualifier " + qualifier + " on Field " + field + " is not exist");
                        if(!ClassUtils.isSuperClass(FieldUtils.getType(field), o.getClass()))
                            throw new RuntimeException("Qualifier " + qualifier + " on Field " + field + " can not assign to");
                        FieldUtils.setField(field, t, o);
                        return;
                    }
                    //没有Qualifier注解，则查询当前类是否存在
                    //如果当前类没有，则查询
                    Object o = context.getBean(FieldUtils.getType(field));
                    //先check原型类
                    if(o != null) {
                        FieldUtils.setField(field, t, o);
                    } else {
                        //原型类不存在，则查询接口类是否存在
                        //接口类如果不存在，则查询是否存在Qualifier注解
                    }
                    if(autowired.required() && FieldUtils.get(field, t) == null) {
                        throw new RuntimeException("Field " + field + " can't find autowired Class");
                    }
                    return;
                }
                //Resource
            });
        }
        return t;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
        this.stringValueResolver = stringValueResolver;
    }

    public String getValue(String name) {
        return stringValueResolver.resolveStringValue(name);
    }
}
