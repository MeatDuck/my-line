package myline.shared;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UtilsTest extends Utils {
	private static final String DOT = ".";
	private static final String NEW_LINE = "\n";
	private static final String DECLARING_CLASS = "DECLARING_CLASS";
	private static final String METHOD_NAME = "METHOD_NAME";
	private static final String FILE_NAME = "FILE_NAME";
	private static final int LINE_NUM = 1;
	private static final String ERROR_MESSAGE = "ERROR_MESSAGE";
	private Utils util = new Utils();
	
	@Mock Exception aThrowable;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetCustomStackTraceNoStackTrace() {
		when(aThrowable.toString()).thenReturn(ERROR_MESSAGE);
		
		assertEquals(ERROR_MESSAGE + NEW_LINE, util.getCustomStackTrace(aThrowable));
	}
	
	@Test
	public void testGetCustomStackTrace() {
		when(aThrowable.toString()).thenReturn(ERROR_MESSAGE);
		
		
		StackTraceElement element = new StackTraceElement(DECLARING_CLASS, METHOD_NAME, FILE_NAME, LINE_NUM);
		StackTraceElement[] arrayElems = new StackTraceElement[1];
		arrayElems[0] = element;
		when(aThrowable.getStackTrace()).thenReturn(arrayElems);
		
		assertEquals(ERROR_MESSAGE + NEW_LINE + DECLARING_CLASS + DOT + METHOD_NAME + "(" + FILE_NAME + ":" + LINE_NUM +  ")" + NEW_LINE  , util.getCustomStackTrace(aThrowable));
	}
}
