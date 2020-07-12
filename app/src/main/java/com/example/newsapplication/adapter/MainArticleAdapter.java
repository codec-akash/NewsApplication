package com.example.newsapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapplication.R;
import com.example.newsapplication.activities.MainActivity;
import com.example.newsapplication.model.Article;
import com.example.newsapplication.utlis.OnRecyclerViewItemClickListener;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class MainArticleAdapter extends RecyclerView.Adapter<MainArticleAdapter.ViewHolder> {

    private List<Article> articleArrayList;

    private Context context;

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public MainArticleAdapter(List<Article> articleArrayList){
        this.articleArrayList = articleArrayList;
    }

    @NonNull
    @Override
    public MainArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_main_article_adapter, viewGroup, false);
        context = viewGroup.getContext();
        return new MainArticleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainArticleAdapter.ViewHolder viewHolder, int position) {
        final Article articleModel = articleArrayList.get(position);

        if (!TextUtils.isEmpty(articleModel.getTitle())){
            viewHolder.titleText.setText(articleModel.getTitle());
        }

        String imageUrl = articleModel.getUrlToImage();
        if (imageUrl != null) {
            Log.i("Image", imageUrl);
            Picasso.with(context).load(imageUrl).into(viewHolder.articleImage);
            viewHolder.articleAdapterParentLinear.setTag(articleModel);
        }
    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText;
        private LinearLayout articleAdapterParentLinear;
        private ImageView articleImage;

        public ViewHolder(final View view) {
            super(view);

            titleText = view.findViewById(R.id.article_adapter_titleText);
            articleAdapterParentLinear = view.findViewById(R.id.article_adapter_ll_parent);
            articleImage = view.findViewById(R.id.article_adapter_image);

            articleAdapterParentLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRecyclerViewItemClickListener != null){
                        onRecyclerViewItemClickListener.onItemClick(getAdapterPosition() , v);
                    }
                }
            });
        }
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener){
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }
}
