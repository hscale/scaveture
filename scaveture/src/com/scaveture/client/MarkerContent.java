package com.scaveture.client;

import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.HasText;

public interface MarkerContent extends HasText {
	String getIconUrl();
	String toHtml();
	String uniqueKey();
	LatLng getCoordinates();
	Marker getMarker();
	void setMarker(Marker m);
	InfoWindow getInfoWindow();
	void setInfoWindow(InfoWindow w);
}
