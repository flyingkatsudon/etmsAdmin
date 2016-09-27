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
public class User {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long userId;

    @Column(unique = true, length = 64) private String username;
    @Column private String password;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY) private Set<UserRole> userRoles;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY) private Set<UserAdmission> userAdmissions;
}
