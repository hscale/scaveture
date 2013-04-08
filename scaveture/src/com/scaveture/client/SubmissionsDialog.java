package com.scaveture.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.scaveture.shared.Hunt;
import com.scaveture.shared.Submission;

public class SubmissionsDialog extends DialogBox {

    private static SubmissionsDialogUiBinder uiBinder = GWT
            .create(SubmissionsDialogUiBinder.class);

    interface SubmissionsDialogUiBinder extends
            UiBinder<Widget, SubmissionsDialog> {
    }

    public SubmissionsDialog() {
        setWidget(uiBinder.createAndBindUi(this));
        setSize("100%", "100%");
    }
    
    private Hunt hunt = null;
    
    @UiField
    Button prevButton;
    
    @UiField
    Hyperlink mapItLink;
    
    @UiField
    Button nextButton;
    
    @UiField
    Image image;
    
    int currentSubmissionIndex = -1;

    public SubmissionsDialog(Hunt h) {
        setWidget(uiBinder.createAndBindUi(this));
        setText("Submissions for hunt " + h.getDescription());
        setGlassEnabled(true);
        setAutoHideEnabled(true);
        hunt = h;
    }
    
    @Override
    public void show() {
        if(currentSubmissionIndex < 0) {
            viewSubmission(0);
        }
        super.show();
    }
    
    @UiHandler("prevButton")
    void doClickPrev(ClickEvent event) {
        viewSubmission(currentSubmissionIndex - 1);
    }
    
    @UiHandler("nextButton")
    void doClickNext(ClickEvent event) {
        viewSubmission(currentSubmissionIndex + 1);
    }
    
    @UiHandler("mapItLink")
    void doClickMapIt(ClickEvent event) {
        super.hide();
    }
    
    private void viewSubmission(int index) {
        if(index != currentSubmissionIndex && index > -1 && hunt != null && index < hunt.getSubmissions().size()) {
            currentSubmissionIndex = index;
            Submission submission = hunt.getSubmissions().get(currentSubmissionIndex);
            final String url =     GWT.getHostPageBaseURL() + 
                                "scaveture/submission?hid=" + hunt.getId() + 
                                "&sid=" + submission.getId();
            image.setUrl(url);
            mapItLink.setTargetHistoryToken("open=hid" + hunt.getId() + "sid" + submission.getId());
            enableOrDisableButtons();
        }
    }
    
    private void enableOrDisableButtons() {
        if(currentSubmissionIndex == 0) {
            prevButton.setEnabled(false);
        }
        else {
            prevButton.setEnabled(true);
        }
        if(currentSubmissionIndex == hunt.getSubmissions().size() - 1) {
            nextButton.setEnabled(false);
        }
        else {
            nextButton.setEnabled(true);
        }
    }

}
