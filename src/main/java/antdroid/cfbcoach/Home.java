package antdroid.cfbcoach;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Home extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    private boolean customCareer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageLogo = findViewById(R.id.imageLogo);
        imageLogo.setImageResource(R.drawable.main_menu_logo);

        Button newGameButton = findViewById(R.id.buttonNewGame);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(Home.this, MainActivity.class);
                myIntent.putExtra("SAVE_FILE", "NEW_LEAGUE_DYNASTY");
                Home.this.startActivity(myIntent);
                finish();
            }
        });

        Button newCustomGameButton = findViewById(R.id.buttonCustomNewGame);
        newCustomGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isExternalStorageReadable();
                customCareer = false;
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("text/plain");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });


        Button newCareerButton = findViewById(R.id.buttonCareer);
        newCareerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(Home.this, MainActivity.class);
                myIntent.putExtra("SAVE_FILE", "NEW_LEAGUE_CAREER");
                Home.this.startActivity(myIntent);
                finish();
            }
        });

        Button newCustomCareerButton = findViewById(R.id.buttonCustomCareer);
        newCustomCareerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isExternalStorageReadable();
                customCareer = true;
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("text/plain");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });

        Button loadGameButton = findViewById(R.id.buttonLoadGame);
        loadGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                loadLeague();
            }
        });

        Button deleteGameButton = findViewById(R.id.buttonDeleteSave);
        deleteGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                deleteSave();
            }
        });

        Button tutorialButton = findViewById(R.id.buttonTutorial);
        tutorialButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent myIntent = new Intent(Home.this, TutorialActivity.class);
                Home.this.startActivity(myIntent);
            }
        });

        Button youtubeButton = findViewById(R.id.youtubeTutorial);
        youtubeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://youtu.be/_zEuX0JAYBg"));
                startActivity(intent);
            }
        });

        Button subredditButton = findViewById(R.id.buttonSubreddit);
        subredditButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://m.reddit.com/r/FootballCoach"));
                startActivity(intent);
            }
        });


        Button antdroidButton = findViewById(R.id.buttonAntdroid);
        antdroidButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.Antdroid.net"));
                startActivity(intent);
            }
        });

        Button githubButton = findViewById(R.id.buttonGitHub);
        githubButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://github.com/antdroidx/CFB-Coach"));
                startActivity(intent);
            }
        });

/*        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Welcome to College Football Coach: Career Edition\n\n" +
                "April 09, 2018" +
                "\n+Implemented Special Teams into Game Simulation" +
                "\n+Kick & Punt Returner Season Stats Tracking" +
                "\n+Returner Stats added to Awards scoring" +
                "\n+New In-Season Awards Watch Menu" +
                "\n+Option to Enable/Disable Potential Rating Display" +
                "\n+Player Height/Weight Info" +
                "\n\nApril 07, 2018" +
                "\n+Added second Safety position" +
                "\n+Added in All-Freshman Team Awards" +
                "\n+Hide Potential Rating (replaced with Predicted Future Rating)" +
                "\n+Removed Overall/Potential rating from Recruiting / Replaced with Scout Grade" +
                "\n+Balanced Offense Strategies (Spread/West Coast Offense tweaks)" +
                "\n+Added Settings option for FULL Play-By-Play Game Log (slower!)")
                .setTitle("Update Log")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView textView = dialog.findViewById(android.R.id.message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * Save League, show dialog to choose which save file to save onto.
     */
    private void loadLeague() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose File to Load:");
        final String[] fileInfos = getSaveFileInfos();
        SaveFilesListArrayAdapter saveFilesAdapter = new SaveFilesListArrayAdapter(this, fileInfos);
        builder.setAdapter(saveFilesAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                if (!fileInfos[item].equals("EMPTY")) {
                    Intent myIntent = new Intent(Home.this, MainActivity.class);
                    myIntent.putExtra("SAVE_FILE", "saveFile" + item + ".cfb");
                    Home.this.startActivity(myIntent);
                } else {
                    Toast.makeText(Home.this, "Cannot load empty file!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Delete Save
     */
    private void deleteSave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose File to Delete:");
        final String[] fileInfos = getSaveFileInfos();
        SaveFilesListArrayAdapter saveFilesAdapter = new SaveFilesListArrayAdapter(this, fileInfos);
        builder.setAdapter(saveFilesAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                if (!fileInfos[item].equals("EMPTY")) {
                    deleteFile("saveFile" + item + ".cfb");
                } else {
                    Toast.makeText(Home.this, "Cannot load delete file!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Get info of the 10 save files for printing in the save file list
     */
    private String[] getSaveFileInfos() {
        String[] infos = new String[10];
        String fileInfo;
        File saveFile;
        File extFile;
        for (int i = 0; i < 10; ++i) {
            saveFile = new File(getFilesDir(), "saveFile" + i + ".cfb");
            if (saveFile.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(saveFile));
                    fileInfo = bufferedReader.readLine();
                    infos[i] = fileInfo.substring(0, fileInfo.length() - 1); //gets rid of % at end
                } catch (FileNotFoundException ex) {
                    System.out.println(
                            "Unable to open file");
                } catch (IOException ex) {
                    System.out.println(
                            "Error reading file");
                } catch (NullPointerException ex) {
                    System.out.println(
                            "Null pointer exception!");
                }
            } else {
                infos[i] = "EMPTY";
            }
        }
        return infos;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
    /* Creates external Save directory */

    public File getExtSaveDir(Context context, String cfbCoach) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS), cfbCoach);
        if (!file.mkdirs()) {
            Log.e(cfbCoach, "Directory not created");
        }
        return file;
    }

    //Takes Custom Universe file and converts it to separate csv files
    private void customLeague(Uri uri) {
        Intent myIntent = new Intent(Home.this, MainActivity.class);

        try {
            File conferences = new File(getFilesDir(), "conferences.txt");
            File teams = new File(getFilesDir(), "teams.txt");
            String line;
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            //First ignore the save file info
            line = reader.readLine();
            //Next get league history
            while ((line = reader.readLine()) != null && !line.equals("[END_CONFERENCES]")) {
                sb.append(line + ",");
            }
            sb.append("END_CONFERENCES");

            // Actually write to the file
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(conferences)))) {
                writer.write(sb.toString());
            } catch (Exception e) {
            }
            StringBuilder sb1 = new StringBuilder();

            //Next get heismans
            while ((line = reader.readLine()) != null && !line.equals("END_TEAMS")) {
                sb1.append(line + ",");
            }
            sb1.append("END_TEAMS");
            // Actually write to the file
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(teams)))) {
                writer.write(sb1.toString());
            } catch (Exception e) {
            }
            // Always close files.
            reader.close();
            myIntent.putExtra("SAVE_FILE", "NEW_LEAGUE_CAREER-CUSTOM");
            Home.this.startActivity(myIntent);
        } catch (Exception e) {
            Toast.makeText(Home.this, "Error! Bad URL or unable to read file.", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getURI(Intent intent) {
        Uri uri = intent.getData();
        return uri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        Intent myIntent = new Intent(Home.this, MainActivity.class);
        String uriStr;

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Home.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            uri = resultData.getData();
            uriStr = uri.toString();
            if (customCareer) {
                myIntent.putExtra("SAVE_FILE", "NEW_LEAGUE_CAREER-CUSTOM," + uriStr);
            } else {
                myIntent.putExtra("SAVE_FILE", "NEW_LEAGUE_DYNASTY-CUSTOM," + uriStr);
            }
            Home.this.startActivity(myIntent);
            //finish();

        }
    }

}


