package ru.skypro.homework.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skypro.homework.model.dto.Role;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@EqualsAndHashCode
@Table(name = "user_info")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "image")
    private String image;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "author")
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "author")
    private List<AdsEntity> ads;

    @OneToOne
    @JoinColumn(name = "avatar_id")
    private ImageEntity avatar;
}
