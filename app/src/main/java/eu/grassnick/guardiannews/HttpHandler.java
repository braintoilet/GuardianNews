package eu.grassnick.guardiannews;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpHandler {
    private static final String TAG = "HttpHandler";


    public HttpHandler() {
    }

    public static URL parseUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public String makeGET(URL url) {
        String response = "";
        HttpURLConnection connection = null;
        InputStream ioStream = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.connect();
            ioStream = connection.getInputStream();
            response = convertStreamToString(ioStream);

        } catch (IOException e) {
            Log.e(TAG, "getHttpRequest: " + e.toString(), e);
        } finally {
            if (connection != null)
                connection.disconnect();
            if (ioStream != null)
                try {
                    ioStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "getHttpRequest: cant close stream", e);
                }

            return response;
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
