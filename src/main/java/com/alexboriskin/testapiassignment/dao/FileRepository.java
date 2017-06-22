package com.alexboriskin.testapiassignment.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.alexboriskin.testapiassignment.models.File;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {
    
    List<File> findByFileName(String fileName);

}
