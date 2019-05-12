//package com.vbee.springbootmongodbnewspapersrestapi.ulti;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//public class UploadImageStore {
//	private static String SERVER_UPLOAD_URL = "http://43.239.223.142:8888/upload.php";
//	private static String STORE_SUFFIX = "http://43.239.223.142:8888/articles/images/";
//	public static String uploadFile(byte[] imageInByte) {
//        try {
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpPost httpPost = new HttpPost(server_url);
//            File tempFile = File.createTempFile("article_", ".jpg");
//            FileOutputStream fos = new FileOutputStream(tempFile);
//            fos.write(imageInByte);
//            FileBody fileBody = new FileBody(tempFile);
//            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//            reqEntity.addPart("audio", fileBody);
//
//            reqEntity.addPart("dir", new StringBody("articles/images/"));
//            httpPost.setEntity(reqEntity);
//            HttpResponse response = httpClient.execute(httpPost);
//            HttpEntity resEntity = response.getEntity();
//            if (resEntity != null) {
//                String responseStr = EntityUtils.toString(resEntity).trim();
//                if( responseStr.equals("upload file complete")) {
//                	return STORE_SUFFIX + tempFile.getName();
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return "error";
//    }
//}
