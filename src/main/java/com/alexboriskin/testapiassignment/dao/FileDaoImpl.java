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
    
    @SuppressWarnings("unchecked")
    @Override
    public List<File> getAll() {
    String hql = "FROM File as files ORDER BY files.id";
    return (List<File>) entityManager.createQuery(hql).getResultList();
    }

    @Override
    public void save(File file) {
        entityManager.persist(file);
    }

    @Override
    public File get(long id) {
        return entityManager.find(File.class, id);
    }

    @Override
    public void update(File file) {
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
