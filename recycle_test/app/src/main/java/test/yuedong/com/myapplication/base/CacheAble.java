package test.yuedong.com.myapplication.base;

/**
 * Created by virl on 15/7/6.
 */
public interface CacheAble<T> {
    T update(T object);
    void prepareReuse();
}
