package com.example.jsonplaceholder;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PostActivity extends AppCompatActivity {

    private TextView postTitle;
    private TextView postBody;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postTitle = findViewById(R.id.postTitle);
        postBody = findViewById(R.id.postBody);
        backButton = findViewById(R.id.backButton);

        String title = getIntent().getStringExtra("postTitle");
        String body = getIntent().getStringExtra("postBody");

        postTitle.setText(title);
        postBody.setText(body);

        backButton.setOnClickListener(v -> finish()); // Termina la actividad actual y regresa a la anterior
    }
}
