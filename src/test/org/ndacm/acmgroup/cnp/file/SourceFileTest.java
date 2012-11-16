/**
 * 
 */
package org.ndacm.acmgroup.cnp.file;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ndacm.acmgroup.cnp.file.SourceFile.SourceType;

public class SourceFileTest {
	
	private static SourceFile file1;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		file1 = new ServerSourceFile(10, "sourceFile1", SourceType.JAVA, "InitialText_1");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.ndacm.acmgroup.cnp.file.SourceFile#toString()}.
	 */
	@Test
	public void testToString() {
		assertEquals("InitialText_1", file1.toString());
	}

}
