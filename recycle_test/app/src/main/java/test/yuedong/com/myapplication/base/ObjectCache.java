package test.yuedong.com.myapplication.base;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by virl on 15/7/5.
 */
public class ObjectCache {
    private static HashMap<Class<?>, LinkedList<Object>> caches = new HashMap<>();
    public static void enqueue(Object object) {
        LinkedList<Object> list = cacheList(object.getClass());
        if(object instanceof CacheAble) {
            ((CacheAble)object).prepareReuse();
        }
        list.add(object);
    }

    public static void enqueue(Class cls, List objectList) {
        LinkedList<Object> list = cacheList(cls);
        for(Object obj: objectList) {
            if(obj instanceof CacheAble) {
                ((CacheAble)obj).prepareReuse();
            }
        }
        list.addAll(objectList);
    }

    public static Object deque(Class cls) {
        return cacheList(cls).pollLast();
    }

    private static LinkedList<Object> cacheList(Class cls) {
        LinkedList<Object> list = caches.get(cls);
        if(list == null) {
            list =  new LinkedList<>();
            caches.put(cls, list);
        }
        return list;
    }

    public static void releaseCls(Class cls) {
        caches.remove(cls);
    }
}
