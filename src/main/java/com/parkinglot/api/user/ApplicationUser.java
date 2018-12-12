//package com.parkinglot.api.user;
//
//import org.hibernate.annotations.NaturalId;
//
//import javax.persistence.*;
//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//import java.util.List;
//
//@Entity
//@Table(name = "users")
//public class ApplicationUser {
//    @Id
//    @Column(name = "ID")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
//    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
//    private Long id;
//
//    @Column(name = "NAME", length = 32, unique = true)
//    @NotNull
//    @Size(min = 4, max = 32)
//    private String name;
//
//    @Column(name = "USERNAME", length = 32, unique = true)
//    @NotNull
//    @Size(min = 4, max = 32)
//    private String username;
//
//    @NaturalId
//    @NotBlank
//    @Size(max = 40)
//    @Email
//    private String email;
//
//    @Column(name = "PASSWORD", length = 200)
//    @NotNull
//    @Size(min = 4, max = 200)
//    private String password;
//
//    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
//    @JoinTable(
//            name = "USER_AUTHORITY",
//            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
//            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
//    private List<Role> authorities;
//
//    public ApplicationUser() {
//
//    }
//
//    public ApplicationUser(String name, String username, String email, String password, List<Role> authorities) {
//        this.name = name;
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.authorities = authorities;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public List<Role> getAuthorities() {
//        return authorities;
//    }
//
//    public void setAuthorities(List<Role> authorities) {
//        this.authorities = authorities;
//    }
//}