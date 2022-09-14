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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MusicServiceImpl implements MusicService {

    private static final Logger logger = LoggerFactory.getLogger(MusicServiceImpl.class);

    private final MusicRepository musicRepository;
    @Autowired
    @Setter
    private MusicDTOMapper musicDTOMapper;

    public Set<MusicDTO> findAllWithFilter(String filter) {

        Sort sort = Sort.by("artist.name").ascending()
                .and(Sort.by("name").ascending());

        Set<Music> musicSet = musicRepository.findAllWithFilter(filter, sort);

        if (musicSet.isEmpty()) {
            logger.info("Music not found");
            throw new MusicNotFound();
        }

        logger.info("Musics returned successfully");
        return musicDTOMapper.toSetOfDTO(musicSet);
    }

    @Override
    public Music findById(String id) {
        return musicRepository
                .findById(id)
                .orElseThrow(() -> {
            logger.error("Invalid music id");
            return new InvalidMusicException();
        });
    }

}