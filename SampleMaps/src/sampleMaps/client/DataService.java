package sampleMaps.client;

import java.util.List;

import sampleMaps.shared.ReceptionGridPoint;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("data")
public interface DataService extends RemoteService {
	List<String> getCarriers();
	
	List<ReceptionGridPoint> getReceptionArea(double topLat, double topLong, double botLat, double botLong, int gridLevel, List<String> carriers);
}
