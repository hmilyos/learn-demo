package com.hmily.designpattern.domain;

import com.hmily.designpattern.util.AbstractObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName StudentVO
 * @Description 学生的 VO
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StudentVO extends AbstractObject {

    private Long id;

    private String name;

}
