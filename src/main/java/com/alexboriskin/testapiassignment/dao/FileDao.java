package com.alexboriskin.testapiassignment.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.alexboriskin.testapiassignment.models.File;

public interface FileDao {

    public void save(File file);

    public File get(long id);
    
    public void update(File file);
    
    public void delete(long id);

    public List<File> getAll();

    public EntityManager getEntityManager();

    public void setEntityManager(EntityManager entityManager);

}
