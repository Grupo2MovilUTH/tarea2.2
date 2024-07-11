package com.example.jsonplaceholder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast; // Importar Toast
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private EditText newPostTitleInput;
    private EditText newPostBodyInput;
    private Button saveButton;
    private ArrayList<Post> postsList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> postTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        newPostTitleInput = findViewById(R.id.newPostTitleInput);
        newPostBodyInput = findViewById(R.id.newPostBodyInput);
        saveButton = findViewById(R.id.saveButton);
        postsList = new ArrayList<>();
        postTitles = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, postTitles);
        listView.setAdapter(adapter);

        // Ejecutar AsyncTask para obtener datos
        new GetPostsTask().execute("https://jsonplaceholder.typicode.com/posts");

        // Configurar el botón Salvar
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPostTitle = newPostTitleInput.getText().toString();
                String newPostBody = newPostBodyInput.getText().toString();
                if (!newPostTitle.isEmpty() && !newPostBody.isEmpty()) {
                    Post newPost = new Post(newPostTitle, newPostBody);
                    postsList.add(newPost);
                    postTitles.add(newPostTitle);
                    adapter.notifyDataSetChanged();
                    newPostTitleInput.setText(""); // Limpiar el campo de título
                    newPostBodyInput.setText("");  // Limpiar el campo de cuerpo
                    // Mostrar mensaje de confirmación
                    Toast.makeText(MainActivity.this, "El post se ha guardado correctamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, PostActivity.class);
            intent.putExtra("postTitle", postsList.get(position).getTitle());
            intent.putExtra("postBody", postsList.get(position).getBody());
            startActivity(intent);
        });
    }

    private class GetPostsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject post = jsonArray.getJSONObject(i);
                    String title = post.getString("title");
                    String body = post.getString("body");
                    postsList.add(new Post(title, body));
                    postTitles.add(title);
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
