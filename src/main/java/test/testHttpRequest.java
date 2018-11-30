package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 11/21/2018.
 */
public class TestHttpRequest {
    // http://q.stock.sohu.com/hisHq?code=cn_000002&start=20181119&end=20181120

    public static void main(String[] args){
        //http://quote.eastmoney.com/stocklist.html
        String url = "http://q.stock.sohu.com/hisHq";
        String params = "code=cn_000002&start=20181119&end=20181120";
        //发送 GET 请求
        String s = TestHttpRequest.sendGet(url, params);
        System.out.println(s);
        //send Post Request
        //String sr = TestHttpRequest.sendPost("http://q.stock.sohu.com/bisHq", "code=cn_000002&start=20181118&end=20181119");
        //System.out.println(sr);
    }

    public static String sendGet(String url, String param){
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            //获取所有响应的头字段
            Map<String, List<String >> map = connection.getHeaderFields();
            for(String key: map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            //定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = in.readLine()) != null){
                result += line;
            }
        }catch(Exception e) {
            System.out.println("发送GET请求出现异常：" + e);
            e.printStackTrace();
        }finally {
            try{
                if(in != null){
                    in.close();
                }
            }catch(Exception e2){
                e2.printStackTrace();
            }
        }

        return result;
    }

    public static String sendPost(String url, String param){
        String result = "";
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //Post请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            //定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while((line = in.readLine()) != null){
                result += line;
            }
        } catch(Exception e) {
            System.out.println("发送POST请求出现异常：" + e);
            e.printStackTrace();
        } finally {
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

}
