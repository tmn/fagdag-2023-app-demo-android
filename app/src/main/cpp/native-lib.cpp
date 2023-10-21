#include <jni.h>
#include <string>
#include "haversineFormula.h"

struct MyClass {
    jclass mylibclass;
} mMyClass;


/* Just a string form JNI */
jstring string_from_JNI(JNIEnv* env, jobject /* this */)
{
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

/* Calculating distance between x and y using haversine formula */
jdouble get_distance(JNIEnv *env, jobject, double start_lat, double start_lon, double end_lat, double end_lon)
{
    return Haversine::distance(
            {start_lat, start_lon },
            {end_lat,end_lon });
}


/// Boostrapping goes here

static const JNINativeMethod methods[] = {
        { "stringFromJNI", "()Ljava/lang/String;", reinterpret_cast<void*>(string_from_JNI)},
        {"getDistance", "(DDDD)D", reinterpret_cast<void*>(get_distance)}
};

extern "C" JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv* env;

    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    jclass c = env->FindClass("io/tmn/sanntidsappenfagdagdemoandroid/MyLibs");
    if (c == nullptr) return JNI_ERR;

    int rc = env->RegisterNatives(c, methods, sizeof(methods)/sizeof(JNINativeMethod));
    if (rc != JNI_OK) return rc;

    mMyClass.mylibclass = (jclass) env->NewGlobalRef(c);

    return JNI_VERSION_1_6;
}