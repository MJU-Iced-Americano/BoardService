package com.mju.board.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "homebannerimage")
public class HomeBanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long homeBannerIndex;

    @Column(name = "image_url")
    private String imageUrl;
    @Builder
    public HomeBanner(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
