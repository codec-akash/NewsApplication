package com.example.newsapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.newsapplication.BuildConfig;
import com.example.newsapplication.R;
import com.example.newsapplication.adapter.MainArticleAdapter;
import com.example.newsapplication.model.Article;
import com.example.newsapplication.model.ResponseModel;
import com.example.newsapplication.rests.ApiClient;
import com.example.newsapplication.rests.ApiInterface;
import com.example.newsapplication.utlis.OnRecyclerViewItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class showFavorites extends AppCompatActivity implements OnRecyclerViewItemClickListener {

    SharedPreferences prefs;
    public static final String API_KEY = "fbe8d05c5cec4782a06ac9a01767a323";
    HashSet<String> set;
    List<Article> favNews;
    ApiInterface apiServices;
    private RecyclerView recyclerView;

    String msg;
    String business = "business";
    String sports = "sports";
    String health = "health";
    String entertainment = "entertainment";
    String science = "science";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.fav_menu , menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.add_fav_menu:
                Intent intent = new Intent(showFavorites.this , AddFavorite.class);
                startActivity(intent);
                return true;

            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_favorites);

        recyclerView = (RecyclerView) findViewById(R.id.fav_news_recycler);
        set = new HashSet<>();
        favNews = new ArrayList<>();

        prefs = getSharedPreferences("SharedPreference", MODE_PRIVATE);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        apiServices = ApiClient.getClient().create(ApiInterface.class);

        getList();
        if (set.size() < 1){
            msg = "No favorite Selected";
        } else {
            showArrayList();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    displayNews();
                }
            },5);
        }
    }

    private void showArrayList() {
        ArrayList<String> list = new ArrayList<>(set);
        for (int i = 0; i< list.size(); i++){
            if (list.get(i).equals(business)){
                loadNews("in", business);
            }
            if (list.get(i).equals(sports)){
                loadNews("in",sports);
            }
            if (list.get(i).equals(health)){
                loadNews("in", health);
            }
            if (list.get(i).equals(science)){
                loadNews("in",science);
            }
            if (list.get(i).equals(entertainment)){
                loadNews("in",entertainment);
            }
        }
        Log.i("display" , "News");
    }

    private void loadNews(String country, String category) {
        Call<ResponseModel> call = apiServices.getIndianNews(country , category , API_KEY);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                assert response.body() != null;
                if (response.body().getStatus().equals("ok")) {
                    List<Article> dummyList = response.body().getArticles();
                    if (dummyList.size() > 0) {
                        favNews.addAll(dummyList);
                        Log.i("favList", favNews.toString());
                        displayNews();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void getList(){
        Gson gson = new Gson();
        String json = prefs.getString("key", "null");
        Type type = new TypeToken<HashSet<String>>() {}.getType();
        set = gson.fromJson(json,type);
        if (set == null){
            set = new HashSet<>();
        }
        Log.i("key" , set.toString());
    }

    void displayNews(){
        final MainArticleAdapter mainArticleAdapter = new MainArticleAdapter(favNews);
        Log.i("displayNews" , favNews.toString());
        mainArticleAdapter.setOnRecyclerViewItemClickListener(showFavorites.this);

        recyclerView.setAdapter(mainArticleAdapter);
    }

    @Override
    public void onItemClick(int adapterPosition, View view) {
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
}