package com.humane.etms.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AttendWaitHall {
    @Id @GeneratedValue
    private Long _id;
    private String hallCd;
    private String groupNm;
}