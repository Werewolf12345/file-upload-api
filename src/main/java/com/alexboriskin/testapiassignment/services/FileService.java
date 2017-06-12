package com.alexboriskin.testapiassignment.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.alexboriskin.testapiassignment.models.File;

public interface FileService {

    public void saveNew(File file);

    public List<File> getAll();

    public File getById(long id);

    public File getByName(String name);

    public void update(File file);

    public void deleteById(long id);

    public void deleteByName(String name);

    public File processUploadedFile(MultipartFile file);

}
