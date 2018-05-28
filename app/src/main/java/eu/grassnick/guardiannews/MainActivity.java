package eu.grassnick.guardiannews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsArticle>>{
    private static final String TAG = "MainActivity";

    ListView listView;
    ProgressBar loadingSpinner;
    TextView emptyText;
    NewsListAdapter newsListAdapter;

    private static final String apiBaseUrl = "http://content.guardianapis.com/search";
    private static final String apiKey = "9781c31d-726d-4b63-9a0e-8adad5f3cc65";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.news_list_view);
        loadingSpinner = findViewById(R.id.loading_indicator);
        emptyText = findViewById(R.id.news_empty_txt);

        newsListAdapter = new NewsListAdapter(this, new ArrayList<NewsArticle>());
        listView.setAdapter(newsListAdapter);

        getLoaderManager().initLoader(0, null, this).forceLoad();

        //Check internet connection
        if (!isOnline())
            emptyText.setText(R.string.no_connection);
        else
            emptyText.setText(R.string.no_data);

        listView.setEmptyView(emptyText);
    }

    @Override
    public Loader<ArrayList<NewsArticle>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String pageSize = sharedPrefs.getString(
                getString(R.string.settings_pagesize_key),
                getString(R.string.settings_pagesize_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        String keyword = sharedPrefs.getString(
                getString(R.string.settings_keyword_key),
                "");

        // create uri builder and add query parameters to baseUrl
        Uri.Builder uriBuilder = Uri.parse(apiBaseUrl).buildUpon();
        if (keyword != "") //only append q parameter if keyword is set
            uriBuilder.appendQueryParameter("q", keyword);
        uriBuilder.appendQueryParameter("section", "politics");
        uriBuilder.appendQueryParameter("show-fields", "standfirst,trailText,byline");
        uriBuilder.appendQueryParameter("page-size", pageSize);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("from-date", "2010-01-01"); //Dont fetch too old news, they mostly lack information
        uriBuilder.appendQueryParameter("use-date", "published");
        uriBuilder.appendQueryParameter("api-key", apiKey);

        return new NewsArticleLoader(MainActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewsArticle>> loader, ArrayList<NewsArticle> data) {
        newsListAdapter.setArticles(data);

        if (data.size() > 0) {
            // Hide loading indicator because the data has been loaded
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewsArticle>> loader) {
        newsListAdapter.setArticles(new ArrayList<NewsArticle>());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        NetworkInfo netInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
