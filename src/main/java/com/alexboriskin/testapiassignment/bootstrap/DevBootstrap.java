package com.alexboriskin.testapiassignment.bootstrap;

import com.alexboriskin.testapiassignment.dao.FileRepository;
import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.models.MetaData;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private FileRepository fileRepository;

    public DevBootstrap(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        File file1 = new File("file1.properties", new Date(), null);
        MetaData metaData1 = new MetaData("metaData11", "metaData12", "metaData13");
        file1.setMetaData(metaData1);
        fileRepository.save(file1);

        File file2 = new File("file2.properties", new Date(), null);
        MetaData metaData2 = new MetaData("metaData21", "metaData22", "metaData23");
        file2.setMetaData(metaData2);
        fileRepository.save(file2);

        File file3 = new File("file3.properties", new Date(), null);
        MetaData metaData3 = new MetaData("metaData31", "metaData32", "metaData33");
        file3.setMetaData(metaData3);
        fileRepository.save(file3);
    }
}
