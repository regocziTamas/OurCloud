package com.thomaster.ourcloud.model.user;


import javax.persistence.*;
import java.util.Objects;

@Entity
public class OCUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long usedBytes;
    private Role role;
    private String username;
    private String password;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

//    public Set<FileSystemElement> getFiles() {
//        return files;
//    }
//
//    public void setFiles(Set<FileSystemElement> files) {
//        this.files = files;
//    }
//
//    public void addFile(FileSystemElement file) {
//        files.add(file);
//    }


    public Long getUsedBytes() {
        return usedBytes;
    }

    public void setUsedBytes(Long usedBytes) {
        this.usedBytes = usedBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OCUser ocUser = (OCUser) o;
        return id.equals(ocUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
