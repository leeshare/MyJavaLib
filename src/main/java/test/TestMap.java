package test;

import java.util.*;

public class TestMap {

    public static void main(String[] args) {
        Map<String, Object> original = new HashMap<>();
        original.put("apple", "big apple");
        original.put("computer", "IBM");
        original.put("book", "The Lord of the Rings");
        original.put(null, "I'm null");
        System.out.println("第一个Map集合大小为" + original.size());

        Map<String, Object> newMap = new HashMap<>();
        newMap.put("apple2", "small apple");
        newMap.put("computer2", "Microsoft");
        newMap.put("book", "Game of Thrones");
        newMap.put(null, "I'm null too");
        System.out.println("第二个Map集合大小为" + newMap.size());

        original.putAll(newMap);
        System.out.println("整合后第一个Map大小为" + original.size());

        Iterator<Map.Entry<String, Object>> it = original.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            System.out.println(entry.getValue());
        }

        Set<String> used = Collections.synchronizedSet(new HashSet<String>());
        used.add("apple");
        used.add("book");
        used.add("apple");
        System.out.println("一共" + used.size() + "个");

    }


    private Map<String, ?> originals;

    public Map<String, Object> originals() {
        Map<String, Object> copy = new RecordingMap<>();
        copy.putAll(originals);
        return copy;
    }

    private class RecordingMap<V> extends HashMap<String, V> {
        private final String prefix;

        RecordingMap() {
            this("");
        }
        RecordingMap(String prefix) {
            this.prefix = prefix;
        }
        RecordingMap(Map<String, ? extends V> m) {
            this(m, "");
        }
        RecordingMap(Map<String, ? extends V> m, String prefix) {
            super(m);
            this.prefix = prefix;
        }

        @Override
        public V get(Object key){
            if(key instanceof String) {
                String keyWithPrefix;
                if(prefix.isEmpty()){
                    keyWithPrefix = (String)key;
                }else {
                    keyWithPrefix = prefix + (String) key;
                }
            }
            return super.get(key);
        }


    }
}
