package com.example.do_it_api.sticker;

import com.example.do_it_api.security.PrivateEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "todo")
@Getter
@Setter
public class Sticker implements PrivateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String headline;
    @Column(columnDefinition = "text")
    private String text;
    private String color;
    @Column(nullable = false)
    private Long userId;

}
