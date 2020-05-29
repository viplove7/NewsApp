package com.example.viplove.newsapp;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {


    private QueryUtils() {
    }


    public static List<News> extractFeaturesFromJson(String NewsJson) {
        if (TextUtils.isEmpty(NewsJson)) {
            return null;
        }


        List<News> newes = new ArrayList<>();


        try {

            JSONObject baseJsonResponse = new JSONObject(NewsJson);

            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray NewsArray = response.getJSONArray("results");
            for (int i = 0; i < NewsArray.length(); i++) {
                JSONObject results = NewsArray.getJSONObject(i);
                String section_name = results.getString("sectionName");
                String web_title = results.getString("webTitle");
                String time = results.getString("webPublicationDate");
                String url = results.getString("webUrl");
                JSONArray author=results.getJSONArray("tags");

                if(author.length()==0){
                    News news = new News(section_name, web_title, time, url);
                    newes.add(news);
                }else {
                    JSONObject author1 = author.getJSONObject(0);
                    String firstname = author1.getString("firstName");
                    String lastname = author1.getString("lastName");
                    String name;
                    if (firstname.equals("") && lastname.equals("")) {
                        name = "Unknown";
                    } else {
                        name = firstname + " " + lastname;
                    }

                    News news = new News(section_name, web_title, time, url, name);
                    newes.add(news);
                }


            }
        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the News JSON results", e);
        }

        return newes;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);



        } catch (MalformedURLException e) {
            Log.e("LOG_TAG", "problem building the URL", e);

        }
        return url;
    }

    private static String makeHttpResponse(URL url) throws IOException {
        String jsonReponse = "";
        if (url == null) {
            return jsonReponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonReponse = readFromStream(inputStream);
            } else {
                Log.e("LOG_TAG", "Error Response Code" + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e("LOG_TAG", "Problem retrieving the News JSON results:", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonReponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<News> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonReponse = null;
        try {
            jsonReponse = makeHttpResponse(url);
        } catch (IOException e) {
            Log.e("LOG_TAG", "problem making the http request", e);
        }
        List<News> newes = extractFeaturesFromJson(jsonReponse);
        return newes;
    }


}

