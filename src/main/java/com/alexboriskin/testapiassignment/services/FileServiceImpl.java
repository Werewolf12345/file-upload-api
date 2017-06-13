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

    public FileDao getFileDao() {
        return fileDao;
    }

    public void setFileDao(FileDao fileDao) {
        this.fileDao = fileDao;
    }

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

        return database.stream().filter(t -> t.getFileName().equals(name)).findAny().orElse(null);
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

        File fileToDelete = database.stream().filter(t -> t.getFileName().equals(name)).findAny().orElse(null);
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

            uploadedFile = new File(file.getOriginalFilename(), new Date(), null);
            MetaData metaData = new MetaData(metaData1, metaData2, metaData3);
            uploadedFile.setMetaData(metaData);

            saveNew(uploadedFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadedFile;

    }

}
