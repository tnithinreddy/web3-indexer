package org.web3.indexer.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
@Log4j2
public class BeanConfiguration {

    @Value("${node.endpoint}")
    private String NODE_ENDPOINT;
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.registerModule(new JavaTimeModule())
                .setDateFormat(new StdDateFormat())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    @Primary
    public Web3j web3Bean() {
        OkHttpClient.Builder clientBuilder = HttpService.getOkHttpClientBuilder();
        clientBuilder.interceptors().add(new OkHttpRetryInterceptor());
        Web3j web3 = Web3j.build(new HttpService(NODE_ENDPOINT, clientBuilder.build()));  // defaults to http://localhost:8545/
        return web3;
    }
}
