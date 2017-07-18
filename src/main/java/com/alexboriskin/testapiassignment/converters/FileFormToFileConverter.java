package com.alexboriskin.testapiassignment.converters;

import com.alexboriskin.testapiassignment.commands.FileForm;
import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.models.MetaData;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by Alexey on 7/17/2017.
 */
@Component
public class FileFormToFileConverter implements Converter<FileForm, File> {
    @Override
    public File convert(FileForm source) {
        File file = new File();
        file.setMetaData(new MetaData());

        file.setFileId(source.getFileId());
        file.getMetaData().setId(source.getMetadataId());
        file.setUploaded(source.getUploaded());
        file.setFileName(source.getFileName());
        file.getMetaData().setMetaData1(source.getMetaData1());
        file.getMetaData().setMetaData2(source.getMetaData2());
        file.getMetaData().setMetaData3(source.getMetaData3());

        return file;
    }
}
