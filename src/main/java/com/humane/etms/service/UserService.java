package com.humane.etms.service;

import com.humane.etms.model.UserAdmission;
import com.humane.etms.model.UserRole;
import com.humane.etms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("userService")
public class UserService implements UserDetailsService {

    @Autowired UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        com.humane.etms.model.User user = userRepository.findOne(userId);
        List<GrantedAuthority> authorities = buildUserAuthority(user.getUserRoles());
        List<String> admissions = buildUserAdmission(user.getUserAdmissions());

        return new CustomUserDetails(user.getUserId(), user.getPassword(), authorities, admissions);
    }

    private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {
        List<GrantedAuthority> authorities = new ArrayList<>(0);
        for (UserRole userRole : userRoles) {
            authorities.add(new SimpleGrantedAuthority(userRole.getRoleName()));
        }

        return authorities;
    }

    private List<String> buildUserAdmission(Set<UserAdmission> userAdmissions) {
        List<String> admissions = null;
        if (userAdmissions != null && userAdmissions.size() > 0) {
            admissions = new ArrayList<>();
            for (UserAdmission userAdmission : userAdmissions) {
                admissions.add(userAdmission.getAdmissionCd());
            }
        }
        return admissions;
    }
}
