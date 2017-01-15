package sampleMaps.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sampleMaps.shared.ReceptionGridPoint;
import sampleMaps.shared.Square;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapTypeOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapZoomEndHandler;
import com.google.gwt.maps.client.event.MapZoomEndHandler.MapZoomEndEvent;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

//TODO: implement search bar
//TODO: implement header bar
//TODO: implement sign ups
//TODO: implement friend system
//TODO: implement map center
//TODO: implement on move to draw
/*
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SampleMaps implements EntryPoint {
	private final DataServiceAsync _dataService = GWT.create(DataService.class);
	private Integer _gridLevel = 0;
	private final Integer _weight = 1;
	private MapWidget _map;
	private List<Polygon> _visiblePolygons = new ArrayList<Polygon>();
	private Map<Polygon, MapClickHandler> _clickHandlers = new HashMap<Polygon, MapClickHandler>();
	private VerticalPanel _filters;
	private StackPanel _stackPanel = new StackPanel();
	private List<String> _carriers = new ArrayList<String>();

	// GWT module entry point method.
	public void onModuleLoad() {
		_stackPanel.setWidth("100%");
		
		/*
		 * Asynchronously loads the Maps API.
		 * 
		 * The first parameter should be a valid Maps API Key to deploy this
		 * application on a public server, but a blank key will work for an
		 * application served from localhost.
		 */
		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				buildUi();
			}
		});
	}

	private void buildUi() {
		//------------------------------MAP-----------------------------------------
		// Open a map centered on Cawker City, KS USA
		LatLng cawkerCity = LatLng.newInstance(39.509, -98.434);

		MapTypeOptions options = new MapTypeOptions();
		options.setMinResolution(3);
		MapType normal = MapType.getNormalMap();
		MapType myOpts = new MapType(normal.getTileLayers(),
				normal.getProjection(), "my map type", options);
		
		_map = new MapWidget(cawkerCity, 5);
		_gridLevel = getGridLevel(_map.getZoomLevel());
		_map.setCurrentMapType(myOpts);

		_map.setSize("100%", "100%");
		// Add some controls for the zoom level
		_map.addControl(new LargeMapControl());

		// Add a marker
		_map.addOverlay(new Marker(cawkerCity));

		// Add an info window to highlight a point of interest
		_map.getInfoWindow().open(_map.getCenter(),
				new InfoWindowContent("World's Largest Ball of Sisal Twine"));

		_map.addMapZoomEndHandler(new MapZoomEndHandler() {

			@Override
			public void onZoomEnd(MapZoomEndEvent event) {
				handleMapZoomEvent(event);
			}
		});

		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		dock.addNorth(_map, 600);

		RootPanel.get("map").add(dock);
		
		
		//---------------------------------FILTERS------------------------------
		_dataService.getCarriers(new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				_filters = new VerticalPanel();
				for (final String s : result) {
					final CheckBox carrier = new CheckBox(s);
					carrier.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							if(carrier.getValue()){
								_carriers.add(s);
							} else {
								_carriers.remove(s);
							}
							
							clearMap();
							drawData();
						}
					});
					
					_filters.add(carrier);
				}
				_stackPanel.add(_filters, "Carriers");
				RootPanel.get("carriers").add(_stackPanel);
			}

			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}
		});
		
	}

	private void handleMapZoomEvent(MapZoomEndEvent event) {
		if(getGridLevel(event.getNewZoomLevel()) != _gridLevel){
			_gridLevel = getGridLevel(event.getNewZoomLevel());
			clearMap();
			drawData();
		} else if ((event.getOldZoomLevel() - event.getNewZoomLevel()) > 0) {
			clearMap();
			drawData();
//			drawDataNonDrawn();
		}
	}
	
	//TODO: implement itteration two
	private void drawDataNonDrawn() {
		
	}

	private void clearMap(){
		for(Polygon p :  _visiblePolygons){
			p.setVisible(false);
			_map.removeMapClickHandler(_clickHandlers.get(p));
		}

		_map.clearOverlays();
		_visiblePolygons.clear();
		_clickHandlers.clear();
	}
	
	//TODO: implement
	private void clearCarrier(){
		
	}
	
	private void drawData(){
		LatLngBounds squareBounds = _map.getBounds();
		System.out.println(squareBounds.toString());
		LatLng northEast = squareBounds.getNorthEast();
		LatLng southWest = squareBounds.getSouthWest();
		
		final Map<String, String> colorMap = new HashMap<String, String>();
		for(int i=0; i < _carriers.size(); i++){
			colorMap.put(_carriers.get(i), chooseColor(i));
		}
		
		
		System.out.println("Carriers " + _carriers.toString());
		
		_dataService.getReceptionArea(southWest.getLatitude(),
				southWest.getLongitude(), northEast.getLatitude(),
				northEast.getLongitude(), _gridLevel, _carriers,
				new AsyncCallback<List<ReceptionGridPoint>>() {

					@Override
					public void onFailure(Throwable caught) {
						throw new RuntimeException(caught);
					}

					@Override
					public void onSuccess(List<ReceptionGridPoint> result) {
						System.out.println("Done");
						
						for (ReceptionGridPoint area : result) {
							System.out.println(area.toString());
							
							LatLng[] points = getPointsFromSquare(area.getPoints());
							
							final Polygon polygon = new Polygon(points, colorMap.get(area.getCarrier()), _weight, getOpacity(area.getReceptionAverage()), colorMap.get(area.getCarrier()), getOpacity(area.getReceptionAverage()));		
							_map.addOverlay(polygon);
							_visiblePolygons.add(polygon);
							MapClickHandler handler = new MapClickHandler() {
								
								@Override
								public void onClick(MapClickEvent event) {
									if(polygon != null && event != null && polygon.isVisible() && polygon.getBounds() != null && polygon.getBounds().containsLatLng(event.getLatLng())) {
										//TODO: get data here in order to show list in decending order
										String html = "<b>The reception of <br />this carrier is</b>";
										_map.getInfoWindow().open(event.getLatLng(), new InfoWindowContent(html));	
									}
								}
							};
							
							_clickHandlers.put(polygon, handler);
							_map.addMapClickHandler(handler);
						}
					}
				});
	}

	protected LatLng[] getPointsFromSquare(Square points) {
		LatLng northEast = LatLng.newInstance(points.getNorthEast().getLatitude(), points.getNorthEast().getLongitude());
		LatLng northWest = LatLng.newInstance(points.getNorthWest().getLatitude(), points.getNorthWest().getLongitude());
		LatLng southWest = LatLng.newInstance(points.getSouthWest().getLatitude(), points.getSouthWest().getLongitude());
		LatLng southEast = LatLng.newInstance(points.getSouthEast().getLatitude(), points.getSouthEast().getLongitude());
		return new LatLng[]{northEast, northWest, southWest, southEast};
	}

	private int getGridLevel(Integer zoomlevel) {
		switch (zoomlevel) {
		case 3:
		case 4:
			return 1;
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
			return 2;
		case 10:
		case 11:
		case 12:
			return 3;
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
			return 4;
		case 18:
		case 19:
		case 20:
		case 21:
		default:
			return 5;
		}
	}
	
	private String chooseColor(int chooser){
		String blue = "#0000FF";
		String red = "#FF0000";
		String green = "#00AA00";
		
		switch(chooser) {
			case 0:
				return blue;
			case 1:
				return red;
		}
		
		return green;
	}
	
	//TODO: figure out bars based on reception and have opacity as such
	private Double getOpacity(Double reception) {
		return (reception / 100.0) * 0.5;
	}
}