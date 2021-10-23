package com.swufestu.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class WordPage extends AppCompatActivity {

    TextView word,sent,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_page);
        word = findViewById(R.id.page_word);
        sent = findViewById(R.id.word_sent);
        type = findViewById(R.id.word_type);
        sent.setMovementMethod(ScrollingMovementMethod.getInstance());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String word1 = bundle.getString("word");
        String sent1 = bundle.getString("sent");
        String type1 = bundle.getString("type");

        word.setText(word1);
        sent.setText(sent1);
        type.setText(type1);
    }
}