package org.lixl.hadoop.lixlsource.metrics2.lib;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;

import java.util.Map;

@InterfaceAudience.Private
public class UniqueNames {
    static class Count {
        final String baseName;
        int value;

        Count(String name, int value) {
            baseName = name;
            this.value = value;
        }
    }

    static final Joiner joiner = Joiner.on('-');
    final Map<String, Count> map = Maps.newHashMap();

    public synchronized String uniqueName(String name) {
        Count c = map.get(name);
        if (c == null) {
            c = new Count(name, 0);
            map.put(name, c);
            return name;
        }
        if (!c.baseName.equals(name)) {
            c = new Count(name, 0);
        }
        do {
            String newName = joiner.join(name, ++c.value);
            Count c2 = map.get(newName);
            if (c2 == null) {
                map.put(newName, c);
                return newName;
            }
        } while (true);
    }

}
