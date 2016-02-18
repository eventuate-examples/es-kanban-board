package net.chrisrichardson.eventstore.examples.kanban.common.board.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by popikyardo on 07.10.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Board {
    @Id
    private String id;

    private String title;
    private String createdBy;
    private Date createdDate;
    private Date updatedDate;
    private String updatedBy;

    public Board() {
    }

    public Board(String id, String title, String createdBy) {
        this.id = id;
        this.title = title;
        this.createdBy = createdBy;
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }

    public Board(String id, String title, String createdBy, Date createdDate, Date updatedDate, String updatedBy) {
        this.id = id;
        this.title = title;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.updatedBy = updatedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
