import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 8/22/2019.
 *
 *  (a)*    对a贪婪
 *  (a)+    对a不贪婪
 */
public class TestRegular {

    public static void main(String[] args){

        isBilateralExistSpace("  abc def ");
        isBilateralExistSpace("2abc def ");
        isBilateralExistSpace("3abc def");

        replaceBilateralSpace("  abc def ");

        main2();
    }

    /**
     * \s 空格
     * @param str
     */
    static void isBilateralExistSpace(String str){

        //String pattern = "^\\s*(.*?)\\s*$";   //替换用
        String pattern = "^\\s+(.*?)\\s+$"; //判断是否如此
        Pattern r = Pattern.compile(pattern);
        Boolean result = r.matcher(str).matches();
        System.out.println(str + (result ? "两端有空格" : "两端无空格"));

    }
    static void replaceBilateralSpace(String str){

        String result = str;
        result = result.replaceAll("^\\s*", "");
        result = result.replaceAll("\\s*$", "");
        System.out.println(str + "替换为：[" + result + "]");

    }


    public static void main2() {

        Count cInstance = new Count( );

        cInstance.accumulate(cInstance.getCount()).getCount();

        new Count( ).accumulate(cInstance.getCount());
    }

    public static class Count {
        volatile Integer count = 2018;

        public Count accumulate(Integer count) {

            System.out.println(++count);

            return this;

        }

        public Integer getCount( ) {

            System.out.println(++count);

            return count;

        }
    }

}

