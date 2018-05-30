package eu.grassnick.guardiannews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsArticleLoader extends AsyncTaskLoader<ArrayList<NewsArticle>> {
    private static final String TAG = "NewsArticleLoader";

    private String apiQueryUrl;

    public NewsArticleLoader(Context context, String url) {
        super(context);
        Log.d(TAG, "NewsArticleLoader: passed url = " + url);
        apiQueryUrl = url;
    }

    @Override
    public ArrayList<NewsArticle> loadInBackground() {
        ArrayList<NewsArticle> results = new ArrayList<>();


        //parse url and get json data from API via http request
        String rawData = new HttpHandler().makeGET(HttpHandler.parseUrl(apiQueryUrl));

        if (rawData != null) {
            //try to parse json data into NewsArticle ArrayList
            try {
                JSONArray jsonResults = new JSONObject(rawData).getJSONObject("response").getJSONArray("results");

                for (int i = 0; i < jsonResults.length(); i++) {
                    JSONObject current = jsonResults.getJSONObject(i);
                    String sectionName = current.getString("sectionName");
                    String dateTime = current.getString("webPublicationDate");
                    String date = dateTime.split("T")[0];
                    String time = dateTime.split("T")[1].substring(0, 5);
                    String headline = current.getString("webTitle");
                    String trailText = current.getJSONObject("fields").getString("trailText");
                    trailText = Html.fromHtml(trailText).toString(); //strip html tags
                    String articleUrl = current.getString("webUrl");
                    String author;
                    //sometimes there is no byline, so set to "unknown" if exception is thrown
                    try {
                        author = current.getJSONObject("fields").getString("byline");
                    } catch (JSONException je) {
                        author = "unknown";
                    }

                    results.add(new NewsArticle(time, date, headline, trailText, articleUrl, author, sectionName));
                }
            } catch (final JSONException e) {
                Log.e(TAG, "loadInBackground: ", e);
            }
        }

        return results;
    }
}
