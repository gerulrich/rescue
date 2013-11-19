package net.cloudengine.cobertura;

import net.cloudengine.util.TestCoverageUtil;
import net.cloudengine.validation.Assert;

import org.junit.Test;

/**
 * Este test (que en realidad no es un test), se encarga
 * de corregir la cobertura de las clases utilitarias que solo
 * contienen metodos estaticos (y tienen un contructor privado).
 * Esta clase se encarga de ejecutar dichos constructores para
 * generar una cobertura del 100% de las clases. 
 * @author German Ulrich
 *
 */
public class FixCoberturaCoverageForUtilitiesTest {

	@Test
	public void fixAssertCoberturaCoverage() {
		TestCoverageUtil.reflectiveInvokePrivateConstructor(Assert.class);
	}

}
