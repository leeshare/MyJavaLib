public class TestRecursion {
    public static void main(String[] args) {
        System.out.println(facorial(6));
    }

    public static int facorial(Integer n) {
        if(n == 0) return 1;    //0! = 1  零的阶乘等于1
        return facorial(n - 1) * n;
    }
}
