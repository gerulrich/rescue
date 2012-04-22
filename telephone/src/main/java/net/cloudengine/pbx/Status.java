package net.cloudengine.pbx;

public enum Status {
	
//    * <li>NOT_INUSE
//    * <li>BUSY
//    * <li>UNAVAILABLE
//    int NOT_INUSE = 0;
//	int INUSE = 1;
//	int BUSY = 1 << 1;
//	int UNAVAILABLE = 1 << 2;
//	int RINGING = 1 << 3;
	
	RINGING {
		protected int intValue() {
			return 1 << 3;
		}
	},
	
	INUSE_RINGING {
		protected int intValue() {
			return RINGING.intValue()+INUSE.intValue();
		}
	},
	
	INUSE {
		protected int intValue() {
			return 1;
		}
	},
	
	NOT_INUSE {
		protected int intValue() {
			return 0;
		}
	},
	
	BUSY {
		protected int intValue() {
			return 1 << 1;
		}
	},
	
	UNAVAILABLE {
		protected int intValue() {
			return 1 << 2;
		}
	};
	
	public static Status fromInt(int value) {
		Status status = null;
		for(Status s : Status.values()) {
			if (value == s.intValue()) {
				status = s;
				break;
			}
		}
		if (status == null) {
			throw new IllegalArgumentException("Estado incorrecto");
		}
		return status;
	}
	
	protected abstract int intValue();

}
