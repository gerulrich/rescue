package net.cloudengine.util;

import java.io.IOException;

import org.junit.Test;

public class UncheckedThrowTest {
	
	static {
		TestCoverageUtil.reflectiveInvokePrivateConstructor(UncheckedThrow.class);
	}
	
	@Test(expected=IOException.class)
	public void testConvertCheckedExceptionIntoUnchecked() {
		try {
			throw new IOException("Checked Exception");
		} catch (IOException e) {
			UncheckedThrow.throwUnchecked(e);
		}
	}
	
	@Test
	public void testThrowNullException() {
		UncheckedThrow.throwUnchecked(null);
	}

}
