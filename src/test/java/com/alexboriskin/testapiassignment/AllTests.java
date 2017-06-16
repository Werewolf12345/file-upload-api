package com.alexboriskin.testapiassignment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FileControllerTest.class, 
                FileDaoImplTest.class, 
                FileServiceImplTest.class, 
                MockCreationTest.class,
                TestapiassignmentApplicationTests.class })
public class AllTests {

}
