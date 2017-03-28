/**
 * 
 */
package com.hgsoft.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Administrator
 *使用该工具类的对象必须要实现Serializable接口，否则是没有办法实现克隆的。例如
 *
 *	实体类
 public class Person implements Serializable{
    private static final long serialVersionUID = 2631590509760908280L;

    ..................
    //去除clone()方法

}

调用方式：
Person person1 =  new Person("张三",email);  
Person person2 =  CloneUtils.clone(person1);  
 *
 */
public class CloneUtils {
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T clone(T obj){
        T cloneObj = null;
        try {
            //写入字节流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream obs = new ObjectOutputStream(out);
            obs.writeObject(obj);
            obs.close();
            
            //分配内存，写入原始对象，生成新对象
            ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(ios);
            //返回生成的新对象
            cloneObj = (T) ois.readObject();
            ois.close();
            out.close();
            ios.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloneObj;
    }
}