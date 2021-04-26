package com.hmily.designpattern.domain;

import com.hmily.designpattern.util.AbstractObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ClassVO
 * @Description 班级 VO
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClassVO extends AbstractObject {

    private Long id;

    private String name;

    private List<StudentVO> students;

}
