package ru.skypro.homework.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@EqualsAndHashCode
@Table(name = "image_info")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "media_type")
    private String mediaType;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @OneToOne(mappedBy = "avatar")
    private UserEntity user;
}
