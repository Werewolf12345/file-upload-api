package com.alexboriskin.testapiassignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.alexboriskin.testapiassignment.dao.FileDao;
import com.alexboriskin.testapiassignment.models.File;
import com.alexboriskin.testapiassignment.models.MetaData;

public class MockCreationTest {

    @Mock
    private FileDao fileDao;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMockCreation() {
        assertNotNull(fileDao);
    }

    @Test
    public void testGet() {

        File file1 = new File("file1.properties", new Date(), null);
        File file2 = new File("file2.properties", new Date(), null);
        File file3 = new File("file3.properties", new Date(), null);

        MetaData metaData1 = new MetaData("metaData11", "metaData22", "metaData33");
        file1.setMetaData(metaData1);
        MetaData metaData2 = new MetaData("metaData21", "metaData22", "metaData23");
        file2.setMetaData(metaData2);
        MetaData metaData3 = new MetaData("metaData31", "metaData32", "metaData33");
        file3.setMetaData(metaData3);

        when(fileDao.get(1)).thenReturn(file1);
        assertEquals(file1, fileDao.get(1));
    }
}