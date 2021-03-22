package glidetest.yuedong.com.myapplication;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AspectjxMine {
    @Pointcut("execution(* glidetest.yuedong.com.myapplication.clickMe(..))")
    public void callMethod() {

    }

    @Around("callMethod()")//
    public void beforeMethodCall(ProceedingJoinPoint joinPoint) {
        try {
            Log.i("AspectjxMine", "callMethod before   ");

            joinPoint.proceed();

            Log.i("AspectjxMine", "callMethod after   ");

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
