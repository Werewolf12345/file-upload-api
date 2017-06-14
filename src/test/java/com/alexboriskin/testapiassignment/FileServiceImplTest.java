package com.alexboriskin.testapiassignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import com.alexboriskin.testapiassignment.dao.FileDao;
import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.models.MetaData;
import com.alexboriskin.testapiassignment.services.FileServiceImpl;

public class FileServiceImplTest {

    @Mock
    private FileDao fileDao;

    private FileServiceImpl fileServiceImpl = new FileServiceImpl();
    private File file1 = new File("file1.properties", new Date(), null);
    private MetaData metaData1 = new MetaData("metaData11", "metaData12", "metaData13");
    private File file2 = new File("file2.properties", new Date(), null);
    private MetaData metaData2 = new MetaData("metaData21", "metaData22", "metaData23");
    private File file3 = new File("file3.properties", new Date(), null);
    private MetaData metaData3 = new MetaData("metaData31", "metaData32", "metaData33");

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
        fileServiceImpl.setFileDao(fileDao);
        file1.setMetaData(metaData1);
        file1.setFileId(1);
        file2.setMetaData(metaData2);
        file2.setFileId(2);
        file3.setMetaData(metaData3);
        file3.setFileId(3);
    }

    @Test
    public void testSaveNew() {
        fileServiceImpl.saveNew(file1);
        verify(fileDao).save(file1);
    }

    @Test
    public void testGetAll() {
        fileServiceImpl.getAll();
        verify(fileDao).getAll();
    }

    @Test
    public void testGetById() {
        when(fileDao.get(1)).thenReturn(file1);
        assertEquals(file1, fileServiceImpl.getById(1));

    }

    @Test
    public void testGetByName() {
        List<File> database = Arrays.asList(file1, file2, file3);
        when(fileDao.getAll()).thenReturn(database);

        File file = fileServiceImpl.getByName("file1.properties");
        assertNotNull(file);
        assertEquals(file1.getFileName(), file.getFileName());
        assertEquals(file1.getId(), file.getId());

        file = fileServiceImpl.getByName("nonExistingName");
        assertNull(file);
    }

    @Test
    public void testUpdate() {
        fileServiceImpl.update(file1);
        verify(fileDao).update(file1);
    }

    @Test
    public void testDeleteById() {
        fileServiceImpl.deleteById(1);
        verify(fileDao).delete(1);
    }

    @Test
    public void testDeleteByNameExistingFile() {
        List<File> database = Arrays.asList(file1, file2, file3);
        when(fileDao.getAll()).thenReturn(database);

        fileServiceImpl.deleteByName("file1.properties");
        verify(fileDao).delete(1);

    }

    @Test
    public void testDeleteByNameNonExistingFile() {
        List<File> database = Arrays.asList(file1, file2, file3);
        when(fileDao.getAll()).thenReturn(database);

        fileServiceImpl.deleteByName("nonExistingName");
        verify(fileDao, never()).delete(anyLong());
    }

    @Test
    public void testProcessUploadedFile() {

        try {
            InputStream stubInputStream = IOUtils.toInputStream("metaData1=test1\nmetaData2=test2\nmetaData3=test3\n",
                    "UTF-8");
            MultipartFile multipartFile = mock(MultipartFile.class);

            when(multipartFile.getInputStream()).thenReturn(stubInputStream);
            File file = fileServiceImpl.processUploadedFile(multipartFile);
            assertEquals("test1", file.getMetaData().getMetaData1());
            assertEquals("test2", file.getMetaData().getMetaData2());
            assertEquals("test3", file.getMetaData().getMetaData3());

        } catch (IOException e) {
            e.printStackTrace();
            fail("Cannot create stub Input Stream");
        }

    }

}
