package test;

/**
 * 比较 try 里面包含for 的性能 和
 * for 里面包含try 的性能 哪个高
 * <p>
 * 通过 javap -c 查看字节码，发现性能是一样的
 */
public class TryCatch {
    public static void main(String[] args) {
        tryFor();
        forTry();
    }

    public static void tryFor() {
        int j = 3;
        try {
            for (int i = 0; i < 1000; i++) {
                Math.sin(j);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void forTry() {
        int j = 3;
        for (int i = 0; i < 1000; i++) {
            try {
                Math.sin(j);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
