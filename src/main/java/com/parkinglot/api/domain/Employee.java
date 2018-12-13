package com.parkinglot.api.domain;

import com.parkinglot.api.user.Role;
import com.parkinglot.api.user.RoleName;
import org.hibernate.annotations.NaturalId;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.*;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", length = 32, unique = true)
    @NotNull
    @Size(min = 4, max = 32)
    private String name;

    @Column(name = "USERNAME", length = 32, unique = true)
    @NotNull
    @Size(min = 4, max = 32)
    private String username;

    @NaturalId
    @NotBlank
    @Size(max = 50)
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(name = "PASSWORD", length = 200)
    @NotNull
    @Size(min = 4, max = 200)
    private String password;

    //---------------------------------------------------

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(
        name = "EMPLOYEE_AUTHORITY",
        joinColumns = {@JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
    private List<Role> authorities;

    @Column(name = "STATUS", nullable = false)
    private String status = "WORKING";

    protected Employee() {
    }

    public Employee(String name, String username, String email, String password, List<RoleName> authorities) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = "no one care";
        this.authorities = authorities.stream().map(roleName -> {
            Role role = new Role();
            role.setName(roleName);
            return role;
        }).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Role> authorities) {
        this.authorities = authorities;
    }
}

