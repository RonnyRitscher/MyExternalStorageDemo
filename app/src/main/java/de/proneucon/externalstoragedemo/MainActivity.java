package de.proneucon.externalstoragedemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
        tv.append("\n");

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
        tv.append(String.format("Lesen ist%s möglich \n", canRead ? "" : " nicht"));
        tv.append(String.format("Schreiben ist%s möglich \n", canWrite ? "" : " nicht"));
        tv.append("\n");

        //Verzeichnis des externen Speicher-Mediums
        File dirBase = Environment.getExternalStorageDirectory();
        tv.append("Verzeichnis: (normal)\n" + dirBase.getAbsolutePath() + "\n");
        tv.append("\n");

        File pubDirBase = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // getExternalStoragePublicDirectory() darf nicht null sein
        tv.append("Verzeichnis (publicDirecrory): \n" + pubDirBase.getAbsolutePath() + "\n");
        tv.append("\n");

        File root = Environment.getRootDirectory();
        tv.append("Root: \n" + root.getAbsolutePath() + "\n");
        tv.append("\n");

        // Erzeugt die Datei
        File dirAppBase = new File(dirBase.getAbsolutePath() + "/Android/data/" + getClass().getPackage().getName() + "/files");
        //tv.append("dirAppBase: \n"+dirAppBase+ "\n");
        tv.append("PackageName: \n" + getPackageName());
        tv.append("\n");

//        File dirAppBase2 = new File(dirBase.getAbsolutePath() + "/Android/data/" + getPackageName() + "/files");
//        tv.append("dirAppBase: "+dirAppBase2+ "\n");

        // Teste, ob die Unterverzeichnisse vorhanden sind
        // mkdir() gibt true zurück nur wenn das Verzeichen in dem Moment erstellt wird
        if (!dirAppBase.mkdir()) {
            tv.append(String.format("alle Unterverzeichnisse von %s schon vorhanden!", dirAppBase.getAbsolutePath()));
            tv.append("\n");
        } else {
            tv.append(String.format("das Unterverzeichnis %s wurde erstellt", dirAppBase.getAbsolutePath()));
            tv.append("\n");
        }
        tv.append("\n");

        File f1 = getExternalFilesDir(null);
        if (f1 != null) {
            tv.append("Basisverzeichnis: \n" + f1.getAbsolutePath());
            tv.append("\n");
        }
        tv.append("\n");

        File f2 = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (f2 != null) {
            tv.append("Bildverzeichnis: \n" + f1.getAbsolutePath());
            tv.append("\n");
        }
        tv.append("\n");

        //Teste, ob das PubPicture-Verzeichnis angelegt wurde
        if (!pubDirBase.mkdir()) {
            tv.append("PubPictutes ist bereits vorhanden!");
        } else {
            tv.append("PubPictutes wurde angelegt!");
        }

        //Neues File erstellen und unter die pubDirBase speichern
        File file = new File(pubDirBase, "grafik.png");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            saveBitmap(fos);

        } catch (FileNotFoundException e) {
            Log.e(TAG, " ", e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, " ", e);
            e.printStackTrace();
        }


    }

    // BITMAP-OBJEKT speichern
    private void saveBitmap(FileOutputStream fos) {
        int w = 100;
        int h = 100;
        // Bitmao-Objekt wird erzeugt
        Bitmap bm = Bitmap.createBitmap(w,h , Bitmap.Config.RGB_565);
        // Canvas -> ist wie eine Leinwand
        Canvas canvas = new Canvas(bm);

        //Paint-Objekt wird erstellt mit dem man auf der Leinwand arbeitet
        Paint paint = new Paint();
        //Einstellungen des paintObjekts
        paint.setTextAlign(Paint.Align.CENTER);

        // Farbe anpassen -> Weiß
        paint.setColor(Color.WHITE);
        // Rechteck auf der Leinwand erzeugt
        canvas.drawRect(0,0,w-1,h-1, paint);

        // Farbe anpassen -> Blau
        paint.setColor(Color.BLUE);
        // Kreuz durch das Rechteck auf der Leinwand
        canvas.drawLine(0,0,w-1,h-1, paint);
        canvas.drawLine(0,h-1 ,w-1,0 , paint);

        // Farbe anpassen -> Schwarz
        paint.setColor(Color.BLACK);
        // Text hinzufügen
        canvas.drawText("Hallo Android " ,w/2,h/2 , paint);

        //über den OutputStream das Bitmap übergeben
        bm.compress(Bitmap.CompressFormat.PNG , 100 , fos);

        //! Hier kommt der Fehler: EACCES (Permission denied)
        // Permission/Zugriff muss erlaubt werden in der ManifestDatei
        // android.permission.WRITE_EXTERNAL_STORAGE

    }
}
