package com.github.nullptr7.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @ApiModelProperty("Represents the ID which is unique to an Address.")
    private Long id;

    @ApiModelProperty("Represents the addressLine1 for given Address.")
    private String addressLine1;

    @ApiModelProperty("Represents the addressLine2 for given Address.")
    private String addressLine2;

    @ApiModelProperty("Represents the pinCode for given Address.")
    private Integer pinCode;

}
