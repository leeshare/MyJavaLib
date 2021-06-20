
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Created by Administrator on 2/24/2018.
 * 根据百度动态返回的缩略图 json数据，从而下载 图片列表
 */
public class JsoupFromBaiduPic {
    static String searchKey = "冰与火之歌";
    static String targetPath = "D:/BaiduPic";       //图片保存的目标目录
    //浏览器中的url
    static String webUrl = "http://image.baidu.com/search/index?tn=baiduimage&ps=1&ct=201326592&lm=-1&cl=2&nc=1&ie=utf-8&word=%E4%B8%89%E4%BD%93";
    static String agent = "\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36\"";
    static String postUrl = "https://image.baidu.com/search/acjson";

    private static Connection  doGetData(String keyword, int page) throws IOException {
        Connection con = Jsoup.connect(postUrl);
        con.timeout(10000); // 10秒超时
        con.ignoreContentType(true);
        Map<String, String> map = new HashMap<String, String>();
        map.put("tn", "resultjson_com");
        map.put("ipn", "rj");
        map.put("ct", "201326592");
        map.put("is", "");
        map.put("fp", "result");
        map.put("queryWord", keyword);
        map.put("cl", "2");
        map.put("lm", "-1");
        map.put("ie", "utf-8");
        map.put("oe", "utf-8");
        map.put("adpicid", "");
        map.put("st", "");
        map.put("z", "");
        map.put("ic", "");
        map.put("word", keyword);
        map.put("s", "");
        map.put("se", "");
        map.put("tab", "");
        map.put("width", "");
        map.put("height", "");
        map.put("face", "");
        map.put("istype", "");
        map.put("qc", "");
        map.put("nc", "1");
        map.put("fr", "");
        //这里是每一页取的记录按30递增  30 60 90 120
        map.put("pn", Integer.toString(30 * page));
        map.put("rn", "30");
        map.put("gsm", "3c");
        map.put("1517365157597", "");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            //添加参数
            con.data(entry.getKey(), entry.getValue());
        }
        return con;
    }

    private static void download(String strUrl, String path) throws IOException{
        URL url = null;
        InputStream in = null;
        OutputStream out = null;
        byte[] buffer = new byte[8192];
        int bytesRead = 0;

        try {
            url = new URL(strUrl);
            in = url.openStream();
            out = new FileOutputStream(path);
            while((bytesRead = in.read(buffer, 0, 8192)) != -1){
                out.write(buffer, 0, bytesRead);
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            in.close();
            out.close();
        }
    }

    private static void doPost() throws IOException{
        int start = 10;
        for(int i = start; i <= start + 10; i++){
            Connection con = doGetData(searchKey, i);

            Connection.Response response = con.execute();
            String result = response.body();

            JSONObject js = JSONObject.fromObject(result);
            JSONArray dataList = js.getJSONArray("data");
            for(int j = 0; j < dataList.size(); j++){
                if(j >= 26){
                    int c = 0;
                }
                if(dataList.getJSONObject(j) == null || dataList.getJSONObject(j).get("thumbURL") == null){
                    break;
                }
                String imgSrc = dataList.getJSONObject(j).get("thumbURL").toString();

                String imgName = FilenameUtils.getName(imgSrc);

                if(imgName.indexOf(".") != -1){
                    if(imgName.indexOf("?") > -1){
                        imgName = imgName.substring(0, imgName.indexOf("?"));
                    }
                    String saveImagePath = targetPath + "/" + imgName;
                    System.out.println("图片抓取开始：");
                    try{
                        download(imgSrc, saveImagePath);
                    }catch(Exception e){
                        continue;
                    }
                    System.out.println("图片抓取结束：" + imgSrc + " 保存路径：" + saveImagePath);
                }
            }
        }
    }

    public static void doGet(){
        String url = "http://www.sina.com";
        /*String pattern = "src=\\\"(.+?)\\\"";

        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select("table[class=page_con]").select("tr");
            for(int i = 0; i < elements.size(); i++){
                //通过正则获取想要的内容
                elements.get(i).getElementsMatchingOwnText(pattern).text();
                //获取符合元素之间的文本
                elements.get(i).text();
                //获取符合元素之间的超链接
                elements.get(i).getElementsByTag("a").attr("href");
            }
        } catch(Exception e){
            e.printStackTrace();
        }*/

        try {
            Document doc = Jsoup.connect(url).userAgent(agent).get();

            Elements imgElements = doc.getElementsByTag("img");

            Set<String> imgSrcSet = new HashSet<String>();
            for(Element img : imgElements){
                String imgSrc = img.attr("abs:src");
                imgSrcSet.add(imgSrc);
            }
            System.out.println("图片总数：" + imgSrcSet.size());

            Iterator<String> i = imgSrcSet.iterator();
            while(i.hasNext()){
                String imgSrc = (String)i.next();
                String imgName = FilenameUtils.getName(imgSrc);

                if(imgName.indexOf(".") != -1){
                    if(imgName.indexOf("?") > -1){
                        imgName = imgName.substring(0, imgName.indexOf("?"));
                    }
                    String saveImagePath = targetPath + "/" + imgName;
                    System.out.println("图片抓取开始：");
                    download(imgSrc, saveImagePath);
                    System.out.println("图片抓取结束：" + imgSrc + " 保存路径：" + saveImagePath);
                }
            }

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File(targetPath);
        if(file.exists()){
            System.out.println("文件夹已存在");
        }else{
            file.mkdir();
            System.out.println("自动创建文件夹");
        }

        //doGet();
        doPost();
    }

}
