package com.example.bookapi.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bookapi.R;
import com.example.bookapi.model.Book;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ImageViewHolder> {
    private ArrayList<Book> imageUrls;
    private Context context;

    public BookAdapter(Context context) {
        this.context = context;
        imageUrls = new ArrayList<>();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Book book = imageUrls.get(position);
        holder.nombreLibro.setText(book.ownerPrefs.getTitle());

        Glide.with(context)
                .load("https://timetonic.com"+book.ownerPrefs.getoCoverImg())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public void adicionarListaLibro(ArrayList<Book> listaBook) {
        imageUrls.addAll(listaBook);
        notifyDataSetChanged();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nombreLibro;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.FotoimageView);
            nombreLibro = itemView.findViewById(R.id.nombreLibro);
        }
    }
}
