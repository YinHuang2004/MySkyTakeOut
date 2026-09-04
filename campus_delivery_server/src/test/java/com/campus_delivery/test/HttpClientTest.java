package com.campus_delivery.test;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
@SpringBootTest
public class HttpClientTest {
    @Test
    public void testGet() throws IOException {
        //创建一个 默认的 HttpClient 客户端对象
        CloseableHttpClient aDefault = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("http://localhost:8080/user/shop/status");
        //执行 HTTP 请求(客户端发送 GET 请求到服务器并封装了服务器的响应)
        CloseableHttpResponse execute = aDefault.execute(httpGet);
        //获取状态行（包含协议版本、状态码、原因短语）和状态码
        int statusCode = execute.getStatusLine().getStatusCode();
        System.out.println("服务器返回的状态码为 "+statusCode);
        //获取 响应实体（即服务器返回的数据体）可能包含 JSON、HTML、文件流等数据
        //HttpEntity是一个容器,因为控制层返回json格式数据,所以装json数据
        HttpEntity entity = execute.getEntity();
        //读取 entity 中的所有内容并转为字符串（默认 UTF-8）
        String string = EntityUtils.toString(entity);
        System.out.println("服务器返回的数据为"+string);
        //关闭资源
        execute.close();
        aDefault.close();
    }



    /**
     * 测试通过httpclient发送POST方式的请求
     */
    @Test
    public void testPOST() throws Exception{

        CloseableHttpClient httpClient = HttpClients.createDefault();


        HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");
        //创建一个 JSON 对象（来自 fastjson 库）用于构建要发送的 JSON 格式数据
        JSONObject jsonObject = new JSONObject();
        //向json对象封装数据{"username":"admin", "password":"123456"}
        jsonObject.put("username","admin");
        jsonObject.put("password","123456");
        //创建一个 字符串实体，用于封装请求体
        //jsonObject.toString()：将 JSON 对象转为字符串
        //
        //StringEntity：将字符串作为 HTTP 请求体发送
        StringEntity entity = new StringEntity(jsonObject.toString());

        entity.setContentEncoding("utf-8");
        //设置实体的 内容类型为 JSON
        //
        //告诉服务器：我发送的是 JSON 格式数据
        //
        //对应 HTTP 头部的 Content-Type: application/json
        entity.setContentType("application/json");
        //将实体 绑定到 POST 请求对象上
        //
        //这样 POST 请求就有了请求体（Body）
        httpPost.setEntity(entity);


        CloseableHttpResponse response = httpClient.execute(httpPost);


        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("响应码为：" + statusCode);
        //获取相应实体
        HttpEntity entity1 = response.getEntity();
        String body = EntityUtils.toString(entity1);
        System.out.println("响应数据为：" + body);


        response.close();
        httpClient.close();
    }
}
