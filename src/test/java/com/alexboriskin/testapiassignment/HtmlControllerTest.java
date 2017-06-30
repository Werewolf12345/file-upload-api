package com.alexboriskin.testapiassignment;

import com.alexboriskin.testapiassignment.controllers.HtmlController;
import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.models.MetaData;
import com.alexboriskin.testapiassignment.services.FileService;
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

import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(HtmlController.class)
public class HtmlControllerTest {
    
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
    public void testUpdateFile() throws Exception {
        
        file1.setFileId(EXISTING_ID);
        file1.getMetaData().setId(EXISTING_ID);
        file1.getUploaded().setTime(1L);
       
        doNothing().when(fileService).update(file1);
        given(fileService.getById(EXISTING_ID)).willReturn(file1);

        mvc.perform(
                get("/files/{id}/update", EXISTING_ID).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(fileService, times(1)).getById(anyLong());
        verifyNoMoreInteractions(fileService);
    }

    @Test
    public void testUploadFile() throws Exception {
      
        mvc.perform(
                get("/files/upload").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
        
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

}
