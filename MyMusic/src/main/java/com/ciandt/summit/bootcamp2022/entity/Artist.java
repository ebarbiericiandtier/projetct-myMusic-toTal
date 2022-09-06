package com.ciandt.summit.bootcamp2022.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "Artistas")
@Getter
@Setter
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE )
public class Artist implements Serializable {

    @Id
    private String id;
    @Column(name = "Nome")
    private String name;

}

