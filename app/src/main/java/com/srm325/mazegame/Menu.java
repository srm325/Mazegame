package com.srm325.mazegame;

import com.srm325.mazegame.R;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class Menu extends Activity implements OnClickListener {
    /** Called when the activity is first created. */

    private static final int STORAGE_PERMISSION_CODE = 101;


    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button newGame = (Button)findViewById(R.id.bNew);
        Button makeyourown = (Button)findViewById(R.id.bOwn);
        Button exit = (Button)findViewById(R.id.bExit);
        newGame.setOnClickListener(this);
        exit.setOnClickListener(this);
        makeyourown.setOnClickListener(this);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

    }
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(Menu.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(Menu.this, new String[] { permission }, requestCode);
        }
        else {
        } }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);


        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Menu.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Menu.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("img", "result");
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == RESULT_LOAD_IMAGE)
            {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);

            }
        }


    }

    Bitmap bitmap=null;
    @Override
    public void onClick(View view) {
        //check which button was clicked with its id
        switch(view.getId()) {
            case R.id.bOwn:
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select an image"), 0);
            case R.id.bExit:
                finish();
                break;
            case R.id.bNew:
                String[] levels = {"Maze 1", "Maze 2", "Maze 3"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.levelSelect));
                builder.setItems(levels, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent game = new Intent(Menu.this,Game.class);  //create an Intent to launch the Game Activity
                        Maze maze = MazeCreator.getMaze(item+1);    //use helper class for creating the Maze
                        game.putExtra("maze", maze);			//add the maze to the intent which we'll retrieve in the Maze Activity
                        startActivity(game);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
        }
    }
}