package com.ciandt.summit.bootcamp2022.entity;


import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "Playlists")
@Data
@Cacheable
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE )
public class Playlist implements Serializable {

    @Id
    private String id;

    @Cache(usage= CacheConcurrencyStrategy.READ_WRITE )
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "PlaylistMusicas",
            joinColumns = @JoinColumn(name = "PlaylistId"),
            inverseJoinColumns = @JoinColumn(name = "MusicaId"))
    private Set<Music> musics;

}
