package com.humane.etms.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Hall {
    @Id private String hallCd;
    private String headNm;
    private String bldgNm;
    private String hallNm;

    @Column(columnDefinition = "bit default 0") private boolean isEtc;
}
