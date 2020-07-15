package com.example.newsapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.newsapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AddFavorite extends AppCompatActivity {

    private CheckBox checkBoxSports;
    private CheckBox checkBoxHealth;
    private CheckBox checkBoxBusiness;
    private CheckBox checkboxEntertainment;
    private CheckBox checkBoxScience;

    String business = "business";
    String sports = "sports";
    String health = "health";
    String entertainment = "entertainment";
    String science = "science";

    private HashSet<String> favorites;
    private HashSet<String> categories;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite);

        checkBoxBusiness = (CheckBox) findViewById(R.id.checkboxBusiness);
        checkBoxHealth = (CheckBox) findViewById(R.id.checkboxHealth);
        checkBoxSports = (CheckBox) findViewById(R.id.checkboxSports);
        checkboxEntertainment = (CheckBox) findViewById(R.id.checkboxEntertainment);
        checkBoxScience = (CheckBox) findViewById(R.id.checkboxScience);

        categories = new HashSet<>();

        Button submit = (Button) findViewById(R.id.submit_fav);

        prefs = getSharedPreferences("SharedPreference", MODE_PRIVATE);

        addCategories();
        getList();

        checkboxticks(categories , favorites);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddFavorite.this, favorites.toString(), Toast.LENGTH_SHORT).show();
                addList("key" , favorites);
                Intent intent = new Intent(AddFavorite.this , showFavorites.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getList(){
        Gson gson = new Gson();
        String json = prefs.getString("key", "null");
        Type type = new TypeToken<HashSet<String>>() {}.getType();
        favorites = gson.fromJson(json,type);
        if (favorites == null){
            favorites = new HashSet<>();
        }
        Log.i("key" , favorites.toString());
    }

    public void addList(String key, HashSet<String> list){
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public void fav_add(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.checkboxBusiness:
                if (checked) {
                    favorites.add(business);
                } else {
                    favorites.remove(business);
                }
                break;
            case R.id.checkboxSports:
                if (checked) {
                    favorites.add(sports);
                } else {
                    favorites.remove(sports);
                }
                break;
            case R.id.checkboxHealth:
                Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
                if (checked){
                    favorites.add(health);
                    Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                } else {
                    favorites.remove(health);
                }
                break;
            case R.id.checkboxScience:
                if (checked){
                    favorites.add(science);
                    Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                } else {
                    favorites.remove(science);
                }
                break;
            case R.id.checkboxEntertainment:
                if (checked){
                    favorites.add(entertainment);
                    Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                } else {
                    favorites.remove(entertainment);
                }
                break;
        }
    }

    public void addCategories(){
        categories.add(health);
        categories.add(business);
        categories.add(sports);
        categories.add(entertainment);
        categories.add(science);
    }

    void checkboxticks(HashSet<String> c , HashSet<String> fav){
        List<String> list = new ArrayList<String>(c);
        List<String> list1 = new ArrayList<>(fav);
        if (fav != null){
            for (int i=0; i<list.size(); i++){
                for (int j=0; j<list1.size(); j++){
                    if (list.get(i).equals(list1.get(j))){
                        if (list1.get(j).equals(business)){
                            checkBoxBusiness.setChecked(true);
                        }
                        if (list1.get(j).equals(health)){
                            checkBoxHealth.setChecked(true);
                        }
                        if (list1.get(j).equals(sports)){
                            checkBoxSports.setChecked(true);
                        }
                        if (list1.get(j).equals(entertainment)){
                            checkboxEntertainment.setChecked(true);
                        }
                        if (list1.get(j).equals(science)){
                            checkBoxScience.setChecked(true);
                        }
                    }
                }
            }
        }
    }

    public void clearData(View view){
        checkBoxBusiness.setChecked(false);
        checkBoxSports.setChecked(false);
        checkBoxHealth.setChecked(false);
        checkBoxScience.setChecked(false);
        checkboxEntertainment.setChecked(false);
        favorites.clear();
        prefs.edit().clear().commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddFavorite.this , MainActivity.class);
        startActivity(intent);
        finish();
    }
}