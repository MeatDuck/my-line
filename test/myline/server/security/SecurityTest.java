package myline.server.security;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import myline.shared.exceptions.ServiceException;
import myline.shared.security.Access;

import org.junit.Test;

public class SecurityTest {
    private static final String SIGN = "6af9d40bfdcacd430ebc58a4952ed782";
    private static final String UID = "69066977";
    Security security = new Security();

    @Test(expected = ServiceException.class)
    public void testIsValidBadVkontakteCreditails() throws ServiceException {
        Access acc = null;
        assertEquals(security.isValid(acc), true);
    }

    @Test(expected = ServiceException.class)
    public void testIsValidBadVkontakteCreditails2() throws ServiceException {
        Access acc = mock(Access.class);
        assertEquals(security.isValid(acc), true);
    }

    @Test
    public void testIsValidNoPassUser() throws ServiceException {
        Access acc = mock(Access.class);
        when(acc.getUid()).thenReturn(SecurityConstants.NONPAS_USER);
        when(acc.getSign()).thenReturn(SecurityConstants.NONPAS_SECRET_HASH);
        assertEquals(security.isValid(acc), true);
    }

    @Test
    public void testIsValidPassUser() throws ServiceException {
        Access acc = mock(Access.class);
        when(acc.getUid()).thenReturn(UID);
        when(acc.getSign()).thenReturn(SIGN);
        assertEquals(security.isValid(acc), true);
    }

    @Test
    public void testCreateMD5() {

        final String createMD5 = security.createMD5(SecurityConstants.API_ID + "_" + "36563363"
                + "_" + SecurityConstants.API_SECRET);
        assertTrue(createMD5.equals("0ac613e1f398423c97ec6ac4e87a5736"));
    }

}
