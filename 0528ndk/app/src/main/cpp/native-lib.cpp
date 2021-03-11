#include <jni.h>
#include <string>


extern "C"
JNIEXPORT void JNICALL
Java_sport_yuedong_com_myapplication_MyException_nativeThrowException(JNIEnv *env,
                                                                      jobject instance) {

    // TODO

}

extern "C"
JNIEXPORT jstring JNICALL
Java_sport_yuedong_com_myapplication_MyException_stringFromJNI(JNIEnv *env, jobject jobjectOther) {

    std::string hello = "22";

    std::string error = "error";

    jclass jclass = env->FindClass("sport/yuedong/com/myapplication/MyException");
    jmethodID mid = env->GetMethodID(jclass, "operation", "()I");
    jmethodID mid2 = env->GetMethodID(jclass, "<init>", "()V");
    jobject jobject1 = env->NewObject(jclass, mid2);
    env->CallIntMethod(jobject1, mid);
    jthrowable jthrowable1 = env->ExceptionOccurred();


    //c调用java的处理,可以做异常检查
    if (jthrowable1) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return env->NewStringUTF(error.c_str());
    }


    return env->NewStringUTF(hello.c_str());
}