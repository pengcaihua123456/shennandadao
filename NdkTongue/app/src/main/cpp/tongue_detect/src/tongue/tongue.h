#ifndef __TONGUE_H__
#define __TONGUE_H__

typedef void *TONGUE_HANDLE;

TONGUE_HANDLE tongue_init(JNIEnv *env,const char *paramPath, const char *modelPath);

int tongue_uninit(TONGUE_HANDLE handle);

int tongue_detect(TONGUE_HANDLE handle, cv::Mat img, cv::Rect &box);

#endif//__TONGUE_H__
