package com.school.schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Activity {
	EditText username, wilmaUrl, password;
	Button btnregister;

	RegisterAdapter register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fill_info);

		register = new RegisterAdapter(this);
		register = register.open();

		username = (EditText) findViewById(R.id.username);
		wilmaUrl = (EditText) findViewById(R.id.wilma_url);
		password = (EditText) findViewById(R.id.Password);

		btnregister = (Button) findViewById(R.id.saveButton);

		btnregister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				String usernameText = username.getText().toString();
				String passwordText = password.getText().toString();
				String wilmaUrlText = wilmaUrl.getText().toString();

				if (usernameText.equals("") || passwordText.equals("")
						|| wilmaUrlText.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Täytä kaikki kentät", Toast.LENGTH_LONG).show();
					return;
				}

				else {
					register.insertEntry(usernameText, passwordText,wilmaUrlText);
					Toast.makeText(getApplicationContext(),
							"Tiedot lisättiin onnistuneesti ",
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		register.close();
	}
}
