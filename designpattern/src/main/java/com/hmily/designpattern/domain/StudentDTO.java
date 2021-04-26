package com.hmily.designpattern.domain;

import com.hmily.designpattern.util.AbstractObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName StudentDTO
 * @Description 学生的 DTO
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StudentDTO extends AbstractObject {

    private Long id;

    private String name;

}
