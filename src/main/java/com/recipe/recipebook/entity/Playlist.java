package com.recipe.recipebook.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String videoId;

    @Column
    private String title;

    @Column(length = 5000)
    private String description;

    @Column
    private String thumbnailUrl;

    @Column
    private boolean favorite = false;

    public Playlist(String videoId, String title, String description, String thumbnailUrl) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }
}
