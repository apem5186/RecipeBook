package com.recipe.recipebook.repository;

import com.recipe.recipebook.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Playlist findByVideoId(String id);

    void deleteByVideoId(String id);

    Boolean existsPlaylistByVideoId(String videoId);
}
