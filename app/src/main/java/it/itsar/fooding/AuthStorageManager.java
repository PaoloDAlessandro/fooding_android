package it.itsar.fooding;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AuthStorageManager {

    public static String AUTH_FILE_NAME = "/auth.txt";

    public AuthStorageManager() {
    }

    public void backupToFile(File file, User user) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(user);
            oos.flush();
        }
    }

    public User backupFromFile(String fileName) {
        User user = new User();
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            user = (User) objectIn.readObject();
            Log.d("USER FROM FILE: ", user.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public boolean deleteFile(File file) {
        return file.delete();
    }
}
