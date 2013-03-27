package net.cloudengine.web.statistics;

public enum TimeUnits {
	
	PER_SECOND {
		public Long getTimeRepresentation() {
			return 1L;
		}
		public int getHoursRange() {
			return 1;
		}
	},
	
	PER_MINUTE {
		public Long getTimeRepresentation() {
			return 60L;
		}
		public int getHoursRange() {
			return 24;
		}
	},
	
	PER_HOUR {
		public Long getTimeRepresentation() {
			return 3600L;
		}
		public int getHoursRange() {
			return 24*7;
		}
	},
	
	DAILY {
		public Long getTimeRepresentation() {
			return 60*60*24L;
		}
		
		public int getHoursRange() {
			return 24*7;
		}
		
	};
	
	public abstract Long getTimeRepresentation();
	public abstract int getHoursRange();

}
