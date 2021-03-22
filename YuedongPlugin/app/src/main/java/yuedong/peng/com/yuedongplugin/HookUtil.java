package yuedong.peng.com.yuedongplugin;

import android.content.Intent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HookUtil {


    public static final String TARGET_INTENT = "targe_intent";


    /***
     * 通过hook，把插件intent，换成宿主intent。进行检查
     */
    public static void hookAMS() {


        try {
            //通过当前线程获取classLoader
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            //通过反射获取ActivityManagerClass类
            Class<?> IActivityManagerClass = Class.forName("android.app.IActivityManager");
            InvocationHandler invocationHandler = new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    /***
                     *    需要hook的方法：
                     *    int result = ActivityManager.getService()
                     *                 .startActivity(whoThread, who.getBasePackageName(), intent,
                     *                         intent.resolveTypeIfNeeded(who.getContentResolver()),
                     *                         token, target != null ? target.mEmbeddedID : null,
                     *                         requestCode, 0, null, options);
                     */

                    //只hook startActivity方法
                    if ("startActivity".equals(method.getName())) {

                        int index = 0;
                        //找到intent是第几个参数
                        for (int i = 0; i < args.length; i++) {
                            if (args[i] instanceof Intent) {
                                index = i;
                                break;
                            }
                        }

                        Intent intent = (Intent) args[index];//得到plugin的intent

                        //替换成宿主的intent
                        Intent proxyIntent = new Intent();
                        proxyIntent.setClassName("yuedong.peng.com.yuedongplugin", ProxyActivity.class.getName());
                        proxyIntent.putExtra(TARGET_INTENT, intent);//存放起来，后面还原的时候调用
                        args[index] = proxyIntent;

                    }

                    //获取Singleton对象
                    Class<?> clazz=Class.forName("android.app.ActivityManager");
                    Field fieldSingleton=clazz.getDeclaredField("IActivityManagerSingleton");
                    fieldSingleton.setAccessible(true);
                    Object singleton=fieldSingleton.get(null);



                    //获取Singleton对象
                    Class<?> singleteonClass=Class.forName("android.util.Singleton");
                    Field field=singleteonClass.getDeclaredField("mInstance");
                    field.setAccessible(true);


                    Object activityManagerObject = null;


                    return method.invoke(activityManagerObject, args);//调用startActivity
                }
            };

            /***
             * 动态代理，修改方法
             */
            Proxy.newProxyInstance(loader, new Class[]{IActivityManagerClass}, invocationHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
