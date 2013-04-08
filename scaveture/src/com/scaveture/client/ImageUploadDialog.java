package com.scaveture.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;

public class ImageUploadDialog extends DialogBox {
    
    private String id = null;
    private String latitude = null;
    private String longitude = null;
    
    @UiField
    FormPanel uploadForm;
    
    @UiField
    FileUpload upload;
    
    @UiField
    Button cancelButton;
    
    @UiField
    Button uploadButton;

    private static ImageUploadDialogUiBinder uiBinder = GWT
            .create(ImageUploadDialogUiBinder.class);

    interface ImageUploadDialogUiBinder extends
            UiBinder<Widget, ImageUploadDialog> {
    }

    public ImageUploadDialog() {
        setWidget(uiBinder.createAndBindUi(this));
        // The upload form, when submitted, will trigger an HTTP call to the
        // servlet.  The following parameters must be set
        uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
        uploadForm.setMethod(FormPanel.METHOD_POST);
        setText("Found it? Upload your picture...");
        setGlassEnabled(true);
        addStyleDependentName("Big");
    }
    
    @UiHandler("cancelButton")
    void doClickCancel(ClickEvent event) {
        hide();
    }
    
    @UiHandler("uploadButton")
    void doClickUpload(ClickEvent event) {
        hide();
    }

    private InputElement getInputElement() {
        return upload.getElement().cast();
    }

    public void setPath(String path) {
        /**
         * This doesn't work because browser security restrictions 
         * prevent programmatically setting the value of an input 
         * element of type "file" (the underlying element here).
         */
        getInputElement().setValue(path);
    }

    public String getPath() {
        return getInputElement().getValue();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }
    
    public static void open(String id, String path, String latitude, String longitude) {
        ImageUploadDialog dlg = new ImageUploadDialog();
        dlg.setId(id);
        dlg.setPath(path);
        dlg.setLatitude(latitude);
        dlg.setLongitude(longitude);
        dlg.show();
        dlg.center();
    }
    
    public static native void exportJavaScriptMethod() /*-{
        $wnd.openImageUploadDialog = function(id, path, latitude, longitude) {
            @com.scaveture.client.ImageUploadDialog::open(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(id, path, latitude, longitude);
        }
    }-*/;
}
