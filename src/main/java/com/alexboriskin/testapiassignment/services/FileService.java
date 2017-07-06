package com.alexboriskin.testapiassignment.services;

import com.alexboriskin.testapiassignment.models.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    void saveNew(File file);

    List<File> getAll();

    File getById(long id);

    List<File> getByName(String name);

    void update(File file);

    void deleteById(long id);

    void deleteByName(String name);

    File processUploadedFile(MultipartFile file);
   
}
