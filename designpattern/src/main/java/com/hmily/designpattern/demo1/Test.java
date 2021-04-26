package com.hmily.designpattern.demo1;

import com.hmily.designpattern.domain.ClassDTO;
import com.hmily.designpattern.domain.StudentDTO;
import com.hmily.designpattern.util.CloneDirection;
import com.hmily.designpattern.util.JSONUtils;
import com.hmily.designpattern.domain.ClassVO;
import com.hmily.designpattern.domain.StudentVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Test
 * @Description 演示 享元模式 + 原型模式
 **/
public class Test {
    public static void main(String[] args) throws Exception {
        StudentDTO studentDTO = StudentDTO.builder().id(1L).name("张三").build();
        StudentDTO studentDTO2 = StudentDTO.builder().id(2L).name("李四").build();
        List<StudentDTO> studentDTOs = new ArrayList<>(2);
        studentDTOs.add(studentDTO);
        studentDTOs.add(studentDTO2);
        ClassDTO classDTO = ClassDTO.builder().id(1L).name("一年级").students(studentDTOs).build();
//        浅拷贝
        StudentVO studentVO = studentDTO.clone(StudentVO.class);
        StudentVO studentVO2 = new StudentVO();
        studentDTO2.clone(studentVO2);
        System.out.println("DTO1: " + studentDTO.toString());
        System.out.println("VO1: " + studentVO.toString());
        System.out.println("DTO2: " + studentDTO2.toString());
        System.out.println("VO2: " + studentVO2.toString());

//        注意，深拷贝时，如果对象里有 List 时，List 的 class 要和 转换后 的 class 在同一包路径下，要不然会找不到其 对应的 class
        ClassVO classVO = classDTO.clone(ClassVO.class, CloneDirection.OPPOSITE);
        System.out.println("classDTO: " + JSONUtils.toJsonString(classDTO));
        System.out.println("classVO: " + JSONUtils.toJsonString(classVO));
        ClassDTO classDTO1 = classVO.clone(ClassDTO.class, CloneDirection.FORWARD);
        System.out.println("classDTO1: " + JSONUtils.toJsonString(classDTO1));
    }
}
