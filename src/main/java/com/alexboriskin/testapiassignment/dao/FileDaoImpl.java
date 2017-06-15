package com.alexboriskin.testapiassignment.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.alexboriskin.testapiassignment.models.File;

@Repository
public class FileDaoImpl implements FileDao {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<File> getAll() {
    String hql = "FROM File as files ORDER BY files.fileId";
    return (List<File>) entityManager.createQuery(hql).getResultList();
    }

    @Override
    public void save(File file) {
        entityManager.persist(file);
    }

    @Override
    public File get(long id) {
        File file = entityManager.find(File.class, id);
        return file;
    }

    @Override
    public void update(File file) {
        System.out.println(file.getFileName());

        entityManager.merge(file);
    }

    @Override
    public void delete(long id) {
        File file = get(id);
        if (file != null) {
            entityManager.remove(file);
        }
    }

}
