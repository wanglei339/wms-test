package com.lsh.wms.core.service.location;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 利用反射,将父类的参数值,传递给子类
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/28 下午2:15
 */
@Component
public class FatherToChildUtil {
    public void fatherToChild (Object father,Object child) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(!(child.getClass().getSuperclass()==father.getClass())){
            throw new RuntimeException("child不是father的子类");
        }
        Class fatherClass= father.getClass();
        Field ff[]= fatherClass.getDeclaredFields();
        for(int i=0;i<ff.length;i++){
            Field f=ff[i];//取出每一个属性，如deleteDate
            Class type=f.getType();
            Method m=fatherClass.getMethod("get"+upperHeadChar(f.getName()));//方法getDeleteDate
            Object obj=m.invoke(father);//取出属性值
            f.set(child,obj);
        }
    }

    /**
     * 首字母大写，in:deleteDate，out:DeleteDate
     */
    public String upperHeadChar(String in){
        String head=in.substring(0,1);
        String out=head.toUpperCase()+in.substring(1,in.length());
        return out;
    }
}
