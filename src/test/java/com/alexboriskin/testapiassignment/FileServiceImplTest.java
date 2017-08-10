package com.alexboriskin.testapiassignment;

import com.alexboriskin.testapiassignment.commands.FileForm;
import com.alexboriskin.testapiassignment.converters.FileFormToFileConverter;
import com.alexboriskin.testapiassignment.dao.FileRepository;
import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.models.MetaData;
import com.alexboriskin.testapiassignment.services.FileService;
import com.alexboriskin.testapiassignment.services.FileServiceImpl;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceImplTest {

    @Mock
    private FileRepository fileRepository;
    @Mock
    private FileFormToFileConverter fileFormToFileConverter;

    @InjectMocks
    private FileService fileService = new FileServiceImpl(fileRepository, fileFormToFileConverter);
    
    private File file1 = new File("file1.properties", new Date(), null);
    private MetaData metaData1 = new MetaData("metaData11", "metaData12", "metaData13");
    private File file2 = new File("file2.properties", new Date(), null);
    private MetaData metaData2 = new MetaData("metaData21", "metaData22", "metaData23");
    private File file3 = new File("file3.properties", new Date(), null);
    private MetaData metaData3 = new MetaData("metaData31", "metaData32", "metaData33");
    private FileForm fileForm = new FileForm();

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
        
        file1.setMetaData(metaData1);
        file1.setFileId(1L);
        file2.setMetaData(metaData2);
        file2.setFileId(2L);
        file3.setMetaData(metaData3);
        file3.setFileId(3L);

        fileForm.setUploaded(new Date());
        fileForm.setFileId(1L);
        fileForm.setFileName("file1.properties");
        fileForm.setMetaData1("metaData11");
        fileForm.setMetaData2("metaData12");
        fileForm.setMetaData3("metaData13");

    }

    @Test
    public void testSaveNewFile() {
        fileService.saveNew(file1);
        verify(fileRepository).save(file1);
    }

    @Test
    public void testSaveNewFileForm() {
        when(fileFormToFileConverter.convert(fileForm)).thenReturn(file1);
        fileService.saveNew(fileForm);
        verify(fileRepository).save(file1);
    }

    @Test
    public void testGetAll() {
        List<File> database = Arrays.asList(file1, file2, file3);
        when(fileRepository.findAll()).thenReturn(database);
        fileService.getAll();
        verify(fileRepository).findAll();
    }

    @Test
    public void testGetById() {
        when(fileRepository.findOne(1L)).thenReturn(file1);
        assertEquals(file1, fileService.getById(1L));

    }

    @Test
    public void testGetByName() {
        List<File> database = Arrays.asList(file1, file2, file3);
        when(fileRepository.findByFileName("file1.properties")).thenReturn(database);

        List<File> files = fileService.getByName("file1.properties");
        assertNotNull(files);
        assertTrue(files.containsAll(database));
        

        files = fileService.getByName("nonExistingName");
        assertTrue(files.isEmpty());
    }

    @Test
    public void testUpdate() {
        fileService.update(file1);
        verify(fileRepository).save(file1);
    }

    @Test
    public void testDeleteById() {
        fileService.deleteById(1L);
        verify(fileRepository).delete(1L);
    }

    @Test
    public void testDeleteByNameExistingFile() {
        List<File> database = Arrays.asList(file1, file2, file3);
        when(fileRepository.findAll()).thenReturn(database);

        fileService.deleteByName("file1.properties");
        verify(fileRepository).delete(1L);

    }

    @Test
    public void testDeleteByNameNonExistingFile() {
        List<File> database = new ArrayList<>(Arrays.asList(file1, file2, file3));
        when(fileRepository.findAll()).thenReturn(database);

        fileService.deleteByName("nonExistingName");
        verify(fileRepository, never()).delete(anyLong());
    }

    @Test
    public void testProcessUploadedFile() {

        try {
            InputStream stubInputStream = IOUtils.toInputStream("metaData1=test1\nmetaData2=test2\nmetaData3=test3\n",
                    "UTF-8");
            MultipartFile multipartFile = mock(MultipartFile.class);

            when(multipartFile.getInputStream()).thenReturn(stubInputStream);
            File file = fileService.processUploadedFile(multipartFile);
            assertEquals("test1", file.getMetaData().getMetaData1());
            assertEquals("test2", file.getMetaData().getMetaData2());
            assertEquals("test3", file.getMetaData().getMetaData3());

        } catch (IOException e) {
            e.printStackTrace();
            fail("Cannot create stub Input Stream");
        }

    }

}
