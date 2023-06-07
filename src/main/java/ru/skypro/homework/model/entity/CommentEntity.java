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
@Table(name = "comment_info")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk")
    private Integer pk;

    @Column(name = "author_image")
    private String authorImage;

    @Column(name = "author_firstName")
    private String authorFirstName;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "text")
    private String text;

    @ManyToOne()
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "ads_id")
    AdsEntity ads;

}
