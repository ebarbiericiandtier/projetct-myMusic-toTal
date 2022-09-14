package com.ciandt.summit.bootcamp2022.service;

import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.entity.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.Set;

public interface MusicService {

   Slice<Music> findAllWithFilter(String filter, int page, int size);

    Music findById(String id);
}
