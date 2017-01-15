package sampleMaps.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sampleMaps.client.DataService;
import sampleMaps.shared.LatitudeLongitude;
import sampleMaps.shared.ReceptionGridPoint;
import sampleMaps.shared.Square;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DataServiceImpl extends RemoteServiceServlet implements
		DataService {
	private static final long serialVersionUID = 1L;

	@Override
	public List<String> getCarriers() {
		ArrayList<String> carriers = new ArrayList<String>();

		try {
			ResultSet results = executeCmd(QueryStrings.CARRIER_QUERY);

			while (results != null && results.next()) {
				carriers.add(results.getString(QueryStrings.CARRIER_COL_NAME));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return carriers;
	}

	@Override
	public List<ReceptionGridPoint> getReceptionArea(double topLat, double topLong,
			double botLat, double botLong, int gridLevel, List<String> carriers) {
		Map<GridKey, ReceptionGridPoint> map = new HashMap<GridKey, ReceptionGridPoint>();
		
		try {
			GridKey top = Grid.toDBGrid(gridLevel, topLat, topLong);
			GridKey bot = Grid.toDBGrid(gridLevel, botLat, botLong);

			int minX = top.getX();
			int maxX = bot.getX();
			int minY = top.getY();
			int maxY = bot.getY();

			ResultSet gridResults = executeCmd(QueryStrings.buildGridQuery(
					gridLevel, minX, maxX, minY, maxY));

			while (gridResults != null && gridResults.next()) {
				ResultSet carrierResults = executeCmd(QueryStrings
						.buildSpecificCarrierQuery(gridResults
								.getInt(QueryStrings.GRID_CARRIER_COL_NAME)));

				while (carrierResults != null && carrierResults.next()) {
					if(carriers.contains(carrierResults.getString(QueryStrings.CARRIER_COL_NAME))) {
						GridKey key = new GridKey(gridResults.getInt(QueryStrings
								.buildGridXColName(gridLevel)),
								gridResults.getInt(QueryStrings.buildGridYColName(gridLevel)));
						
						Square points = Grid.toPoints(
								gridLevel,
								gridResults.getInt(QueryStrings.buildGridXColName(gridLevel)),
								gridResults.getInt(QueryStrings.buildGridYColName(gridLevel)));
						
						ReceptionGridPoint value = new ReceptionGridPoint(
								gridResults.getDouble(QueryStrings.GRID_RECEPTION_COL_NAME),
								gridResults.getDouble(QueryStrings.GRID_NETWORK_COL_NAME),
								points,
								carrierResults.getString(QueryStrings.CARRIER_COL_NAME));
						
						value.setX(gridResults.getInt(QueryStrings.buildGridXColName(gridLevel)));
						value.setY(gridResults.getInt(QueryStrings.buildGridYColName(gridLevel)));
						
						if(map.containsKey(key)) {
							//TODO: figure out what to do when two carriers have equal reception
							if(map.get(key).getReceptionAverage() < value.getReceptionAverage()) {
								map.put(key, value);
							}
						} else {
							map.put(key, value);
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return new ArrayList<ReceptionGridPoint>(map.values());
	}

	private ResultSet executeCmd(String cmd) throws SQLException {
		Connection conn = MySQLConnection.getConnection();

		return conn == null ? null : conn.createStatement().executeQuery(cmd);
	}
}
