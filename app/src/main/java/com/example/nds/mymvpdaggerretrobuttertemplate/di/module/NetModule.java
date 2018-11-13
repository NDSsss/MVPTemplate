package com.example.nds.mymvpdaggerretrobuttertemplate.di.module;

import com.example.nds.mymvpdaggerretrobuttertemplate.App.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {

    @Provides
    @Singleton
    public OkHttpClient gethttpClietn() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            builder.connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(Constants.TIMEOUT, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(true);
//            builder.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request();
//                    request = request.newBuilder()
//                            .addHeader("Content-Type", "text/plain")
//                            .addHeader("Spec-Revision", "1408")
//                            .addHeader("Channel", "Mobile-bank-new")
//                            .build();
//                    Response response = chain.proceed(request);
//                    return response;
//                }
//            });
//        builder.addNetworkInterceptor(logging);
//            LogInterceptor netLogging = new LogInterceptor();
//            netLogging.setLevel(LogInterceptor.Level.BODY);
//            if (BuildConfig.DEBUG_) {
//                builder.addNetworkInterceptor(netLogging);
//            }

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setPrettyPrinting()
                .setVersion(1.0)
                .create();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(ErrorHandlingCallAdapterFactory.create())
//                .baseUrl(Urls.getBaseBpcApi(true))
                .client(okHttpClient)
                .build();
        return retrofit;
    }

}
