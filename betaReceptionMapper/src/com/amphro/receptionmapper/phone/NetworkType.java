package com.amphro.receptionmapper.phone;

public enum NetworkType {
	NTUKN {
		@Override
		public int getNetwork() {
			return -1;
		}
	},
	NTNONE {
		@Override
		public int getNetwork() {
			return 0;
		}
	},
	NTEDGE {
		@Override
		public int getNetwork() {
			return 1;
		}
	},
	NT2G {
		@Override
		public int getNetwork() {
			return 3;
		}
	},
	NT3G {
		@Override
		public int getNetwork() {
			return 5;
		}
	},
	;
	public abstract int getNetwork();
}
