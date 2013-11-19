package net.cloudengine.domain.dsl.workflow;

import static org.junit.Assert.*

import net.cloudengine.domain.dsl.workflow.WorkflowDSLParser;
import net.cloudengine.domain.dsl.workflow.WorkflowMetadata;

import org.junit.Test

class WorkflowDSLParserTest {
	
	String workflow = """\
		workflow {
			version 1.0d
			start 0	
			states {
				'OPEN' {
					description 'Abierto'
				}
		
				'DERIVATED' {
					description 'Derivado'
				}
		
				'IN_PROGRESS' {
					description 'En progreso'
				}
		
				'WAIT_CLOSED' {
					description 'Espera de cierre'
				}
		
				'CLOSED' {
					description 'Cerrado'
				}
		
				'REOPENED' {
					description 'Reabierto'
				}
			}
	
			transitions {
				'INIT_PROGRESS' {
					description 'En progreso'
					from 'OPEN'
					to 'IN_PROGRESS'
				}

				'DERIVATE' {
					description 'Derivar'
					from 'OPEN'
					to 'DERIVATED'
				}
		
				'CLOSE' {
					description 'Cerrar'
					from 'WAIT_CLOSED'
					to 'CLOSED'
					allowTo 'SUPERVISOR'
				}
		
				'REOPEN' {
					description 'Reabrir'
					from 'CLOSED'
					to 'REOPENED'
					allowTo 'SUPERVISOR' | 'ADMIN'
				}
			}
		}	""";
	
	
	@Test
	void test_parseDSL() {
		WorkflowDSLParser parser = new WorkflowDSLParser();
		WorkflowMetadata metadata = parser.parseDSL(workflow, "workflow");
		assertEquals(6, metadata.getStates().size());
		assertEquals(4, metadata.getTransitions().size());
	}
}
