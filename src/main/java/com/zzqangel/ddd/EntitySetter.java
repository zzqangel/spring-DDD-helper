package com.zzqangel.ddd;

import com.zzqangel.ddd.model.Entity;
import com.zzqangel.ddd.reflect.AnnotationUtils;
import com.zzqangel.ddd.reflect.FieldUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
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
                    Object o = context.getBean(FieldUtils.getType(field));
                    if(o != null) {
                        FieldUtils.setField(field, t, o);
                    }

                    if(autowired.required()) {
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
