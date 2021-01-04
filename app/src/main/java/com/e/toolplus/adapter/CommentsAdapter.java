package com.e.toolplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplus.R;
import com.e.toolplus.beans.Comment;
import com.e.toolplus.databinding.CommentItemsForShowBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    Context context;
    ArrayList<Comment> list;
    public CommentsAdapter(Context context, ArrayList<Comment> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentItemsForShowBinding binding = CommentItemsForShowBinding.inflate(LayoutInflater.from(context));
        return new CommentsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        Comment comment = list.get(position);
        holder.binding.userName.setText(comment.getUserName());
        Picasso.get().load(comment.getUserImageUrl()).placeholder(R.drawable.logo_white).into(holder.binding.userProfile);
        if (comment.getRating() == 0){
            holder.binding.userRating.setVisibility(View.GONE);
        } else {
            holder.binding.userRating.setRating(comment.getRating());
        }
        if (comment.getComment() == null){
            holder.binding.comment.setVisibility(View.GONE);
        } else {
            holder.binding.comment.setText(comment.getComment());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder {
        CommentItemsForShowBinding binding;
        public CommentsViewHolder(CommentItemsForShowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
