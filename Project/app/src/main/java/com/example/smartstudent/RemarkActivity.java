package com.example.smartstudent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RemarkActivity extends AppCompatActivity {

    private EditText etRemark;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark);

        etRemark = findViewById(R.id.etRemark);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            String remark = etRemark.getText().toString().trim();
            Intent result = new Intent();
            result.putExtra("remark", remark);
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
