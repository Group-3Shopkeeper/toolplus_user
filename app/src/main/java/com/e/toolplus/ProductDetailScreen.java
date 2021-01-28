package com.e.toolplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.e.toolplus.adapter.CommentsAdapter;
import com.e.toolplus.adapter.SliderAdapterExample;
import com.e.toolplus.api.CartService;
import com.e.toolplus.api.CommentService;
import com.e.toolplus.api.FavoriteService;
import com.e.toolplus.beans.Cart;
import com.e.toolplus.beans.Category;
import com.e.toolplus.beans.Comment;
import com.e.toolplus.beans.Favorite;
import com.e.toolplus.beans.Product;
import com.e.toolplus.beans.SliderItem;
import com.e.toolplus.databinding.ActivityProductDetailScreenBinding;
import com.e.toolplus.utility.InternetConnection;
import com.e.toolplus.utility.InternetIntentFilter;
import com.google.firebase.auth.FirebaseAuth;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailScreen extends AppCompatActivity {

    Product product;
    Category category;
    String userId;
    ArrayList<Cart> list;
    ArrayList<Favorite> favList;
    CommentsAdapter adapter;
    private SliderAdapterExample adapter1;
    ActivityProductDetailScreenBinding binding;
    int flag = 0;
    int flag2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailScreenBinding.inflate(LayoutInflater.from(ProductDetailScreen.this));
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        InternetConnection internetConnection = new InternetConnection();
        registerReceiver(internetConnection, InternetIntentFilter.getIntentFilter());

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Intent in = getIntent();
        product = (Product) in.getSerializableExtra("product");
        category = (Category) in.getSerializableExtra("category");

        binding.productDetailName.setText(product.getName());
        binding.productDetailStocks.setText("Stock Availability : " + product.getQtyInStock());
        binding.productDetailCategory.setText("Category : " + category.getCategoryName());
        binding.productDetailDescription.setText(product.getDescription());

        setRatingOnUi();

        if (product.getDiscount() < 1){
            binding.productMRP.setVisibility(View.GONE);
            binding.productDetailDiscount.setVisibility(View.GONE);
            binding.productDetailPrice.setText("Price : ₹ " + product.getPrice());
        } else {
            Long actualPrice = (product.getDiscount()*product.getPrice())/100;
            binding.productDetailPrice.setText("Price : ₹ "+(product.getPrice() - actualPrice));
            binding.productMRP.setText("MRP : ₹ "+product.getPrice());
            binding.productMRP.setPaintFlags(binding.productMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.productDetailDiscount.setText("Off : ("+product.getDiscount()+"%)");
        }
        adapter1 = new SliderAdapterExample(this);
        binding.iv.setSliderAdapter(adapter1);
        binding.iv.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.iv.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.iv.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.iv.setIndicatorSelectedColor(Color.BLUE);
        binding.iv.setIndicatorMargin(12);
        binding.iv.setIndicatorUnselectedColor(Color.GRAY);
        binding.iv.setScrollTimeInSec(3);
        binding.iv.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {

            }
        });

        renewItems(binding.getRoot());

        CommentService.CommentAPI commentAPI = CommentService.getCommentAPIInstance();
        Call<ArrayList<Comment>> commentList =commentAPI.getListOfComment(product.getProductId());
        commentList.enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                final ArrayList<Comment> commentArrayList = response.body();
                if (commentArrayList == null){
                    binding.ratingLl.setVisibility(View.GONE);
                    binding.tvRating.setVisibility(View.GONE);
                    binding.ratingBar.setVisibility(View.GONE);
                    binding.tvComment.setVisibility(View.GONE);
                    binding.rvComments.setVisibility(View.GONE);
                } else {
                    binding.tvComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            binding.rvComments.setVisibility(View.VISIBLE);
                            adapter = new CommentsAdapter(ProductDetailScreen.this,commentArrayList);
                            binding.rvComments.setAdapter(adapter);
                            binding.rvComments.setLayoutManager(new LinearLayoutManager(ProductDetailScreen.this));
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {

            }
        });


        CartService.CartAPI cartAPI = CartService.getCartAPIInstance();
        Call<ArrayList<Cart>> listCall = cartAPI.getCartList(userId);
        listCall.enqueue(new Callback<ArrayList<Cart>>() {
            @Override
            public void onResponse(Call<ArrayList<Cart>> call, Response<ArrayList<Cart>> response) {
                list = response.body();
                String pId = product.getProductId();
                for (Cart cart : list) {
                    if (pId.equals(cart.getProductId())) {
                        flag = 1;
                        binding.addToC.setText("Already Added");
                        binding.addToC.setCompoundDrawableTintList(ColorStateList.valueOf(Color.WHITE));
                        binding.addToC.setTextColor(Color.WHITE);
                        final int sdk = android.os.Build.VERSION.SDK_INT;
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            binding.btnProductDetailCart.setBackgroundDrawable(ContextCompat.getDrawable(ProductDetailScreen.this, R.drawable.already_added) );
                        } else {
                            binding.btnProductDetailCart.setBackground(ContextCompat.getDrawable(ProductDetailScreen.this, R.drawable.already_added));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {

            }
        });


        FavoriteService.FavoriteAPI favoriteAPI = FavoriteService.getFavoriteAPIInstance();
        Call<ArrayList<Favorite>> listCall2 = favoriteAPI.getFavoriteByCategory(userId, category.getCategoryId());
        listCall2.enqueue(new Callback<ArrayList<Favorite>>() {
            @Override
            public void onResponse(Call<ArrayList<Favorite>> call, Response<ArrayList<Favorite>> response) {
                favList = response.body();
                String pId = product.getProductId();
                try {
                    for (Favorite favorite : favList) {
                        if (pId.equals(favorite.getProductId())) {
                            flag2 = 1;
                            binding.imageFavoriteHeart.setImageResource(R.drawable.favorite_btn_filled);
                        }
                    }
                } catch (NullPointerException e) {

                }

            }

            @Override
            public void onFailure(Call<ArrayList<Favorite>> call, Throwable t) {

            }
        });


        binding.imageFavoriteHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag2 == 1) {
                    for (Favorite favorite : favList) {
                        if (product.getProductId().equals(favorite.getProductId())) {
                            FavoriteService.FavoriteAPI favoriteAPI = FavoriteService.getFavoriteAPIInstance();
                            Call<Favorite> fav = favoriteAPI.deleteFavorite(favorite.getFavoriteId());
                            fav.enqueue(new Callback<Favorite>() {
                                @Override
                                public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                                    if (response.isSuccessful()) {
                                        binding.imageFavoriteHeart.setImageResource(R.drawable.favourite_btn);
                                        flag2 = 0;
                                    }
                                }

                                @Override
                                public void onFailure(Call<Favorite> call, Throwable t) {

                                }
                            });
                        }

                        if (flag2 == 0)
                            break;
                    }
                }
                if (flag2 == 0) {
                    Favorite favorite = new Favorite();
                    favorite.setBrand(product.getBrand());
                    favorite.setCategoryId(product.getCategoryId());
                    favorite.setDescription(product.getDescription());
                    favorite.setImageUrl(product.getImageUrl());
                    favorite.setName(product.getName());
                    favorite.setPrice(product.getPrice());
                    favorite.setProductId(product.getProductId());
                    favorite.setShopKeeperId(product.getShopKeeperId());
                    favorite.setUserId(userId);

                    FavoriteService.FavoriteAPI api = FavoriteService.getFavoriteAPIInstance();
                    Call<Favorite> favoriteCall = api.saveProductInFavorite(favorite);
                    favoriteCall.enqueue(new Callback<Favorite>() {
                        @Override
                        public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                            if (response.isSuccessful()) {
                                flag2 = 1;
                                binding.imageFavoriteHeart.setImageResource(R.drawable.favorite_btn_filled);

                                FavoriteService.FavoriteAPI api1 = FavoriteService.getFavoriteAPIInstance();
                                Call<ArrayList<Favorite>> listCall = api1.getFavoriteByCategory(userId, category.getCategoryId());
                                listCall.enqueue(new Callback<ArrayList<Favorite>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<Favorite>> call, Response<ArrayList<Favorite>> response) {
                                        favList = response.body();
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<Favorite>> call, Throwable t) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<Favorite> call, Throwable t) {

                        }
                    });
                }
            }

        });

        binding.btnProductDetailCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag == 1) {
                    Toast.makeText(ProductDetailScreen.this, "Product Already Added", Toast.LENGTH_SHORT).show();
                } else {
                    Cart cart = new Cart();
                    cart.setBrand(product.getBrand());

                    cart.setCategoryId(category.getCategoryId());
                    cart.setUserId(userId);
                    cart.setShopKeeperId(product.getShopKeeperId());
                    cart.setQtyInStock(product.getQtyInStock());
                    cart.setDescription(product.getDescription());
                    cart.setImageUrl(product.getImageUrl());
                    cart.setName(product.getName());
                    if (product.getDiscount() < 1) {
                        cart.setPrice(product.getPrice());
                    }else {
                        Long actualPrice = (product.getDiscount()*product.getPrice())/100;
                        Long sendingPrice = product.getPrice() - actualPrice;
                        cart.setPrice(sendingPrice);
                    }
                    cart.setProductId(product.getProductId());

                    CartService.CartAPI cartAPI = CartService.getCartAPIInstance();
                    Call<Cart> cartCall = cartAPI.saveProductInCart(cart);
                    cartCall.enqueue(new Callback<Cart>() {
                        @Override
                        public void onResponse(Call<Cart> call, Response<Cart> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ProductDetailScreen.this, "Product Successfully Added In Cart", Toast.LENGTH_SHORT).show();
                                binding.addToC.setText("Added To Cart");
                                binding.addToC.setCompoundDrawableTintList(ColorStateList.valueOf(Color.WHITE));
                                binding.addToC.setTextColor(Color.WHITE);

                                final int sdk = android.os.Build.VERSION.SDK_INT;
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    binding.btnProductDetailCart.setBackgroundDrawable(ContextCompat.getDrawable(ProductDetailScreen.this, R.drawable.already_added) );
                                } else {
                                    binding.btnProductDetailCart.setBackground(ContextCompat.getDrawable(ProductDetailScreen.this, R.drawable.already_added));
                                }

                                flag = 1;
                            }
                        }

                        @Override
                        public void onFailure(Call<Cart> call, Throwable t) {

                        }
                    });
                }

            }
        });
    }

    private void setRatingOnUi() {
        CommentService.CommentAPI api = CommentService.getCommentAPIInstance();
        Call<ArrayList<Comment>> commentList = api.getListOfComment(product.getProductId());
        commentList.enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                ArrayList<Comment> list = response.body();
                if (list.size()==0){
                    binding.ratingBar.setRating(3);
                }
                calculateAverageRating(list);
            }

            @Override
            public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {

            }
        });
    }

    private void calculateAverageRating(ArrayList<Comment> list) {
        Long average, user1=0L, user2=0L, user3=0L, user4=0L, user5=0L;

        for (Comment comment : list){
            if (comment.getRating() == 5){
                user5++;
            }
            if (comment.getRating() == 4){
                user4++;
            }
            if (comment.getRating() == 3){
                user3++;
            }
            if (comment.getRating() == 2){
                user2++;
            }
            if (comment.getRating() == 1){
                user1++;
            }
            average = ((user1*1)+(user2*2)+(user3*3)+(user4*4)+(user5*5))/(user1+user2+user3+user4+user5);
            binding.ratingBar.setRating(average);
        }
    }

    public void renewItems(View view) {
        List<SliderItem> sliderItemList = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            SliderItem sliderItem = new SliderItem();
            if (i == 1) {
                sliderItem.setImageUrl(product.getImageUrl());
            } else if (i == 2) {
                sliderItem.setImageUrl(product.getSecondImageUrl());
            } else if (i == 3) {
                sliderItem.setImageUrl(product.getThirdImageurl());
            }
            sliderItemList.add(sliderItem);
        }
        adapter1.renewItems(sliderItemList);
    }
}