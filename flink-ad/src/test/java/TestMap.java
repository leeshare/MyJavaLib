import java.util.*;

/**
 * Created by Administrator on 8/14/2019.
 */
public class TestMap {

    public static void main(String[] args){

        UseHashMap();
        UseHashTable();
        UseTreeMap();
        UseLinkedHashMap();
    }

    private static void UseHashMap(){
        Map<String, String> map = new HashMap<>();
        map.put("c", "ccc");
        map.put("d", "ddd");
        map.put("b", "bbb");
        map.put("a", "aaa");
        Iterator<String> iterator = map.keySet().iterator();
        while(iterator.hasNext()){
            Object key = iterator.next();
            System.out.println("hashMap.get(key) is : " + map.get(key));
        }
        System.out.println();
    }
    private static void UseHashTable(){
        Map<String, String> map = new Hashtable<>();
        map.put("d", "ddd");
        map.put("b", "bbb");
        map.put("a", "aaa");
        map.put("c", "ccc");
        Iterator<String> iterator = map.keySet().iterator();
        while(iterator.hasNext()){
            Object key = iterator.next();
            System.out.println("hashTable.get(key) is : " + map.get(key));
        }
        System.out.println();
    }
    private static void UseTreeMap(){
        Map<String, String> map = new TreeMap<>();
        map.put("d", "ddd");
        map.put("b", "bbb");
        map.put("a", "aaa");
        map.put("c", "ccc");
        Iterator<String> iterator = map.keySet().iterator();
        while(iterator.hasNext()){
            Object key = iterator.next();
            System.out.println("treeMap.get(key) is : " + map.get(key));
        }
        System.out.println();
    }
    private static void UseLinkedHashMap(){
        Map<String, String> map = new LinkedHashMap<>();
        map.put("d", "ddd");
        map.put("b", "bbb");
        map.put("a", "aaa");
        map.put("c", "ccc");
        Iterator<String> iterator = map.keySet().iterator();
        while(iterator.hasNext()){
            Object key = iterator.next();
            System.out.println("linkedHashMap.get(key) is : " + map.get(key));
        }
        System.out.println();
    }


}
