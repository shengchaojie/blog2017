package com.scj.dal.ro;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public class BaseRO implements Serializable{

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
