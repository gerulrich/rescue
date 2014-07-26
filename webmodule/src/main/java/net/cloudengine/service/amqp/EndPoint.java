package net.cloudengine.service.amqp;

public enum EndPoint {

	REPORT_EXECUTION {
		public String getRountingKey() {
			return null;
		}
	},
	TICKET_UPDATE {
		public String getRountingKey() {
			return null;
		}		
	},
	REPORT_UPLOAD {
		public String getRountingKey() {
			return "file.report";
		}
	},
	WORKFLOW_UPLOAD {
		public String getRountingKey() {
			return "file.workflow";
		}
	};
	
	public abstract String getRountingKey();
	
}
