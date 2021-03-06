package com.example.newsapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.newsapplication.BuildConfig;
import com.example.newsapplication.R;
import com.example.newsapplication.adapter.MainArticleAdapter;
import com.example.newsapplication.model.Article;
import com.example.newsapplication.model.ResponseModel;
import com.example.newsapplication.rests.ApiClient;
import com.example.newsapplication.rests.ApiInterface;
import com.example.newsapplication.utlis.OnRecyclerViewItemClickListener;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener , NavigationView.OnNavigationItemSelectedListener {

    public static final String API_KEY = "fbe8d05c5cec4782a06ac9a01767a323";
    private DrawerLayout drawerLayout;
    private String country;
    private String category;
    ApiInterface apiServices;
    RecyclerView mainRecyclerView;
    NavigationView navigationView;
    SharedPreferences prefs;
    HashSet<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        country = "in";
        prefs = getSharedPreferences("SharedPreference",MODE_PRIVATE);

        set = new HashSet<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mainRecyclerView = findViewById(R.id.activity_main_rv);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mainRecyclerView.setLayoutManager(linearLayoutManager);

        apiServices = ApiClient.getClient().create(ApiInterface.class);

        if (savedInstanceState == null) {
            loadHome(country);
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.language:
                Intent languageIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(languageIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onItemClick(int position, View view) {
        if (view.getId() == R.id.article_adapter_ll_parent) {
            Article article = (Article) view.getTag();
            if (!TextUtils.isEmpty(article.getUrl())) {
                Log.i("url clicked", article.getUrl());
                Intent webView = new Intent(this, WebActivity.class);
                webView.putExtra("url", article.getUrl());
                Log.i("List", "webView");
                startActivity(webView);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.nav_business:
                country = "in";
                category = "business";
                Log.i("tag","working");
                loadNews(country , category);
                break;

            case R.id.nav_entertainment:
                category = "entertainment";
                loadNews(country,category);
                break;

            case R.id.nav_science:
                category = "science";
                loadNews(country, category);
                break;

            case R.id.nav_health:
                country = "in";
                category = "health";
                loadNews(country,category);
                break;

            case R.id.nav_sports:
                country = "in";
                category = "sports";
                loadNews(country, category);
                break;

            case R.id.nav_home:
                country = "in";
                loadHome(country);
                break;

            case R.id.nav_fav:
                loadList();
                Log.i("log" , set.toString());
                if (set.size() > 0){
                    Intent intent = new Intent(MainActivity.this, showFavorites.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this,AddFavorite.class);
                    startActivity(intent);
                }
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadHome(String country){
        Call<ResponseModel> call = apiServices.getHomeNews(country, API_KEY);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                assert response.body() != null;
                if (response.body().getStatus().equals("ok")) {
                    List<Article> articleList = response.body().getArticles();
                    if (articleList.size() > 0) {
                        Log.i("ListAkash", "List Contains elements");
                        int number = response.body().getTotalResults();
                        Log.i("ListAkash", Integer.toString(number));
                        Toast.makeText(MainActivity.this, Integer.toString(number), Toast.LENGTH_SHORT).show();

                        final MainArticleAdapter mainArticleAdapter = new MainArticleAdapter(articleList);
                        mainArticleAdapter.setOnRecyclerViewItemClickListener(MainActivity.this);

                        mainRecyclerView.setAdapter(mainArticleAdapter);

                    }
                } else {
                    Toast.makeText(MainActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.i("error", t.toString());
            }
        });
    }

    public void loadNews(String country, String category){
        Log.i("tag",category);
        Call<ResponseModel> call = apiServices.getIndianNews(country, category, API_KEY);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                assert response.body() != null;
                if (response.body().getStatus().equals("ok")) {
                    List<Article> articleList = response.body().getArticles();
                    if (articleList.size() > 0) {
                        Log.i("List", "List Contains elements");
                        int number = response.body().getTotalResults();
                        Log.i("List", Integer.toString(number));
                        Toast.makeText(MainActivity.this, Integer.toString(number), Toast.LENGTH_SHORT).show();

                        final MainArticleAdapter mainArticleAdapter = new MainArticleAdapter(articleList);
                        mainArticleAdapter.setOnRecyclerViewItemClickListener(MainActivity.this);

                        mainRecyclerView.setAdapter(mainArticleAdapter);

                    }
                } else {
                    Toast.makeText(MainActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.i("error", t.toString());
            }
        });
    }

    public void loadList(){
        Gson gson = new Gson();
        String json = prefs.getString("key", "null");
        Type type = new TypeToken<HashSet<String>>() {}.getType();
        set = gson.fromJson(json,type);
        if (set == null){
            set = new HashSet<>();
        }
        Log.i("key" , set.toString());
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}