package com.tce.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table("employees")
public class Employee {

    @Id
    private Long id;

    private String name;

}
