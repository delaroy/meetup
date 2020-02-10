package com.bamideleoguntuga.meetup.networking.api;

import com.bamideleoguntuga.meetup.model.Create;
import com.bamideleoguntuga.meetup.model.CreateResponse;
import com.bamideleoguntuga.meetup.model.Login;
import com.bamideleoguntuga.meetup.model.LoginResponse;
import com.bamideleoguntuga.meetup.model.users.Users;
import com.bamideleoguntuga.meetup.networking.Routes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    @POST(Routes.CREATE )
    Call<CreateResponse> create(@Body Create createUser);

    @POST(Routes.LOGIN )
    Call<LoginResponse> login(@Body Login loginUser);

    @GET(Routes.USERS)
    Call<Users> getUsers(@Query("page") int pageIndex);

}

