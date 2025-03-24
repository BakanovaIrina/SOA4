package com.example.testservice.adapter;

import com.example.testservice.exception.AppException;
import com.example.testservice.exception.AppRuntimeException;
import com.example.testservice.soap.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Component
@RequiredArgsConstructor
@Slf4j
@Service
public class EbayServiceAdapter {
    @Value("${base.endpoint}")
    private String productServiceUrl;

    private final HttpClient client = HttpClient.newHttpClient();
    private final JsonMapper<Product> cityMapper = new JsonMapper<>(Product.class);

    public List<Product> getProducts(String filters, int page, int size) throws AppException {
        URI uri = URI.create(productServiceUrl + "?" + filters + "&page=" + page + "&size=" + size);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .GET()
                .build();

        CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        CompletableFuture<List<Product>> result = null;

        try {
            result = futureResponse.handle((response, ex) -> {

                if (response.statusCode() == HttpStatus.BAD_REQUEST.value()) {
                    throw new AppRuntimeException(HttpStatus.BAD_REQUEST, response.body());
                }

                if(response.statusCode() == HttpStatus.OK.value()) {
                    return cityMapper.deserializeList(response.body());
                }

                return cityMapper.deserializeList(response.body());
            });
        }
        catch (CompletionException e) {
            catchCompletionException(e);
        }

        return result.join();
    }


    private void catchCompletionException(CompletionException e) throws AppException {
        try {
            throw e.getCause();
        }
        catch (AppRuntimeException appRuntimeException) {
            throw new AppException(appRuntimeException.getStatus(), appRuntimeException.getMessage());
        }
        catch(Throwable impossible) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }


    public List<Product> getProducts() throws AppException {
        URI uri = URI.create(productServiceUrl + "?" + "filter=owner%5Beq%5D%3D94" + "&page=1" + "&size=10");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .GET()
                .build();

        CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        CompletableFuture<List<Product>> result = null;

        try {
            result = futureResponse.handle((response, ex) -> {

                if (response.statusCode() == HttpStatus.BAD_REQUEST.value()) {
                    throw new AppRuntimeException(HttpStatus.BAD_REQUEST, response.body());
                }

                if(response.statusCode() == HttpStatus.OK.value()) {
                    return cityMapper.deserializeList(response.body());
                }

                return cityMapper.deserializeList(response.body());
            });
        }
        catch (CompletionException e) {
            catchCompletionException(e);
        }

        return result.join();
    }
}
