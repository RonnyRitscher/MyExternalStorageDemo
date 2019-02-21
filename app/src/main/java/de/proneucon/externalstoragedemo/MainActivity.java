package de.proneucon.externalstoragedemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //LOG-TAG
    private static final String TAG = MainActivity.class.getSimpleName();


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
    private void doIt() {

        //prüfen ob externer Speicher anerkannt wird
        // Envirement-> die System-Umgebung der Application
        tv.setText(String.format("Medium kann%s entfernt werden \n", Environment.isExternalStorageRemovable() ? "" : " nicht"));

        //Welchen Status hat unser Externes Medium?
        // -> kann gelesen oder geschrieben werden?
        final String state = Environment.getExternalStorageState();
        final boolean canRead, canWrite;
        switch (state) {
            case Environment.MEDIA_MOUNTED:
                canRead = true;
                canWrite = true;
                break;

            case Environment.MEDIA_MOUNTED_READ_ONLY:

                canRead = true;
                canWrite = false;
                break;

            default:
                canRead = canWrite = false;
        }
        tv.append(String.format("Lesen ist%s möglich \n" , canRead?"":" nicht"));
        tv.append(String.format("Schreiben ist%s möglich \n" , canWrite?"":" nicht"));


        //Verzeichnis des externen Speicher-Mediums
        File dirBase = Environment.getExternalStorageDirectory();
        tv.append("Verzeichnis: (normal)"+dirBase.getAbsolutePath() + "\n");

        File pubDirBase = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // getExternalStoragePublicDirectory() darf nicht null sein
        tv.append("Verzeichnis (publicDirecrory): "+pubDirBase.getAbsolutePath() + "\n");

        File root = Environment.getRootDirectory();
        tv.append("Root: "+root.getAbsolutePath() + "\n");

        File dirAppBase = new File(dirBase.getAbsolutePath() + "/Android/data/" + getClass().getPackage().getName() + "/files");
        //tv.append("dirAppBase: "+dirAppBase+ "\n");
        tv.append(getPackageName());

//        File dirAppBase2 = new File(dirBase.getAbsolutePath() + "/Android/data/" + getPackageName() + "/files");
//        tv.append("dirAppBase: "+dirAppBase2+ "\n");

        // Wen die Unterverzeichnisse vorhanden sind
        // mkdir() gibt true zurück nur wenn das Verzeichen in dem Moment erstellt wird
        if( !dirAppBase.mkdir() ){
            tv.append(String.format("alle Unterverzeichnisse von %s schon vorhanden!" , dirAppBase.getAbsolutePath() ));
        }else{
            tv.append(String.format("das Unterverzeichnis %s wurde erstellt" , dirAppBase.getAbsolutePath()));
        }

    }
}
