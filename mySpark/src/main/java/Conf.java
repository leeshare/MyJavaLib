import org.apache.spark.SparkConf;
import org.apache.spark.util.Utils;
import scala.collection.Map;

public class Conf extends SparkConf {

    public void SetConf() {
        Map<String, String> systemProperties = Utils.getSystemProperties();
        systemProperties.iterator()
        while (systemProperties.iterator()) {

        }

    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setMaster("local").setAppName("wc");
    }
}
