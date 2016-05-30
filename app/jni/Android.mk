LOCAL_PATH		:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := JNI_ImageBlur
LOCAL_SRC_FILES := com_lhkj_blurdemo_jni_ImageBlur.cpp
LOCAL_LDLIBS    := -lm -llog -ljnigraphics

include $(BUILD_SHARED_LIBRARY)