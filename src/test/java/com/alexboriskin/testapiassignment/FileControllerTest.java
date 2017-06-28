package com.alexboriskin.testapiassignment;

import com.alexboriskin.testapiassignment.controllers.FileController;
import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.models.MetaData;
import com.alexboriskin.testapiassignment.services.FileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(FileController.class)
public class FileControllerTest {
    
    private final long EXISTING_ID = 1L;
    private final long NON_EXISTING_ID = 2L;

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
        
        List<File> files = new ArrayList<>(Arrays.asList(file1, file2, file3));
        for(int i = 1; i < 4; i++) {
            files.get(i - 1).setFileId(i);
            files.get(i - 1).getMetaData().setId(i);
            files.get(i - 1).getUploaded().setTime(1L);
        }
                        
        given(fileService.getByName("filename")).willReturn(files);
        mvc.perform(get("/files?fileName=filename").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
                        .json("[{'fileId':1,'fileName':'file1.properties','uploaded':1,'metaData':"
                                + "{'id':1,'metaData1':'metaData11','metaData2':'metaData12','metaData3':"
                                + "'metaData13'},'links':[{'rel':'self','href':'http://localhost/files/1'}]},"
                                + "{'fileId':2,'fileName':'file2.properties','uploaded':1,'metaData':"
                                + "{'id':2,'metaData1':'metaData21','metaData2':'metaData22','metaData3':"
                                + "'metaData23'},'links':[{'rel':'self','href':'http://localhost/files/2'}]},"
                                + "{'fileId':3,'fileName':'file3.properties','uploaded':1,'metaData':"
                                + "{'id':3,'metaData1':'metaData31','metaData2':'metaData32','metaData3':"
                                + "'metaData33'},'links':[{'rel':'self','href':'http://localhost/files/3'}]}]"));
        
        files.clear();
        mvc.perform(get("/files").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string(""));
       
    }

    @Test
    public void testGetFile() throws Exception {
        
        file1.setFileId(EXISTING_ID);
        file1.getMetaData().setId(EXISTING_ID);
        file1.getUploaded().setTime(1L);

        given(fileService.getById(EXISTING_ID)).willReturn(file1);
        mvc.perform(get("/files/{id}", EXISTING_ID).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
                         .json("{'fileId':1,'fileName':'file1.properties'," 
                             + "'uploaded':1,"
                             + "'metaData':{'id':1,'metaData1':'metaData11','metaData2':'metaData12',"
                             + "'metaData3':'metaData13'},"
                             + "'_links':{'self':{'href':'http://localhost/files/1'}}}"));
        
        given(fileService.getById(NON_EXISTING_ID)).willReturn(null);
        mvc.perform(get("/files/{id}", NON_EXISTING_ID).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string(""));
        
        verify(fileService, times(2)).getById(anyLong());
        verifyNoMoreInteractions(fileService);
    }

    @Test
    public void testUpdateFile() throws Exception {
        
        file1.setFileId(EXISTING_ID);
        file1.getMetaData().setId(EXISTING_ID);
        file1.getUploaded().setTime(1L);
       
        doNothing().when(fileService).update(file1);
        
        mvc.perform(
                put("/files/{id}", EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(file1)))
                .andExpect(status().isOk());
        
        verify(fileService, times(1)).update(any(File.class));
        verifyNoMoreInteractions(fileService);

    }

    @Test
    public void testUploadFile() throws Exception {
      
        mvc.perform(
                get("/files/upload"))
                .andExpect(status().isOk());
        
    }

    @Test
    public void testHandleFileUpload() throws Exception {
        
        file1.setFileId(EXISTING_ID);
        file1.getMetaData().setId(EXISTING_ID);
        file1.getUploaded().setTime(1L);
        
        MockMultipartFile mockFile = new MockMultipartFile("file", "file.name", "text/plain", "bytes".getBytes());
       
        given(fileService.processUploadedFile(any(MultipartFile.class))).willReturn(file1);
        
        mvc.perform(
                fileUpload("/files/upload").file(mockFile))
                   
                    .andExpect(status().is3xxRedirection())
                    .andExpect(model().attribute("id", "1"));
        
        verify(fileService, times(1)).processUploadedFile(any(MultipartFile.class));
        verifyNoMoreInteractions(fileService);
    }

    @Test
    public void testDeleteFile() throws Exception {
        
        file1.setFileId(EXISTING_ID);
        file1.getMetaData().setId(EXISTING_ID);
        file1.getUploaded().setTime(1L);
       
        given(fileService.getById(EXISTING_ID)).willReturn(file1);
        given(fileService.getById(NON_EXISTING_ID)).willReturn(null);
        doNothing().when(fileService).deleteById(anyLong());
        
        mvc.perform(
                delete("/files/{id}", EXISTING_ID))
                .andExpect(status().isNoContent());
        
        mvc.perform(
                delete("/files/{id}", NON_EXISTING_ID))
                .andExpect(status().isNotFound());
        
        verify(fileService, times(2)).getById(anyLong());
        verify(fileService, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(fileService);
        
    }
    
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
