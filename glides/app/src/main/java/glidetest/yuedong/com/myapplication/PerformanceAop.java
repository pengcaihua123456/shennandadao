package glidetest.yuedong.com.myapplication;


import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;

//@Aspect
public class PerformanceAop {


    @Around("call(**glidetest.yuedong.com.myapplication.*.*(..))")
    public void getTime(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String name = signature.toShortString();
        long time = System.currentTimeMillis();
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        Log.i("PerformanceAop", name + " cost " + (System.currentTimeMillis() - time));

    }
}
