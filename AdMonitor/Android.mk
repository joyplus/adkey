LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_STATIC_JAVA_LIBRARIES := \
        Miaozhen \
        fasthttp \
 #       libs/android-support-v4 \

LOCAL_SRC_FILES := \
        $(call all-java-files-under, src)\
        src/com/joyplus/ad/config/adconfig.properties

LOCAL_MODULE := adboot
#LOCAL_MODULE_CLASS := JAVA_LIBRARIES

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES:=Miaozhen:libs/Miaozhen.jar\
                                      fasthttp:libs/fasthttp.jar
                                      #android-support-v4:libs/android-support-v4


include $(BUILD_STATIC_JAVA_LIBRARY)

# additionally, build tests in sub-folders in a separate .apk
# include $(call all-makefiles-under,$(LOCAL_PATH))

#include $(CLEAR_VARS)
#LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES:=Miaozhen:libs/Miaozhen.jar\
#                                      fasthttp:libs/fasthttp.jar
#                                      #android-support-v4:libs/android-support-v4
#include $(BUILD_MULTI_PREBUILT)
