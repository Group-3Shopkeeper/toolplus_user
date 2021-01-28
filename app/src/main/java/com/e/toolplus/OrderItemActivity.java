package com.e.toolplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.e.toolplus.adapter.HistoryOrderItemAdapter;
import com.e.toolplus.api.CommentService;
import com.e.toolplus.api.UserService;
import com.e.toolplus.beans.Comment;
import com.e.toolplus.beans.OrderItem;
import com.e.toolplus.beans.User;
import com.e.toolplus.databinding.ActivityOrderItemBinding;
import com.e.toolplus.databinding.ReveivThankBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderItemActivity extends AppCompatActivity {
    HistoryOrderItemAdapter adapter;
    ActivityOrderItemBinding binding;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderItemBinding.inflate(LayoutInflater.from(OrderItemActivity.this));
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        ArrayList<OrderItem> list = (ArrayList<OrderItem>) intent.getSerializableExtra("orderItems");

        adapter = new HistoryOrderItemAdapter(OrderItemActivity.this, list);
        binding.rvHistoryOrderItem.setAdapter(adapter);
        binding.rvHistoryOrderItem.setLayoutManager(new LinearLayoutManager(OrderItemActivity.this));

        adapter.setOnClickListener(new HistoryOrderItemAdapter.OnRecyclerCommentClickListener() {
            @Override
            public void onItemClick(final OrderItem orderItem, int position) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(OrderItemActivity.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_comment, null);

                final RatingBar ratingBar = bottomSheetView.findViewById(R.id.ratingBar);
                final EditText etComment = bottomSheetView.findViewById(R.id.etComment);

                RelativeLayout btnPostComment = bottomSheetView.findViewById(R.id.btnPostComment);

                btnPostComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog pd = new ProgressDialog(OrderItemActivity.this, R.style.Theme_MyDialog);
                        pd.setTitle("Saving");
                        pd.setMessage("Please wait");
                        pd.show();

                        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        final UserService.UserAPI api = UserService.getUserAPIInstance();
                        Call<User> userCall = api.getUserById(userId);
                        userCall.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                user = response.body();

                                Comment comment = new Comment();

                                Long timestamp = Calendar.getInstance().getTimeInMillis();

                                String review = etComment.getText().toString();
                                if (review.isEmpty()) {
                                    comment.setComment(null);
                                }
                                comment.setComment(review);
                                comment.setComment("" + review);
                                comment.setProductId(orderItem.getProductId());
                                comment.setRating((long) ratingBar.getRating());
                                comment.setShopKeeperId(orderItem.getShopKeeperId());
                                comment.setUserId(userId);
                                comment.setTimestamp(timestamp);
                                comment.setUserName(user.getName());
                                comment.setUserImageUrl(user.getImageUrl());

                                CommentService.CommentAPI commentAPI = CommentService.getCommentAPIInstance();
                                Call<Comment> call2 = commentAPI.postComment(comment);
                                call2.enqueue(new Callback<Comment>() {
                                    @Override
                                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                                        if (response.isSuccessful()) {
                                            pd.dismiss();
                                            bottomSheetDialog.dismiss();

                                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderItemActivity.this);

                                            ReveivThankBinding binding = ReveivThankBinding.inflate(LayoutInflater.from(OrderItemActivity.this));

                                            builder.setView(binding.getRoot());

                                            final AlertDialog alertDialog = builder.create();
                                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                            binding.cd.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    alertDialog.dismiss();
                                                    bottomSheetDialog.show();
                                                }
                                            });
                                            binding.done.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    alertDialog.dismiss();
                                                    bottomSheetDialog.dismiss();
                                                }
                                            });

                                            alertDialog.show();

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Comment> call, Throwable t) {
                                        pd.dismiss();
                                        Log.e("error", "==========>" + t);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }
}