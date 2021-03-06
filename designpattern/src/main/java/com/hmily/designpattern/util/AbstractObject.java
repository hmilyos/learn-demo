package com.hmily.designpattern.util;


import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AbstractObject
 * @Description 基础POJO类
 **/
public abstract class AbstractObject implements Serializable {

    /**
     * 浅度克隆
     * @param clazz 类
     * @return
     * @throws Exception
     */
    public <T> T clone(Class<T> clazz) throws Exception {
        T target = clazz.newInstance();
        BeanCopierUtils.copyProperties(this, target);
        return target;
    }

    /**
     * 浅度克隆
     * @param target 对象
     */
    public <T> void clone(T target) {
        BeanCopierUtils.copyProperties(this, target);
    }

    /**
     * 深度克隆
     * @param clazz
     * @param cloneDirection 克隆方向 CloneDirection
     * @return
     * @throws Exception
     */
    public <T> T clone(Class<T> clazz, Integer cloneDirection) throws Exception {
        // 先完成基本字段的浅克隆
        T target = clazz.newInstance();
        BeanCopierUtils.copyProperties(this, target);

        // 完成所有List类型的深度克隆
        // CategoryDTO
        Class<?> thisClazz = this.getClass();

        Field[] fields = thisClazz.getDeclaredFields();

        for(Field field : fields) {
            field.setAccessible(true);

            // 如果判断某个字段是List类型的
            // field = private List<Relation> relations;
            if(field.getType() != List.class) {
                continue;
            }

            // field.getType() List 不是 List<Relation>
            // List<RelationDTO>集合
            List<?> list = (List<?>) field.get(this);
            if(CollectionUtils.isEmpty(list)) {
                continue;
            }

            // 获取List集合中的泛型类型
            // RelationDTO
            Class<?> listGenericClazz = getListGenericType(field);
            // 获取要克隆的目标类型
            // 假设CloneDirection是反向，此时获取到的就是RelationVO

            Class<?> cloneTargetClazz = getCloneTargetClazz(listGenericClazz, cloneDirection);
            // 将list集合克隆到目标list集合中去
            List clonedList = new ArrayList(list.size());
            cloneList(list, clonedList, cloneTargetClazz, cloneDirection);

            // 获取设置克隆好的list的方法名称
            // setRelations
            Method setFieldMethod = getSetCloneListFieldMethodName(field, clazz);
            setFieldMethod.invoke(target, clonedList);
            // target是CategoryVO对象，此时就是调用CategoryVO的setRelations方法，
            // 将克隆好的List<CategoryVO>给设置进去
        }

        return target;
    }

    /**
     * 获取list集合的泛型类型
     * @param field
     * @return
     * @throws Exception
     */
    private Class<?> getListGenericType(Field field) throws Exception {
        // genericType = List<RelationDTO>，不是List
        Type genericType = field.getGenericType();
        if(genericType instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            return (Class<?>)parameterizedType.getActualTypeArguments()[0];
        }
        return null;
    }

    /**
     * 获取目标类名
     * @param clazz className
     * @param cloneDirection 克隆方向CloneDirection
     * @return
     * @throws Exception
     */
    private Class<?> getCloneTargetClazz(Class<?> clazz,
                                         Integer cloneDirection) throws Exception {
        String cloneTargetClassName = null;

        // ReflectionDTO
        String className = clazz.getName();

//      这里的处理比较暴力， 要是 clone list 时， VO、DTO、DO 得在同一包路径下，要不然就会找不到其 class
        if(cloneDirection.equals(CloneDirection.FORWARD)) {
            if(className.endsWith(DomainType.VO)) {
                cloneTargetClassName = className.substring(0, className.length() - 2) + "DTO";
            } else if(className.endsWith(DomainType.DTO)) {
                cloneTargetClassName = className.substring(0, className.length() - 3) + "DO";
            }
        }

        if(cloneDirection.equals(CloneDirection.OPPOSITE)) {
            if(className.endsWith(DomainType.DO)) {
                cloneTargetClassName =  className.substring(0, className.length() - 2) + "DTO";
            } else if(className.endsWith(DomainType.DTO)) {
                cloneTargetClassName = className.substring(0, className.length() - 3) + "VO";
            }
        }

        return Class.forName(cloneTargetClassName);
    }

    /**
     * 获取设置克隆好的list的方法名称
     * @param field 字段
     * @param clazz 类
     * @return
     * @throws Exception
     */
    private Method getSetCloneListFieldMethodName(Field field, Class<?> clazz) throws Exception {
        String name = field.getName();
        String setMethodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);

        Method setFieldMethod = null;

        for(Method method : clazz.getDeclaredMethods()) {
            if(method.getName().equals(setMethodName)) {
                setFieldMethod = method;
                break;
            }
        }

        return setFieldMethod;
    }

    /**
     * 将一个list克隆到另外一个list
     * @param sourceList
     * @param targetList
     * @param cloneTargetClazz
     * @param cloneDirection
     * @throws Exception
     */
    private void cloneList(List sourceList, List targetList,
                           Class cloneTargetClazz, Integer cloneDirection) throws Exception {
        for(Object object : sourceList) {
            AbstractObject targetObject = (AbstractObject) object;
            // 将集合中的RelationDTO，调用其clone()方法，将其往RelationVO去克隆
            AbstractObject clonedObject = (AbstractObject) targetObject.clone(cloneTargetClazz, cloneDirection);
            // RelationVO的集合
            targetList.add(clonedObject);
        }
    }


}
