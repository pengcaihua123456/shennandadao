#include <jni.h>
#include "android_native.h"

#include <string>
#include <android/log.h>
#include <android/bitmap.h>
#include <malloc.h>
#include <assert.h>
#include <time.h>
#include <vector>
#include <unistd.h>

#include "opencv2/opencv.hpp"
#include "tongue.h"

using namespace cv;
using namespace std;

static TONGUE_HANDLE tongue_handle = NULL;

//計算時間
long getCurrentTime()
{
	struct timeval tv;
	gettimeofday(&tv, NULL);
	return tv.tv_sec * 1000 + tv.tv_usec / 1000;
}

Mat convertTo3Channels(const Mat &binImg)
{
	Mat three_channel = Mat::zeros(binImg.rows, binImg.cols, CV_8UC3);
	vector<Mat> channels;
	for(int i = 0; i < 3; i++)
	{
		channels.push_back(binImg);
	}
	merge(channels, three_channel);
	return three_channel;
}


int processImage(cv::Mat src, TongueInfo &ti)
{
	//src > BGR
	cv::Rect rect;
	int rc;
	memset(&ti, 0, sizeof(ti));//清空先前存的結果
	rc = tongue_detect(tongue_handle, src, rect);
	if(rc != 0) return rc;
	ti.isTongue = rect.width > 0 ? true : false;
	if(rect.width > 0)
	{
		ti.togueLocation[0] = rect.x;//xmin
		ti.togueLocation[1] = rect.y; //ymin
		ti.togueLocation[2] = rect.width;//xmax
		ti.togueLocation[3] = rect.height;//ymax
	}
	return 0;
}

int CopyBehaviorInfoToJava(JNIEnv *env, jobject thiz, TongueInfo &ti, jobject info)
{
	jclass tongueInfoClass = env->GetObjectClass(info);
	//	jclass objectClass = env->FindClass(info);
	jfieldID id = env->GetFieldID(tongueInfoClass, "isTongue", "Z");
	env->SetBooleanField(info, id, ti.isTongue);

	jfloatArray togueLocationArrary = env->NewFloatArray(4);
	jfloat *togueLocation = env->GetFloatArrayElements(togueLocationArrary, NULL);
	memcpy(togueLocation, ti.togueLocation, sizeof(ti.togueLocation));
	env->ReleaseFloatArrayElements(togueLocationArrary, togueLocation, 0);

	id = env->GetFieldID(tongueInfoClass, "tongueLocation", "[F");
	env->SetObjectField(info, id, togueLocationArrary);

	return 0;
}

extern "C" JNIEXPORT jint
JNICALL
Java_com_yuedong_sport_health_tongue_detect_TongueDetect_tongueInit(
		JNIEnv *env, jobject thiz, jstring paramPath, jstring modelPath)
{
	// jni -> c
	const char *paramPathC = env->GetStringUTFChars(paramPath, NULL);
	const char *modelPathC = env->GetStringUTFChars(modelPath, NULL);
	tongue_handle = tongue_init(env,paramPathC,modelPathC);
	return tongue_handle == NULL ? -1 : 0;
//	if(NULL == (tongue_handle = tongue_init(env)))
//		return -1;
//	return 0;
}

extern "C" JNIEXPORT jint
JNICALL
Java_com_yuedong_sport_health_tongue_detect_TongueDetect_tongueUninit(JNIEnv *env, jobject thiz)
{
	if(NULL != tongue_handle)
		tongue_uninit(tongue_handle);
	tongue_handle = NULL;
	return 0;
}


extern "C" JNIEXPORT jint
JNICALL
Java_com_yuedong_sport_health_tongue_detect_TongueDetect_processRGB888(JNIEnv *env, jobject thiz,
																	   jbyteArray rgb888Data,
																	   jint width, jint height,
																	   jint flag, jobject info)
{
    jbyte *rgb888 = env->GetByteArrayElements(rgb888Data, 0);
//    cv::Mat imgSrc(height, width, CV_8UC3, (unsigned char *)rgb888);
	cv::Mat imgSrc(height, width, CV_8UC3, (unsigned char *)rgb888);


	cv::Mat imgDst;
    cv::cvtColor(imgSrc, imgDst, CV_RGB2BGR);

	__android_log_print(ANDROID_LOG_DEBUG, "Behavior", "******before processImage (ms)*******");

	TongueInfo ti;
	long processImage_time_start = getCurrentTime();
    int rc = processImage(imgDst, ti);//imgDst > BGR
	long processImage_time_end = getCurrentTime();

	__android_log_print(ANDROID_LOG_DEBUG, "Behavior", "******processImage time = %ld (ms)*******",processImage_time_end - processImage_time_start);
	__android_log_print(ANDROID_LOG_DEBUG, "james", "******tongue detected, isTongue:%d left = %f right = %f  width = %f  height = %f *******",
			ti.isTongue,ti.togueLocation[0],ti.togueLocation[1],ti.togueLocation[2],ti.togueLocation[3]);

    CopyBehaviorInfoToJava(env, thiz, ti, info);

    return 0;
}

extern "C" JNIEXPORT jint
JNICALL
Java_com_yuedong_sport_health_tongue_detect_TongueDetect_processYUV(JNIEnv *env, jobject thiz,
																	jbyteArray yuvData, jint width,
																	jint height, jobject info)
{
	jbyte *yuv = env->GetByteArrayElements(yuvData, 0);
	jsize size = env->GetArrayLength(yuvData);

	// jbyteArray_size = (int)size;
	cv::Mat imgSrc(height, width, CV_8UC1, (unsigned char *) yuv);
	cv::Mat imgDst = convertTo3Channels(imgSrc);
	//cv::cvtColor(imgSrc, imgDst, CV_YUV2BGR_NV12);
	//cv::cvtColor(imgSrc, imgDst, CV_YUV2BGR_NV21);
	//cv::cvtColor(imgSrc, imgDst, CV_YUV2BGR_I420);

	TongueInfo ti;

	long processImage_time_start = getCurrentTime();
	int rc = processImage(imgDst, ti);
	long processImage_time_end = getCurrentTime();
	__android_log_print(ANDROID_LOG_DEBUG, "james", "******processYUV processImage time = %ld (ms)*******",processImage_time_end - processImage_time_start);
	__android_log_print(ANDROID_LOG_DEBUG, "james", "******tongue detected, isTongue:%d left = %f right = %f  width = %f  height = %f *******",
						ti.isTongue,ti.togueLocation[0],ti.togueLocation[1],ti.togueLocation[2],ti.togueLocation[3]);
	CopyBehaviorInfoToJava(env, thiz, ti, info);

		__android_log_print(ANDROID_LOG_DEBUG, "NDK-peng", "******end***********************************",processImage_time_end - processImage_time_start);

	return 0;
}