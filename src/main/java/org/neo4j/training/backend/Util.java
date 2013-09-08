package org.neo4j.training.backend;

import org.neo4j.helpers.collection.IteratorUtil;

import java.util.Iterator;
import java.util.Random;

/**
 * @author mh
 * @since 30.05.12
 */
public class Util {
    private static final Random random = new Random();

    public static String randomId() {
        int value = Math.abs(random.nextInt()+100000);
        return toId(value);
    }

    public static String toId(int value) {
        StringBuilder result = new StringBuilder(10);
        do {
            int tmp = value % 36;
            final char c = (char) (tmp < 10 ? '0' + tmp : tmp - 10 + 'a');
            result.insert(0, c);
            value /= 36;
        } while (value > 0);
        return result.toString();
    }

    public static String join(Iterable iterable, String delim) {
        StringBuilder result=new StringBuilder();
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next == null) continue;
            result.append(next.toString());
            if (it.hasNext()) result.append(delim);
        }
        return result.toString();
    }
}
