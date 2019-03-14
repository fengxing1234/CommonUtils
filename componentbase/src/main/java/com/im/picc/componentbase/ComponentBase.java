package com.im.picc.componentbase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ComponentBase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_base);
    }
}
