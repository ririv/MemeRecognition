package com.riri.emojirecognition.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(indexes = {
        @Index(columnList = "subId"),
        @Index(columnList = "tag")
        })
public class Img implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String tag;

//    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
//    @JoinColumn(name = "img_id",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
//    private Set<Tag> tags;

    private String description;

    private String owner;

    private String sourcename;

    private String subDir;

    //subId为此标签中图片的序号，用于通过标签推荐
    private Long subId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSourcename() {
        return sourcename;
    }

    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }

    public String getSubDir() {
        return subDir;
    }

    public void setSubDir(String subDir) {
        this.subDir = subDir;
    }

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }
}
