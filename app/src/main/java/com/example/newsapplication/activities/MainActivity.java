package com.example.newsapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {

    public static final String API_KEY = BuildConfig.ApiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView mainRecyclerView = findViewById(R.id.activity_main_rv);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);

        mainRecyclerView.setLayoutManager(linearLayoutManager);

        final ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseModel> call = apiServices.getIndianNews("in","sports",API_KEY);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                assert response.body() != null;
                if (response.body().getStatus().equals("ok")){
                    List<Article> articleList = response.body().getArticles();
                    if (articleList.size() > 0){
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
                Log.i("error" , t.toString());
            }
        });
    }
    public void onItemClick(int position, View view){
        switch (view.getId()){
            case R.id.article_adapter_ll_parent:
                Article article = (Article) view.getTag();
                if (!TextUtils.isEmpty(article.getUrl())){
                    Log.i("url clicked" , article.getUrl());
                    Intent webView = new Intent(this, WebActivity.class);
                    webView.putExtra("url", article.getUrl());
                    Log.i("ListAkash","webView");
                    startActivity(webView);
                }
                break;
        }
    }

}