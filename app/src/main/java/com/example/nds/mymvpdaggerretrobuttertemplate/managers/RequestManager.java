package com.example.nds.mymvpdaggerretrobuttertemplate.managers;

import javax.inject.Inject;

import dagger.Module;
import retrofit2.Retrofit;

@Module
public class RequestManager {

    private Retrofit mRetrofit;

    @Inject
    public RequestManager(Retrofit retrofit){
        mRetrofit = retrofit;
    }

}
