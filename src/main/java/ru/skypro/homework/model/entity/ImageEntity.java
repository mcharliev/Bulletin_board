package ru.skypro.homework.model.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@ToString
@Entity
@EqualsAndHashCode
@Table(name = "image_info")
public class ImageEntity {

    @Id
    private String id;
    @Lob
    @Column(name = "image")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] data;

}
