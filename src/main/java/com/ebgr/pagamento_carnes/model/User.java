package com.ebgr.pagamento_carnes.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String login;
    private String name;
    @Setter
    private String password;


    public User (String name, String login, String password) {
        this.login=login;
        this.name=name;
        this.password=password;
    }

    @Override
    public String toString() {
        return String.format("""
                User: {
                    id: %d,
                    name: %s,
                    password: %s
                }
                """, getId(),getName(), getPassword());
    }
}