package com.github.nullptr7.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.EAGER;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue
    @ApiModelProperty("Represents the ID which is unique to an Employee.")
    private Long id;

    @NotBlank
    @ApiModelProperty("Represents the First Name of the Employee.")
    private String firstName;

    @NotBlank
    @ApiModelProperty("Represents the Last Name of the Employee.")
    private String lastName;

    @ApiModelProperty("Represents the Age of the Employee.")
    private Integer age;

    @NotBlank
    @ApiModelProperty("Represents the gender of the Employee.")
    private String gender;

    @ApiModelProperty("Represents the role of the Employee.")
    private String role;

    @NotNull
    @ManyToOne(fetch = EAGER, optional = false)
    @ApiModelProperty("Represents the address ID of an Employee.")
    private Address address;
}
