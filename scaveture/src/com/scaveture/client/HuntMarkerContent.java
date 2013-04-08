package com.scaveture.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.scaveture.shared.Hunt;

public class HuntMarkerContent extends Composite implements MarkerContent {

    private static HuntMarkerContentUiBinder uiBinder = GWT
            .create(HuntMarkerContentUiBinder.class);

    interface HuntMarkerContentUiBinder extends
            UiBinder<Widget, HuntMarkerContent> {
    }
    
    private Hunt hunt;
    private Marker marker;
    private InfoWindow infoWindow;
    private boolean isMobileClient;
    
    @UiField
    HTMLPanel panel;
    
    @UiField
    Label descriptionLabel;
    
    @UiField
    Anchor foundLink;
    
    @UiField
    Hyperlink submissionsLink;

    public HuntMarkerContent(Hunt h, boolean isMobile) {
        initWidget(uiBinder.createAndBindUi(this));
        hunt = h;
        isMobileClient = isMobile;
        descriptionLabel.setText(hunt.getDescription());
        descriptionLabel.addStyleDependentName("Bigger");
        foundLink.addStyleDependentName("Big");
        submissionsLink.addStyleDependentName("Smaller");
        String queryStart = null;
        
        if(isMobileClient) {
            foundLink.setText("found it! (take a picture)");
            queryStart = "&path=&id=";
        }
        else {
            foundLink.setText("found it! (upload a picture)");
            queryStart = "#path=&id=";
        }

        foundLink.setHref(Window.Location.getHref() + queryStart + 
                          hunt.getId() + "&lat=" + hunt.getLatitude() + 
                          "&long=" + hunt.getLongitude());
        
        if(hunt.getSubmissions().size() > 0) {
            submissionsLink.setTargetHistoryToken("hid=" + hunt.getId());
            submissionsLink.setText("view submissions (" + h.getSubmissions().size() + ")");
        }
    }

    @Override
    public String getIconUrl() {
        return "http://google-maps-icons.googlecode.com/files/sight.png";
    }

    @Override
    public String toHtml() {
        return panel.toString();
    }

    @Override
    public String uniqueKey() {
        return "hid" + hunt.getId();
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
        return LatLng.newInstance(hunt.getLatitude(), hunt.getLongitude());
    }

    @Override
    public String getText() {
        if(hunt != null) {
            return hunt.getDescription();
        }
        return null;
    }

    @Override
    public void setText(String text) {
        // no-op
    }
}
