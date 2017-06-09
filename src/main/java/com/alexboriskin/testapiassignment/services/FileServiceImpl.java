package com.alexboriskin.testapiassignment.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alexboriskin.testapiassignment.dao.FileDao;
import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.models.MetaData;

@Service
public class FileServiceImpl implements FileService {
    
    @Autowired
    private FileDao fileDao;

    List<File> database = new ArrayList<>();

    {
        File file1 = new File("file1.properties", new Date(), null);
        File file2 = new File("file2.properties", new Date(), null);
        File file3 = new File("file3.properties", new Date(), null);

        MetaData metaData1 = new MetaData(file1, "metaData11",
                "metaData22", "metaData33");
        file1.setMetaData(metaData1);
        MetaData metaData2 = new MetaData(file2, "metaData21",
                "metaData22", "metaData23");
        file2.setMetaData(metaData2);
        MetaData metaData3 = new MetaData(file3, "metaData31",
                "metaData32", "metaData33");
        file3.setMetaData(metaData3);

        database.add(file1);
        database.add(file2);
        database.add(file3);
    }

    @Override
    @Transactional
    public void saveNew(File file) {
        fileDao.save(database.get(0));
        fileDao.save(database.get(1));
        fileDao.save(database.get(2));
    }

    @Override
    public List<File> getAll() {
        return fileDao.getAll();
    }

    @Override
    public File getById(long id) {
        return fileDao.get(id);
    }

    @Override
    public File getByName(String name) {
        return database.stream()
                .filter(t -> t.getFileName().equals(name))
                .findAny()
                .orElse(null);
    }

    @Override
    public void update(File file) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteById(long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteByName(String name) {
        // TODO Auto-generated method stub

    }

}
