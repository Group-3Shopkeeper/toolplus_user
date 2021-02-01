package com.e.toolplus.api;

import com.e.toolplus.beans.Comment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class CommentService {
    public static CommentAPI commentAPI;

    public static CommentAPI getCommentAPIInstance(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerAddress.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (commentAPI == null)
            commentAPI = retrofit.create(CommentService.CommentAPI.class);
        return commentAPI;
    }

    public interface CommentAPI{

        @POST("comment/")
        Call<Comment> postComment(@Body Comment comment);

        @GET("comment/{productId}")
        Call<ArrayList<Comment>> getListOfComment(@Path("productId") String productId);

        @GET("comment/{userId}/{productId}")
        Call<Comment> getUserCommentOnParticularProduct(@Path("userId") String userId, @Path("productId") String productId);

        @POST("comment/update/")
        Call<Comment> updateComment(@Body Comment comment);
    }
}
