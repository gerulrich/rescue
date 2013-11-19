package net.cloudengine.util;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Writer;

import net.cloudengine.service.web.WebPageTemplateExceptionHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import freemarker.template.TemplateException;

public class WebPageTemplateExceptionHandlerTest {

	private TemplateException exception;
	private Writer out;
	
	@Before
	public void setUp() {
		out = mock(Writer.class);
		exception = mock(TemplateException.class);
	}
	
	@Test
	public void test_handleTemplateException_normally() throws IOException {
		when(exception.getMessage())
			.thenReturn("exception message");

		WebPageTemplateExceptionHandler handler = new WebPageTemplateExceptionHandler();
		handler.handleTemplateException(exception, null, out);
		
		verify(out, times(1)).write(anyString());
		Mockito.verifyNoMoreInteractions(out);
		verify(exception).getMessage();
		Mockito.verifyNoMoreInteractions(exception);
	}
	
	@Test(expected=IOException.class)
	public void test_handleTemplateException_withException() throws IOException {
		Mockito.doThrow(new IOException()).when(out).write(anyString());
		WebPageTemplateExceptionHandler handler = new WebPageTemplateExceptionHandler();
		handler.handleTemplateException(exception, null, out);
	}

}
