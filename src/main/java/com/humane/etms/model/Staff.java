package com.humane.etms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@ToString
public class Staff {
    @Id @GeneratedValue private Long _id;
    @Column private String staffNm;
    @Column private String phoneNo;
    @Column private String bldgNm;
}
