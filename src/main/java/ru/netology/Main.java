package ru.netology;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static final String URL_CATS = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        // Создание обьекта запроса с произвольными заголовками
        HttpGet request = new HttpGet(URL_CATS);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        // Отправка запроса
        CloseableHttpResponse response = httpClient.execute(request);
        //Вывод полученных заголовков
        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
        //Преобразование json в список java объектов;
        ObjectMapper mapper = new ObjectMapper();
        List<Post> posts = mapper.readValue(
                response.getEntity().getContent(),
                new TypeReference<List<Post>>() {
                });
        //Вывод всего списка
        posts.stream()
                .forEach(System.out::println);
        System.out.println("*****************SeparatoR********************");
        //Вывод списка с учетом фильтра Api
        posts.stream()
                .filter(value -> value.getUpvotes() != null)
                .filter(value -> value.getUpvotesInt() > 0 )
                .forEach(System.out::println);
        response.close();
        httpClient.close();
    }
}
