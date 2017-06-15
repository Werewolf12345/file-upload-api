package com.alexboriskin.testapiassignment;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.alexboriskin.testapiassignment.controllers.FileController;
import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.models.MetaData;
import com.alexboriskin.testapiassignment.services.FileService;

@RunWith(SpringRunner.class)
@WebMvcTest(FileController.class)
public class FileControllerTest {

    private MetaData metaData1 = new MetaData("metaData11", "metaData12", "metaData13");
    private File file1 = new File("file1.properties", new Date(), metaData1);
    private MetaData metaData2 = new MetaData("metaData21", "metaData22", "metaData23");
    private File file2 = new File("file2.properties", new Date(), metaData2);
    private MetaData metaData3 = new MetaData("metaData31", "metaData32", "metaData33");
    private File file3 = new File("file3.properties", new Date(), metaData3);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileService fileService;

    @Test
    public void testGetAllFiles() throws Exception {
        
        List<File> files = Arrays.asList(file1, file2, file3);
        for(int i = 1; i < 4; i++) {
            files.get(i - 1).setFileId(i);
            files.get(i - 1).getMetaData().setId(i);
            files.get(i - 1).getUploaded().setTime(1L);
        }
        
        given(fileService.getAll()).willReturn(files);
        mvc.perform(get("/files").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
        
    }

    @Test
    public void testGetFile() throws Exception {
        
        final long EXISTING_ID = 1;
        final long NON_EXISTING_ID = 2;
        
        file1.setFileId(EXISTING_ID);
        file1.getMetaData().setId(EXISTING_ID);
        file1.getUploaded().setTime(1L);

        given(fileService.getById(EXISTING_ID)).willReturn(file1);
        mvc.perform(get("/files/1").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
                         .json("{'fileId':1,'fileName':'file1.properties'," 
                             + "'uploaded':1,"
                             + "'metaData':{'id':1,'metaData1':'metaData11','metaData2':'metaData12',"
                             + "'metaData3':'metaData13'},"
                             + "'_links':{'self':{'href':'http://localhost/files/1'}}}"));
        
        given(fileService.getById(NON_EXISTING_ID)).willReturn(null);
        mvc.perform(get("/files/2").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string(""));
    }

    @Test
    public void testUpdateFile() {
        
    }

    @Test
    public void testUploadFile() {
        
    }

    @Test
    public void testHandleFileUpload() {
        
    }

    @Test
    public void testDeleteFile() {
        
    }

}
