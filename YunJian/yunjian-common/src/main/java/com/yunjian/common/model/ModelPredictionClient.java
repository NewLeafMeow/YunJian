package com.yunjian.common.model;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ModelPredictionClient {

    private static final String MODEL_SERVER_URL = "http://localhost:5000/predict";

    public static String predictTextCategory(String text) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(MODEL_SERVER_URL);

            // 设置请求体为原始文本，并指定字符集为UTF-8
            StringEntity entity = new StringEntity(
                    text,
                    ContentType.create("text/plain", StandardCharsets.UTF_8));
            httpPost.setEntity(entity);

            // 发送请求
            ClassicHttpResponse response = client.execute(httpPost);

            // 确保以UTF-8编码读取响应内容
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            return result.toString(); // 返回预测结果字符串
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}