package ru.intabia.sso.demo.backend.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "userentity")
@Entity
@Data
public class UserEntity {

    @Id
    @SequenceGenerator(name = "seq_client_id", sequenceName = "seq_client_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_client_id")
    private Long id;

    @Column
    private String username;

    @Column
    @NotNull
    private String email;

    @Column
    @NotNull
    private String password;

    @Column
    private String phone;
}
