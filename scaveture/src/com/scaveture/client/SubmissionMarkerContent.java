package com.scaveture.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.scaveture.shared.Submission;

public class SubmissionMarkerContent extends Composite implements MarkerContent {

	private static SubmissionMarkerContentUiBinder uiBinder = GWT
			.create(SubmissionMarkerContentUiBinder.class);

	interface SubmissionMarkerContentUiBinder extends
			UiBinder<Widget, SubmissionMarkerContent> {
	}
	
	private Submission submission;
	private Marker marker;
	private InfoWindow infoWindow;
	
	@UiField
	HTMLPanel panel;
	
	@UiField
	Hyperlink huntLink;
	
	@UiField
	Image image;

	public SubmissionMarkerContent(Submission s) {
		initWidget(uiBinder.createAndBindUi(this));
		submission = s;
		final String url = 	GWT.getHostPageBaseURL() + 
							"scaveture/submission?hid=" + submission.getHunt().getId() + 
							"&sid=" + submission.getId();
		image.setUrl(url);
		huntLink.setTargetHistoryToken("open=hid" + submission.getHunt().getId());
	}

	@Override
	public String getIconUrl() {
		return "http://google-maps-icons.googlecode.com/files/photo.png";
	}

	@Override
	public String toHtml() {
		return panel.toString();
	}

	@Override
	public String uniqueKey() {
		return "hid" + submission.getHunt().getId() + "sid" + submission.getId();
	}

	@Override
	public Marker getMarker() {
		return marker;
	}

	@Override
	public void setMarker(Marker m) {
		marker = m;
	}

	@Override
	public InfoWindow getInfoWindow() {
		return infoWindow;
	}

	@Override
	public void setInfoWindow(InfoWindow w) {
		infoWindow = w;
	}

	@Override
	public LatLng getCoordinates() {
		return LatLng.newInstance(submission.getLatitude(), submission.getLongitude());
	}

	@Override
	public String getText() {
		if(submission != null && submission.getHunt() != null) {
			return submission.getHunt().getDescription();
		}
		return null;
	}

	@Override
	public void setText(String text) {
		// no-op
	}
}
