#include <jni.h>
#include <stdio.h>
#include <algorithm>
#include <vector>
#include <opencv2/opencv.hpp>
#include <string>
#include <list>
#include <map>
#include "net.h"

#include "tongue.h"
#include <android/log.h>

#define thres_val 0.8//0.9
using namespace std;
using namespace cv;

typedef struct _tongue_handle
{
	int magic;
	ncnn::Net net;
} tongue_handle;

struct Object
{
	cv::Rect rec;
	int class_id;
	float prob;
};


static int detect_MobileNetSSD(ncnn::Net &squeezenet, const cv::Mat bgr, cv::Rect &rect)
{
	if(bgr.cols == 0 || bgr.rows == 0)
	{
		__android_log_print(ANDROID_LOG_DEBUG, "TONGUE",
							"error bgr.cols==0 && bgr.rows==0 @detect_MobileNetSSD");
		return -1;
	}
	//ncnn::Mat in = ncnn::Mat::from_pixels(bgr.data, ncnn::Mat::PIXEL_BGR2GRAY, bgr.cols, bgr.rows); //
	ncnn::Mat in = ncnn::Mat::from_pixels_resize(bgr.data, ncnn::Mat::PIXEL_BGR2GRAY, bgr.cols,
												 bgr.rows, 300, 300); //2.5M 320*180   1.5M 320*240
	const float mean_vals[1] = {127.5};//
	in.substract_mean_normalize(mean_vals, 0);

	ncnn::Extractor ex = squeezenet.create_extractor();
	ex.set_light_mode(true);
	ex.set_num_threads(3);
	ex.input("data", in);

	ncnn::Mat out;
	ex.extract("detection_out", out);

	int w = bgr.cols;
	int h = bgr.rows;
	for(int iw = 0; iw < out.h; iw++)
	{
		const float *values = out.row(iw);
		float xmin = values[2] * w;
		float ymin = values[3] * h;
		float xmax = values[4] * w;
		float ymax = values[5] * h;

		if(xmin < 0) xmin = 0;
		if(xmax > w) xmax = w;
		if(ymin < 0) ymin = 0;
		if(ymax > h) ymax = h;

		Object object;
		object.class_id = values[0];
		object.prob = values[1];
		if(object.prob > thres_val && (object.class_id == 1))
		{
			rect.x = xmin;
			rect.y = ymin;
			rect.width = xmax - xmin;
			rect.height = ymax - ymin;
		}

	}
	return 0;
}


TONGUE_HANDLE tongue_init(JNIEnv *env, const char *paramPath, const char *modelPath)
{
	int rc = 0;
	tongue_handle *handle = new tongue_handle();
//	 if(NULL == handle)
//	 return NULL;
	// rc = handle->net.load_param("./models/tongue_5classes_singlechannel.param");
	rc = handle->net.load_param(paramPath);//
	//读取.param，读取成功时rc=0 ,讀取.h 读取成功时rc!=0
	if(rc != 0)
	{
		//"load_person_param error"
		delete handle;
		return NULL;
	}
	// rc = handle->net.load_model("./models/tongue_5classes_singlechannel.bin");
	rc = handle->net.load_model(modelPath);
	if(rc != 0)
	{
		//load_person_model error
		delete handle;
		return NULL;
	}
	return (TONGUE_HANDLE) handle;
}

int tongue_uninit(TONGUE_HANDLE handle)
{
	tongue_handle *tg_handle = (tongue_handle *) handle;
	delete tg_handle;
	return 0;
}

int tongue_detect(TONGUE_HANDLE handle, cv::Mat img, cv::Rect &rect)
{
	tongue_handle *tg_handle = (tongue_handle *) handle;
	rect.x = 0;
	rect.y = 0;
	rect.width = 0;
	rect.height = 0;
	__android_log_print(ANDROID_LOG_DEBUG, "james", "******still alive : before(detect_MobileNetSSD)");
	// return detect_squzeeNetSSD(tg_handle->net, img, rect);//origin james changed
	return detect_MobileNetSSD(tg_handle->net, img, rect);
}
