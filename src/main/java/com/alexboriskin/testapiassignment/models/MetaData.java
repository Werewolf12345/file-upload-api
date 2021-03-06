package com.alexboriskin.testapiassignment.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "metadata")
public class MetaData implements Serializable {
    
    private static final long serialVersionUID = -5047974920599026173L;
    private long id;
    private String metaData1;
    private String metaData2;
    private String metaData3;
    
    public MetaData() {
    }

    public MetaData(String metaData1, String metaData2,
            String metaData3) {
        this.metaData1 = metaData1;
        this.metaData2 = metaData2;
        this.metaData3 = metaData3;
    }

    @Id
    @Column(name = "metadata_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMetaData1() {
        return metaData1;
    }

    public void setMetaData1(String metaData1) {
        this.metaData1 = metaData1;
    }

    public String getMetaData2() {
        return metaData2;
    }

    public void setMetaData2(String metaData2) {
        this.metaData2 = metaData2;
    }

    public String getMetaData3() {
        return metaData3;
    }

    public void setMetaData3(String metaData3) {
        this.metaData3 = metaData3;
    }

}
