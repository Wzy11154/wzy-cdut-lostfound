package com.wzy.utils;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Component
public class BaiduCloudApi {
    public static final String API_KEY = "ETAKZbzUSfZd5iq9vfmncrT6";
    public static final String SECRET_KEY = "1bTRSvMFSkfOupmCsyvNcqt2nEWxDhSH";

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

//    public static void main(String []args) throws IOException{
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        String image = getFileContentAsBase64("F:\\idea_java_project\\BaiduCloudApiTest\\src\\main\\images\\IMG4.jpg", true);
//        // image 可以通过 getFileContentAsBase64("C:\fakepath\img.png") 方法获取,如果Content-Type是application/x-www-form-urlencoded时,第二个参数传true
//        RequestBody body = RequestBody.create(mediaType, String.format("image=%s", image));
//        Request request = new Request.Builder()
//                .url("https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general?access_token=" + getAccessToken())
//                .method("POST", body)
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .addHeader("Accept", "application/json")
//                .build();
//        Response response = HTTP_CLIENT.newCall(request).execute();
//        String image_info = response.body().string();
//        System.out.println(image_info);
//
//    }

    public static String getImageInfo(String path) throws IOException{
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String image = getFileContentAsBase64(path, true);
        // image 可以通过 getFileContentAsBase64("C:\fakepath\img.png") 方法获取,如果Content-Type是application/x-www-form-urlencoded时,第二个参数传true
        RequestBody body = RequestBody.create(mediaType, String.format("image=%s", image));
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String image_info = response.body().string();
        System.out.println(image_info);
        return image_info;
    }

    /**
     * 获取文件base64编码
     *
     * @param path      文件路径
     * @param urlEncode 如果Content-Type是application/x-www-form-urlencoded时,传true
     * @return base64编码信息，不带文件头
     * @throws IOException IO异常
     */
    public static String getFileContentAsBase64(String path, boolean urlEncode) throws IOException {
        byte[] b = Files.readAllBytes(Paths.get(path));
        String base64 = Base64.getEncoder().encodeToString(b);
        if (urlEncode) {
            base64 = URLEncoder.encode(base64, "utf-8");
        }
        return base64;
    }


    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    public static String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }

}