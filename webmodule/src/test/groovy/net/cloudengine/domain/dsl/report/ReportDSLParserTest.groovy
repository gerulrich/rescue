package net.cloudengine.domain.dsl.report;

import static org.junit.Assert.*
import net.cloudengine.reports.ReportMetadata
import net.cloudengine.reports.ReportMetadataDSL

import org.junit.Assert
import org.junit.Test

class ReportDSLParserTest {
	
	String manifest = """\
		report {
			name "Reporte por zonas"
			parameters {
				"NAME" {
					label "Nombre"
					required true
					type String.class
				}
			}
		}
	""";
	
	
	@Test
	void test_parseDSL() {
		ReportMetadataDSL parser = new ReportMetadataDSL();
		ReportMetadata metadata = parser.parseDSL(manifest, "manifest.groovy");
		Assert.assertEquals(1, metadata.getParameters().size())
		
	}
}
