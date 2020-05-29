package com.example.viplove.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    final String BASE_URL="http://content.guardianapis.com/search?";
    final String query_parameter="q";
    final String api_key_parameter="api-key";
    final String show_tag_param="show-tags";
    public static final String LOG_TAG = NewsActivity.class.getName();
    private NewsAdapter adapter;
    private static final int NEWS_LOADER_ID = 1;
    TextView Emptystate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Emptystate = (TextView) findViewById(R.id.empty_view);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {


            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            Emptystate.setText(R.string.no_internet_connection);
        }


        ListView newsList = (ListView) findViewById(R.id.list);
        adapter = new NewsAdapter(this, new ArrayList<News>());
        newsList.setEmptyView(Emptystate);
        newsList.setAdapter(adapter);
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News current = adapter.getItem(i);
                Uri Newsuri = Uri.parse(current.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, Newsuri);
                startActivity(intent);
            }
        });


    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        Uri buildUri=Uri.parse(BASE_URL)
                 .buildUpon()
                 .appendQueryParameter(query_parameter,"coronovirus")
                .appendQueryParameter(api_key_parameter,"test")
                .appendQueryParameter(show_tag_param,"contributor")
                 .build();



        NewsLoader load = new NewsLoader(this,buildUri.toString());

        return load;
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newes) {


        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        Emptystate.setText(R.string.no_news);

        adapter.clear();

        if (newes != null && !newes.isEmpty()) {
            adapter.addAll(newes);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        adapter.clear();
    }
}
