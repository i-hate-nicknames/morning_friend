package com.domain.nvm.morningfriend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LogActivity extends AppCompatActivity {

    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        mTextView = (TextView) findViewById(R.id.activity_log_text_view);
        mTextView.setText(Logger.read(this));
    }
}
