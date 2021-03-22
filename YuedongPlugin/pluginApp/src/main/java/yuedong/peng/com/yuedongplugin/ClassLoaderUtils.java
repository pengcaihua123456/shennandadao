package yuedong.peng.com.yuedongplugin;

import android.content.Context;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;


/**
 * 插件化：类加载
 */
class ClassLoaderUtils {


    public static void loadClass(Context context,String apkPath) {


        try {
            //获取dexElemntField,先拿到类，然后得到field,用于后面改变体统的值
            Class<?> dexpathListClass = Class.forName("dalvik.system.DexPathList");
            Field dexElemntField = dexpathListClass.getField("dexElements");
            dexElemntField.setAccessible(true);

            //宿主的DexPathList 对象
            Class<?> baseDexClassLoaderClass = Class.forName("dalvik.system.baseDexClassLoaderClass");
            Field pathListField = baseDexClassLoaderClass.getField("pathList");
            dexElemntField.setAccessible(true);

            //宿主的类加载器pathClassLoader对象
            ClassLoader pathClassLoader = context.getClassLoader();
            //宿主的DexPathList对象
            Object hostDexPathList = pathListField.get(pathClassLoader);//代表的含义

            //宿主的dexelemnts对象(数组也是一个object)
            Object[] hostDexElemnts = (Object[]) dexElemntField.get(hostDexPathList);


            /***
             * 获取插件数组
             */
            //插件的类加载器
            ClassLoader pluginClassLoader=new DexClassLoader(apkPath,context.getCacheDir().getAbsolutePath(),null,pathClassLoader);

            //插件的DexPathList对象
            Object pluginDexPathList = pathListField.get(pluginClassLoader);//代表的含义

            //插件的dexelemnts对象
            Object[] pluginDexElemnts = (Object[]) dexElemntField.get(pluginDexPathList);


            /***
             * 合并
             */
            Object[] dexElements= (Object[])Array.newInstance(pluginDexElemnts.getClass().getComponentType(),hostDexElemnts.length+pluginDexElemnts.length);
            System.arraycopy(hostDexElemnts,0,dexElements,0,hostDexElemnts.length);
            System.arraycopy(pluginDexElemnts,0,dexElements,hostDexElemnts.length,pluginDexElemnts.length);

            //重新赋值
            dexElemntField.set(hostDexPathList,dexElements);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }


    public void testPathClassLoad() {

        PathClassLoader pathClassLoader = new PathClassLoader("", ClassLoader.getSystemClassLoader());
        try {
            pathClassLoader.loadClass("");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

}