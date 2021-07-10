package flink.stream;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FileSource implements SourceFunction<String> {

    //文件路径
    public String filePath;

    public FileSource(String filePath) {
        this.filePath = filePath;
    }

    private InputStream inputStream;
    private BufferedReader reader;
    private Random random = new Random();

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        reader = new BufferedReader(new InputStreamReader(new
                FileInputStream(filePath)));
        String line = null;
        while ((line = reader.readLine()) != null) {
// 模拟发送数据
            TimeUnit.MILLISECONDS.sleep(random.nextInt(500));
// 发送数据
            ctx.collect(line);
        }
        if (reader != null) {
            reader.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Override
    public void cancel() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception e) {

        }
    }

    public static class Constants {
        public static final String
                ORDER_INFO1_PATH = "src/input/OrderInfo1.txt";
        public static final String
                ORDER_INFO2_PATH = "src/input/OrderInfo2.txt";
    }

    public static class OrderInfo1 {
        //订单ID
        private Long orderId;
        //商品名称
        private String productName;
        //价格
        private Double price;

        public OrderInfo1() {
        }

        public OrderInfo1(Long orderId, String productName, Double price) {
            this.orderId = orderId;
            this.productName = productName;
            this.price = price;
        }

        @Override
        public String toString() {
            return "OrderInfo1{" +
                    "orderId=" + orderId +
                    ", productName='" + productName + '\'' +
                    ", price=" + price +
                    '}';
        }

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public static OrderInfo1 string2OrderInfo1(String line) {
            OrderInfo1 orderInfo1 = new OrderInfo1();
            if (line != null && line.length() > 0) {
                String[] fields = line.split(",");
                orderInfo1.setOrderId(Long.parseLong(fields[0]));
                orderInfo1.setProductName(fields[1]);
                orderInfo1.setPrice(Double.parseDouble(fields[2]));
            }
            return orderInfo1;
        }
    }

    public static class OrderInfo2 {
        //订单ID
        private Long orderId;
        //下单时间
        private String orderDate;
        //下单地址
        private String address;

        public OrderInfo2() {
        }

        public OrderInfo2(Long orderId, String orderDate, String address) {
            this.orderId = orderId;
            this.orderDate = orderDate;
            this.address = address;
        }

        @Override
        public String toString() {
            return "OrderInfo2{" +
                    "orderId=" + orderId +
                    ", orderDate='" + orderDate + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public static OrderInfo2 string2OrderInfo2(String line) {
            OrderInfo2 orderInfo2 = new OrderInfo2();
            if (line != null && line.length() > 0) {
                String[] fields = line.split(",");
                orderInfo2.setOrderId(Long.parseLong(fields[0]));
                orderInfo2.setOrderDate(fields[1]);
                orderInfo2.setAddress(fields[2]);
            }
            return orderInfo2;
        }
    }


}
