package net.cloudengine.workflow;

public enum Workflow {
	
	ABIERTO {
		public Workflow[] canGo() {
			return new Workflow[]{DERIVADO};
		}
	},
	DERIVADO {
		public Workflow[] canGo() {
			return new Workflow[]{DERIVADO, CERRADO};
		}
	},
	CERRADO {
		public Workflow[] canGo() {
			return new Workflow[] {REABIERTO};
		}
	},
	REABIERTO {
		public Workflow[] canGo() {
			return new Workflow[] {CERRADO, DERIVADO};
		}
	};
	
	public abstract Workflow[] canGo();

}
