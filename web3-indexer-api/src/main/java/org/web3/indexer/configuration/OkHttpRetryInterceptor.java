package org.web3.indexer.configuration;

import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/*
* OKHttp client is used to interact with the blockchain node. All other interactions happen only via messaging across systems.
* the okhttp client is configured to ensure we retry at least 3 times on every failure.
* back-off mechanisms and delays are not supported yet added. Can be added by plugging in spring retry or any equivalent library.
* */

@Log4j2
public class OkHttpRetryInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        // try the request
        log.info("Submitting request with URL {}", request.url());
        Response response = chain.proceed(request);
        int tryCount = 0;
        int maxLimit = 3;
        while (!response.isSuccessful() && tryCount < maxLimit) {
            log.info("Request failed to url {}, Retry attempt {} ", request.url(), tryCount);
            tryCount++;
            response = chain.proceed(request);
        }
        return response;
    }
}
