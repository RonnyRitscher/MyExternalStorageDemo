package de.proneucon.externalstoragedemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialisieren der Elemente
        tv = findViewById(R.id.textView);

        doIt();

    }


    //EIGENE METHODE ZUM SPEICHERN AUF EXTERNES MEDIUM
    private void doIt(){

        //prüfen ob externer Speicher anerkannt wird
        // Envirement-> dieUmgebung
        tv.setText( String.format("Medium kann%s entfernt werden",Environment.isExternalStorageRemovable() ? "" : " nicht") );

    }
}
