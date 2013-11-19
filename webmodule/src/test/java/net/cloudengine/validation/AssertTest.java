package net.cloudengine.validation;

import net.cloudengine.util.TestCoverageUtil;
import net.cloudengine.validation.Assert;

import org.junit.Test;

public class AssertTest {

	static {
		TestCoverageUtil.reflectiveInvokePrivateConstructor(Assert.class);
	}
	
	@Test
	public void testIsTrue_withTrueValue_nothingHappen() {
		Assert.isTrue(true, "valid condicion");		
	}
	
	@Test(expected=RuntimeException.class)
	public void testIsTrue_withFalseValue_throwRuntimeException() {
		Assert.isTrue(false, "invalid condicion");
	}

	@Test
	public void testNotNull_withNotNullValue_nothingHappen() {
		Assert.notNull(new Object(), "valid condition");
	}
	
	@Test(expected=RuntimeException.class)
	public void testNotNull_withNullValue_throwRuntimeException() {
		Assert.notNull(null, "valid condition");
	}

	@Test
	public void testNotEmpty_withNotEmptyString_nothingHappen() {
		Assert.notEmpty("not empty string", "valid condition");
	}
	
	@Test(expected=RuntimeException.class)
	public void testNotEmpty_withEmptyString_throwsRuntimeException() {
		Assert.notEmpty("  ", "valid condition");
	}
	
	@Test(expected=RuntimeException.class)
	public void testNotEmpty_withNullString_throwsRuntimeException() {
		Assert.notEmpty(null, "valid condition");
	}

}
