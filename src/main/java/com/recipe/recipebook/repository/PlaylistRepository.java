package com.recipe.recipebook.repository;

import com.recipe.recipebook.entity.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Optional<Playlist> findByVideoId(String id);

    void deleteByVideoId(String id);

    Boolean existsPlaylistByVideoId(String videoId);

    Page<Playlist> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<Playlist> findByFavoriteTrue();
}
