package com.humane.etms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"admissionCd", "userId"})})
@Getter
@Setter
@ToString
public class UserAdmission {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;

    @Column(nullable = false) private String admissionCd;

    @ManyToOne @JoinColumn(name = "userId") private User user;
}
