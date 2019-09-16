package com.masthuggis.boki.backend;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility class with functionality for Fetching data from Backend in form of a .json-file.
 * The .json-file must be placed in the assets-folder of the application
 */
public class BackendDataFetcher {

    /**
     * Converts the contents of a json-file into a String via a Context-object.
     * @param context the Context-object required to load the .json-file via the assets-folder
     * @return the contents of the loaded .json-file formatted as a String.
     */

     static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open("mockBooks.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            return json;

        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
