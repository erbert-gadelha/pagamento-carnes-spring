package com.ebgr.pagamento_carnes.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "tb_user")
@ToString
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String login;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    public UserModel(String name, String login, String password) {
        this.login=login;
        this.name=name;
        this.password=password;
    }

    public String getName(){return name;}
    public String getPassword(){return password;}
    public void setPassword(String encode) { this.password = encode; }

    /*public enum UserRole {
        ROLE_CUSTOMER,
        ROLE_ADMINISTRATOR
    }*/
}

