package org.latifah.employeedashboardback.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "archives")
public class Archive {
    @Id
    private String id;
    @Lob
    private String data; // JSON backup

    private LocalDateTime archivedAt;

    // getters and setters

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getData() {
        return data;
    }
    public void setArchivedAt(LocalDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }
    public LocalDateTime getArchivedAt() {
        return archivedAt;
    }
}

