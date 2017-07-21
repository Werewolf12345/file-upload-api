package com.alexboriskin.testapiassignment.commands;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Alexey on 7/17/2017.
 */
public class FileForm {
    private long fileId;
    @NotEmpty
    private String fileName;

    @DateTimeFormat(pattern = "dd-MMM-yyyy")
    private Date uploaded;
    private long metadataId;
    @NotEmpty
    private String metaData1;
    @NotEmpty
    private String metaData2;
    @NotEmpty
    private String metaData3;

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getUploaded() {
        return uploaded;
    }

    public void setUploaded(Date uploaded) {
        this.uploaded = uploaded;
    }

    public long getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(long metadataId) {
        this.metadataId = metadataId;
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
