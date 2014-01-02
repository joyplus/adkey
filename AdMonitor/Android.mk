LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_STATIC_JAVA_LIBRARIES := \
        Miaozhen \
        fasthttp \

LOCAL_SRC_FILES := \
        $(call all-java-files-under, src)\
        src/com/joyplus/ad/config/adconfig.properties

LOCAL_MODULE := admonitor
#LOCAL_MODULE_CLASS := JAVA_LIBRARIES

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES:=Miaozhen:libs/Miaozhen.jar\
                                      fasthttp:libs/fasthttp.jar
                                


include $(BUILD_STATIC_JAVA_LIBRARY)
