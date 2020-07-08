package com.example.mycardgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class ContactUsActivity extends AppCompatActivity {
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputSubject;
    private TextInputLayout textInputMassege;
    private Button sendMessageBtn;
    private  EmailSubject emailSubject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        textInputEmail = findViewById(R.id.text_input_email);
        textInputSubject = findViewById(R.id.text_input_subject);
        textInputMassege = findViewById(R.id.text_input_massenge);
        sendMessageBtn = findViewById(R.id.sendMessageBtn);
        emailSubject = (EmailSubject) getIntent().getSerializableExtra(ContactUsDialog.SUBJECT);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Contact Us");
        actionBar.setDisplayHomeAsUpEnabled(true);

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInput(v);
            }
        });
    }

    /**
     * check about the email ' ' is not exist
     * check about the email '@' is exist
     * check about the email '.' is exist
     *
     * @return
     */
    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if((emailInput.indexOf('@') == -1) || (emailInput.indexOf('.') == -1)||(emailInput.indexOf(' ') >=0) ) {
            textInputEmail.setError("You must enter an Email");
            return false;
        }
        else{
            textInputEmail.setError(null);
            textInputMassege.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateSubject() {
        String nameInput = textInputSubject.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            textInputSubject.setError("Field can't be empty");
            return false;
        } else {
            textInputSubject.setError(null);
            textInputMassege.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateMassege() {
        String massegeInput = textInputMassege.getEditText().getText().toString().trim();

        if (massegeInput.isEmpty()) {
            textInputMassege.setError("Field can't be empty");
            return false;
        } else {
            textInputMassege.setError(null);
            textInputMassege.setErrorEnabled(false);
            return true;
        }
    }

    public void confirmInput(View v) {
        if (!validateEmail() | !validateSubject() | !validateMassege()) {
            return;
        }
        sendMail();
        //Toast.makeText(this, "Message sent !", Toast.LENGTH_SHORT).show();
    }

    public void sendMail()
    {
        String recipient = "";
        String subject = "";
        if(emailSubject == EmailSubject.REPORT_PROBLEM) {
            recipient  = "liadhazoot5@gmail.com";
            subject = textInputSubject.getEditText().getText().toString().trim() + " - " + "Reporting a Problem";
        }
        else if(emailSubject == EmailSubject.GIVE_FEEDBACK)
        {
             recipient = "liadhazoot5@gmail.com";
             subject = textInputSubject.getEditText().getText().toString().trim() + " - " + "Give Feedback";
        }
        String[] recipients = recipient.split(",");

        //String subject = text.getText().toString();
        String userMassege = textInputMassege.getEditText().getText().toString() +
                " \n---------------------------------------------------------------------" +
                "\n User Email: " + textInputEmail.getEditText().getText().toString().trim();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, userMassege);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
