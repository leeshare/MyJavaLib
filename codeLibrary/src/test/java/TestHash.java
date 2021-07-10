/**
 * Created by Administrator on 6/20/2018.
 */
public class TestHash {
    public static void main(String[] args){
        System.out.println("192.168.0.0:1111的哈希值:" + "192.168.0.0:1111".hashCode());
        System.out.println("192.168.0.1:1111的哈希值:" + "192.168.0.1:1111".hashCode());
        System.out.println("192.168.0.2:1111的哈希值:" + "192.168.0.2:1111".hashCode());
        System.out.println("192.168.0.3:1111的哈希值:" + "192.168.0.3:1111".hashCode());
        System.out.println("192.168.0.4:1111的哈希值:" + "192.168.0.4:1111".hashCode());

        String key = "book";
        System.out.println(key + " 的 my     HashCode = " + myHashCode(key));
        System.out.println(key + " 的 system HashCode = " + key.hashCode());
    }

    //@Override
    public static int myHashCode(String _value) {
        char[] value = _value.toCharArray();        //98 111 111 107
        int h = 0;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            //hash = h;
        }
        return h;
    }
}
