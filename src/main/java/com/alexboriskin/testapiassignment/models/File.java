package com.alexboriskin.testapiassignment.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement(name = "file")
@XmlType(propOrder = { "fileId", "fileName", "uploaded", "metaData" })
@JsonPropertyOrder({ "fileId", "fileName", "uploaded", "metaData"  })
@JsonInclude(value=Include.NON_EMPTY, content=Include.NON_NULL)
@Entity
@Table(name = "files")
public class File extends ResourceSupport {
   
    private long fileId;
    private String fileName;
    private Date uploaded;
    private MetaData metaData;
        
    public File() {
    }

    public File(String fileName, Date uploaded, MetaData metaData) {
        this.fileName = fileName;
        this.uploaded = uploaded;
        this.metaData = metaData;
    }

    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlElement
    public long getFileId() {
        return fileId;
    }

    public void setFileId(long id) {
        this.fileId = id;
    }

    @XmlElement
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @XmlElement
    public Date getUploaded() {
        return uploaded;
    }

    public void setUploaded(Date uploaded) {
        this.uploaded = uploaded;
    }

    @XmlElement
    @OneToOne(fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "metadata_id")
    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

}
