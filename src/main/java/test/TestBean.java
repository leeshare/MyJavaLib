package test;

/**
 * Created by Administrator on 7/4/2018.
 */
public class TestBean {
    private String testStr = "testStr";

    public String getTestStr(){
        return testStr;
    }
    public void setTestStr(String testStr){
        this.testStr = testStr;
    }
    public void test(){
        System.out.println("test");
    }
}
