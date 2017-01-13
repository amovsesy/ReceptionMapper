package com.amphro.receptionmapper.reports.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Login {
	private static VerticalPanel vpanel;
	
	private static void setUp(){
		
		if(vpanel == null)
			vpanel = new VerticalPanel();
		
		vpanel.clear();
		
		HorizontalPanel h1 = new HorizontalPanel();
		HorizontalPanel h2 = new HorizontalPanel();
		HorizontalPanel h3 = new HorizontalPanel();
		
		final Button sendButton = new Button("Login");
		final TextBox usernameField = new TextBox();
		final TextBox passwordField = new PasswordTextBox();
		
		Label user = new Label("Username:");
		Label pass = new Label("Password:");
		
		vpanel.setSpacing(5);
		
		h1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		h1.setSpacing(5);
		h1.add(user);
		h1.add(usernameField);
		
		h2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		h2.setSpacing(5);
		h2.add(pass);
		h2.add(passwordField);
		
		h3.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		h3.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		h3.setSpacing(5);
		h3.add(sendButton);
		
		vpanel.add(h1);
		vpanel.add(h2);
		vpanel.add(h3);
		
		// Focus the cursor on the name field when the app loads
		usernameField.setFocus(true);
		usernameField.selectAll();
		
		sendButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ReceptionMapperReports.root.clear();
				ReceptionMapperReports.root.add(ReportSelection.getLayout());
			}
		});
		
		passwordField.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					ReceptionMapperReports.root.clear();
					ReceptionMapperReports.root.add(ReportSelection.getLayout());
				}
			}
		});
	}
	
	public static void getLayout(){
		setUp();
		ReceptionMapperReports.root.add(vpanel);
	}
}
