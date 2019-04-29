package com.riri.emojirecognition.domain;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames="enabled")})
public class Model implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Timestamp updateTime;

    private String description;

    //此路径以classpath为根路径
    private String path;

    private int height;

    private int width;

    private int channels; //通道

    private String labels; //使用,隔开

    private Boolean enabled; //使用唯一约束，不启用的模型设置为null，启用的则设置为true

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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public String[] getLabels() {
        return labels.split(",");
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public void setLabels(String[] labels) {
        this.labels = StringUtils.join(labels,",");
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
