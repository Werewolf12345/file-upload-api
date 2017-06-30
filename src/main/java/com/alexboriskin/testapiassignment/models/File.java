package com.alexboriskin.testapiassignment.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

@XmlRootElement(name = "file")
@XmlType(propOrder = { "fileId", "fileName", "uploaded", "metaData" })
@JsonPropertyOrder({ "fileId", "fileName", "uploaded", "metaData"  })
@JsonInclude(value=Include.NON_EMPTY, content=Include.NON_NULL)
@Entity
@Table(name = "files")
public class File extends ResourceSupport {
   
    private long fileId;
    private String fileName;
    @DateTimeFormat(pattern = "dd-MMM-yyyy")
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
