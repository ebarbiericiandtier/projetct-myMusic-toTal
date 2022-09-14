package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.entity.Music;
import org.springframework.data.domain.Slice;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.Set;

public interface MusicService {

   Slice<Music> findAllWithFilter(String filter, int page, int size);

    Optional<Music> findById(String id);
}
