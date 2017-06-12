package com.alexboriskin.testapiassignment.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alexboriskin.testapiassignment.dao.FileDao;
import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.models.MetaData;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileDao fileDao;

    /*
     * { File file1 = new File("file1.properties", new Date(), null); File file2
     * = new File("file2.properties", new Date(), null); File file3 = new
     * File("file3.properties", new Date(), null);
     * 
     * MetaData metaData1 = new MetaData(file1, "metaData11", "metaData22",
     * "metaData33"); file1.setMetaData(metaData1); MetaData metaData2 = new
     * MetaData(file2, "metaData21", "metaData22", "metaData23");
     * file2.setMetaData(metaData2); MetaData metaData3 = new MetaData(file3,
     * "metaData31", "metaData32", "metaData33"); file3.setMetaData(metaData3);
     * 
     * database.add(file1); database.add(file2); database.add(file3); }
     */

    @Override
    @Transactional
    public void saveNew(File file) {
        fileDao.save(file);
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
        List<File> database = getAll();

        return database.stream().filter(t -> t.getFileName().equals(name))
                .findAny().orElse(null);
    }

    @Override
    @Transactional
    public void update(File file) {
        fileDao.update(file);

    }

    @Override
    @Transactional
    public void deleteById(long id) {
        fileDao.delete(id);

    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        List<File> database = getAll();

        File fileToDelete = database.stream()
                .filter(t -> t.getFileName().equals(name)).findAny()
                .orElse(null);
        if (fileToDelete != null) {
            fileDao.delete(fileToDelete.getId());
        }

    }

    @Override
    @Transactional
    public File processUploadedFile(MultipartFile file) {
        Properties properties = new Properties();
        File uploadedFile = null;

        try (InputStream input = file.getInputStream()) {
            properties.load(input);

            String metaData1 = properties.getProperty("metaData1");
            String metaData2 = properties.getProperty("metaData2");
            String metaData3 = properties.getProperty("metaData3");

            uploadedFile = new File(file.getOriginalFilename(), new Date(),
                    null);
            MetaData metaData = new MetaData(uploadedFile, metaData1,
                    metaData2, metaData3);
            uploadedFile.setMetaData(metaData);

            saveNew(uploadedFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadedFile;

    }

}
