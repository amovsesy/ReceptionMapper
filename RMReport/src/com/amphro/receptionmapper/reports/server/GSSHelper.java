package com.amphro.receptionmapper.reports.server;

import com.amphro.receptionmapper.location.GSS;

public class GSSHelper {
	public static int toGSSx(int level, double lon) {
		int x = -1;
		switch(level) {
			case 1:
				x = GSS.toGSS1x(lon);
				break;
			case 2:
				x = GSS.toGSS2x(lon);
				break;
			case 3:
				x = GSS.toGSS3x(lon);
				break;
			case 4:
				x = GSS.toGSS4x(lon);
				break;
			case 5:
				x = GSS.toGSS5x(lon);
				break;
		}
		return x;
	}
	
	public static int toGSSy(int level, double lat) {
		int y = -1;
		switch(level) {
			case 1:
				y = GSS.toGSS1y(lat);
				break;
			case 2:
				y = GSS.toGSS2y(lat);
				break;
			case 3:
				y = GSS.toGSS3y(lat);
				break;
			case 4:
				y = GSS.toGSS4y(lat);
				break;
			case 5:
				y = GSS.toGSS5y(lat);
				break;
		}
		return y;
	}
}
