package com.scaveture.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class NewHuntDialog extends DialogBox {

	private static NewHuntDialogUiBinder uiBinder = GWT.create(NewHuntDialogUiBinder.class);
	
	private boolean saved = false;

	interface NewHuntDialogUiBinder extends UiBinder<Widget, NewHuntDialog> {
	}
	
	@UiField
	TextArea descriptionText;
	
	@UiField
	Button cancelButton;
	
	@UiField
	Button saveButton;

	public NewHuntDialog() {
		setWidget(uiBinder.createAndBindUi(this));
		setText("Describe the item to be found:");
	    setGlassEnabled(true);
	    addStyleDependentName("Big");
	}
	
	public void clear() {
		saved = false;
		descriptionText.setText("");
	}
	
	@UiHandler("cancelButton")
	void doClickCancel(ClickEvent event) {
		hide();
	}
	
	@UiHandler("saveButton")
	void doClickSave(ClickEvent event) {
		saved = true;
		hide();
	}

	public boolean isSaved() {
		return saved;
	}
	
	public String getDescription() {
		return descriptionText.getText();
	}
}
