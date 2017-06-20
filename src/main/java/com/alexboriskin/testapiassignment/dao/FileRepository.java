package com.alexboriskin.testapiassignment.dao;

import org.springframework.data.repository.CrudRepository;

import com.alexboriskin.testapiassignment.models.File;

public interface FileRepository extends CrudRepository<File, Long> {

}
