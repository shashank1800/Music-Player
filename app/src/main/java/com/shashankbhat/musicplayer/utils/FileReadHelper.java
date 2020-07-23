package com.shashankbhat.musicplayer.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by SHASHANK BHAT on 23-Jul-20.
 */
public class FileReadHelper {

    public static String loadJSONFromAsset(@NonNull Context context, @NonNull String fileName) {
        String json = null;

        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];

            //noinspection ResultOfMethodCallIgnored
            inputStream.read(buffer);
            json = new String(buffer, StandardCharsets.UTF_8);

            inputStream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("File read error :" +ex.getMessage());
        }
        return json;

    }
}
