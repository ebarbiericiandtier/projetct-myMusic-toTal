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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MusicServiceImpl implements MusicService {

    private final MusicRepository musicRepository;
    @Autowired
    @Setter
    private MusicDTOMapper musicDTOMapper;

    public Slice<Music> findAllWithFilter(String filter, Integer page, Integer size) {

        Sort sort = Sort.by("artist.name").ascending()
                .and(Sort.by("name").ascending());

        Pageable pageable = PageRequest.of(
                Optional.ofNullable(page).orElse(1),
                Optional.ofNullable(size).orElse(10),
                sort);

        Slice<Music> musicSet = musicRepository.findAllWithFilter(filter, pageable);

        if (musicSet.isEmpty())
            throw new MusicNotFound();

//       return musicDTOMapper.toSetOfDTO(musicSet);
        return musicSet;
    }

    @Override
    public Music findById(String id) {
        return musicRepository
                .findById(id)
                .orElseThrow(InvalidMusicException::new);
    }

}
