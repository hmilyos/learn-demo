package com.hmily.designpattern.domain;

import com.hmily.designpattern.util.AbstractObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ClassDTO
 * @Description 班级 DTO
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClassDTO extends AbstractObject {

    private Long id;

    private String name;

    private List<StudentDTO> students;
}
