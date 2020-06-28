package com.fks.domain;

import java.io.Serializable;
import java.util.List;

public class Role implements Serializable {
    private Integer id;
    private String role_name;

    private List<Res> reses;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public List<Res> getReses() {
        return reses;
    }

    public void setReses(List<Res> reses) {
        this.reses = reses;
    }
}
