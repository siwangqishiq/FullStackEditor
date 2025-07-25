#include <jni.h>
#include <string>

extern "C"{
#include "libavcodec/avcodec.h"
}

extern "C"
JNIEXPORT jstring JNICALL
Java_xyz_panyi_fullstackeditor_bridge_NativeBridge_ffmpegVersion(JNIEnv *env, jclass clazz) {
    unsigned version = avcodec_version();  // 获取版本号
    char info[100];
    snprintf(info, sizeof(info), "%u.%u.%u",
             (version >> 16) & 0xFF, (version >> 8) & 0xFF, version & 0xFF);
    return env->NewStringUTF(info);
}