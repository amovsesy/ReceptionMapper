/**
 * 
 */
package com.amphro.receptionmapper.logger;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amphro.receptionmapper.logger.User.UserState;

/**
 * @author Aleks, Thomas Dvornik
 *
 */
public class UserDialog extends Dialog {
	private TextView mText;
	private EditText mEdit;
	private User mUser;
	
	private Logger mLogger;
	
	public UserDialog(Context context, User user) {
		super(context);
		mLogger = (Logger) context;
		mUser = user;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.userdialog);
    	setTitle("Welcome.");

    	mText = (TextView) findViewById(R.id.text);
    	mText.setText("Please enter a username.");
    	mEdit = (EditText) findViewById(R.id.edit);
    	
    	final Button button = (Button) findViewById(R.id.button);
 
    	button.setText("Ok");
    	button.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(android.view.View v) {
				UserState state = mUser.insertUser(getUsername(), Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID));

				if (state == UserState.USER_CREATED) {
					mLogger.startLogging();
					mLogger.setText();
					dismiss();
				} else if (state == UserState.PHONEID_EXIST) {
					mText.setText(state.getMessage() + ". Please contact us at support@receptionmapper.com to resolve this issue.");
					mEdit.setEnabled(false);
					button.setEnabled(false);
				}
				else {
					mText.setText(state.getMessage() + ". Please try another user name.");
				}
			}
    		
    	});

	}
	
	public String getUsername() {
		return mEdit.getText().toString();
	}
	
	public void setUsername(String name) {
		mEdit.setText(name);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
