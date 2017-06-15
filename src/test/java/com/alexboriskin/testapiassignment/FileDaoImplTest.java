package com.alexboriskin.testapiassignment;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.alexboriskin.testapiassignment.dao.FileDao;
import com.alexboriskin.testapiassignment.dao.FileDaoImpl;
import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.models.MetaData;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FileDaoImplTest {

    @Autowired
    private FileDao fileDao;

    private MetaData metaData1 = new MetaData("metaData11", "metaData12", "metaData13");
    private File file1 = new File("file1.properties", new Date(), metaData1);
    private MetaData metaData2 = new MetaData("metaData21", "metaData22", "metaData23");
    private File file2 = new File("file2.properties", new Date(), metaData2);
    private MetaData metaData3 = new MetaData("metaData31", "metaData32", "metaData33");
    private File file3 = new File("file3.properties", new Date(), metaData3);

    @TestConfiguration
    static class Config {
        @Bean
        public FileDao fileDao() {
            return new FileDaoImpl();
        }
    }

    @Test
    public void testGetAll() {
        List<File> resultList = fileDao.getAll();

        assertTrue(resultList.isEmpty());

        fileDao.save(file1);
        fileDao.save(file2);
        fileDao.save(file3);

        resultList = fileDao.getAll();

        assertTrue(resultList.containsAll(Arrays.asList(file1, file2, file3)));
    }

    @Test
    public void testSaveGetDelete() {
        long id = file1.getFileId();
        assertNull(fileDao.get(id));
        
        fileDao.save(file1);
        id = file1.getFileId();
        
        assertEquals(file1, fileDao.get(id));
        
        fileDao.delete(id);
        assertNull(fileDao.get(id));
    }

    @Test
    public void testUpdate() {
        fileDao.save(file1);
        long id = file1.getFileId();
        assertEquals(file1, fileDao.get(id));
        
        file1.getMetaData().setMetaData1("UpdatedMetaData1");
        file1.getMetaData().setMetaData2("UpdatedMetaData2");
        file1.getMetaData().setMetaData3("UpdatedMetaData3");
        file1.setFileName("updatedFileName");
        Date newDate = new Date();
        file1.setUploaded(newDate);
        
        fileDao.update(file1);
        
        File fileInDB = fileDao.get(id);
        assertNotNull(fileDao.get(id));
        
        System.out.println(fileInDB.getFileName());
        
        assertTrue(fileInDB.getMetaData().getMetaData1().equals("UpdatedMetaData1"));
        assertTrue(fileInDB.getMetaData().getMetaData2().equals("UpdatedMetaData2"));
        assertTrue(fileInDB.getMetaData().getMetaData3().equals("UpdatedMetaData3"));
        
        assertTrue(fileInDB.getFileName().equals("updatedFileName"));
        
        assertTrue(fileInDB.getUploaded().compareTo(newDate) == 0);
    }
}
