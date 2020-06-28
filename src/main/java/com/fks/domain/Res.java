package com.fks.domain;

import java.io.Serializable;

public class Res implements Serializable {
    private Integer id;
    private String res_name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRes_name() {
        return res_name;
    }

    public void setRes_name(String res_name) {
        this.res_name = res_name;
    }
}
