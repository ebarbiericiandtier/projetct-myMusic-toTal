package com.ciandt.summit.bootcamp2022.service.impl;

import com.ciandt.summit.bootcamp2022.entity.Music;
import com.ciandt.summit.bootcamp2022.exception.InvalidMusicException;
import com.ciandt.summit.bootcamp2022.repository.MusicRepository;
import com.ciandt.summit.bootcamp2022.dto.MusicDTO;
import com.ciandt.summit.bootcamp2022.exception.MusicNotFound;
import com.ciandt.summit.bootcamp2022.service.MusicService;
import com.ciandt.summit.bootcamp2022.service.mapper.MusicDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MusicServiceImpl implements MusicService {

    private static final Logger logger = LoggerFactory.getLogger(MusicServiceImpl.class);

    private final MusicRepository musicRepository;
    @Autowired
    @Setter
    private MusicDTOMapper musicDTOMapper;

    public Slice<Music> findAllWithFilter(String filter, int page, int size) {

        Sort sort = Sort.by("artist.name").ascending()
                .and(Sort.by("name").ascending());

        Pageable pageable = PageRequest.of(page, size, sort);

        Slice<Music> musicSet = musicRepository.findAllWithFilter(filter, pageable);

        if (musicSet.isEmpty()) {
            logger.info("Music not found");
            throw new MusicNotFound();
        }

//       return musicDTOMapper.toSetOfDTO(musicSet);
        logger.info("Musics returned successfully");
        return musicSet;
    }

    @Override
    public Optional<Music> findById(String id) {
        return musicRepository
                .findById(id);
    }

}
