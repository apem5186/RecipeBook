package com.recipe.recipebook.dto;

import com.recipe.recipebook.entity.Playlist;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlaylistDTO {

    private String videoId;

    private String title;

    private String description;

    private String thumbnailUrl;

    private boolean favorite;

    public PlaylistDTO(Playlist playlist) {
        this.videoId = playlist.getVideoId();
        this.title = playlist.getTitle();
        this.description = playlist.getDescription();
        this.thumbnailUrl = playlist.getThumbnailUrl();
        this.favorite = playlist.isFavorite();
    }
}
