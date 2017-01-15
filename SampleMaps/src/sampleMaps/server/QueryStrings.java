package sampleMaps.server;

public class QueryStrings {
	public static final String CARRIER_ID_COL_NAME = "id";
	public static final String CARRIER_COL_NAME = "name";
	public static final String CARRIER_TABLE_NAME = "carrier";
	public static final String CARRIER_QUERY = "select " + CARRIER_COL_NAME
			+ " from " + CARRIER_TABLE_NAME;

	public static String buildSpecificCarrierQuery(int carrierId) {
		return CARRIER_QUERY + " where " + CARRIER_ID_COL_NAME + " = "
				+ carrierId;
	}

	public static final String GRID_RECEPTION_COL_NAME = "avgrec";
	public static final String GRID_NETWORK_COL_NAME = "network";
	public static final String GRID_CARRIER_COL_NAME = "carrier";
	public static final String GRID_TABLE_NAME = "gridlevel";
	
	public static String buildGridXColName(int gridLevel) {
		return "gss" + gridLevel + "x";
	}
	
	public static String buildGridYColName(int gridLevel) {
		return "gss" + gridLevel + "y";
	}

	public static String buildGridQuery(int gridLevel, int minX, int maxX,
			int minY, int maxY) {
		String ret = "";
		
		if(minX > maxX) {
			ret = "select " + buildGridXColName(gridLevel) + ", "
			+ buildGridYColName(gridLevel) + ", " + GRID_RECEPTION_COL_NAME
			+ ", " + GRID_NETWORK_COL_NAME + ", " + GRID_CARRIER_COL_NAME
			+ " from " + GRID_TABLE_NAME + gridLevel + " where ("
			+ buildGridXColName(gridLevel) + " >= " + minX + " or "
			+ buildGridXColName(gridLevel) + " <= " + maxX + ") and "
			+ buildGridYColName(gridLevel) + " >= " + minY + " and "
			+ buildGridYColName(gridLevel) + " <= " + maxY;
		} else {
			ret = "select " + buildGridXColName(gridLevel) + ", "
					+ buildGridYColName(gridLevel) + ", " + GRID_RECEPTION_COL_NAME
					+ ", " + GRID_NETWORK_COL_NAME + ", " + GRID_CARRIER_COL_NAME
					+ " from " + GRID_TABLE_NAME + gridLevel + " where "
					+ buildGridXColName(gridLevel) + " >= " + minX + " and "
					+ buildGridXColName(gridLevel) + " <= " + maxX + " and "
					+ buildGridYColName(gridLevel) + " >= " + minY + " and "
					+ buildGridYColName(gridLevel) + " <= " + maxY;
		}
		
		System.out.println(ret);
		
		return ret;
	}
}
