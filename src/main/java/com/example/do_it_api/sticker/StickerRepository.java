package com.example.do_it_api.sticker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StickerRepository extends JpaRepository<Sticker, Long> {
    List<Sticker> findAllByUserIdOrderByIdAsc(Long userId);
}
