package com.alexboriskin.testapiassignment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ RestControllerTest.class, HtmlControllerTest.class, FileServiceImplTest.class })
public class AllTests {

}
