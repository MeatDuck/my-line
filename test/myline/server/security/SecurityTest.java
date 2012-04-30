package myline.server.security;

import static org.junit.Assert.*;

import org.junit.Test;

public class SecurityTest {

	@Test
	public void testIsValid() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateMD5() {
		final String createMD5 = Security.createMD5(SecurityConstants.API_ID + "_" + "36563363" + "_" + SecurityConstants.API_SECRET);
		assertTrue(createMD5.equals("0ac613e1f398423c97ec6ac4e87a5736"));
	}

}
