cmake_minimum_required(VERSION 2.8.0)

project(tongueorSDK)

### SET CROSS-COMPILE ###
#set(CMAKE_SYSTEM_NAME Linux)
#set(CMAKE_BUILD_TYPE Release)
#生成的so文件目录
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../../../../jni-libs/armeabi)

#set(CMAKE_SYSTEM_NAME Android)
#set(CMAKE_SYSTEM_VERSION 21) # API level
#set(CMAKE_ANDROID_ARCH_ABI arm-v7a)
#set(CMAKE_ANDROID_NDK /Users/mac/Library/Android/sdk/ndk-bundle)
#set(CMAKE_ANDROID_STL_TYPE gnustl_static)
#set(CMAKE_MACOSX_RPATH 1)

#SET(CMAKE_C_COMPILER "/root/eabihf/gcc-linaro-4.9.4-2017.01-x86_64_arm-linux-gnueabihf/bin/arm-linux-gnueabihf-gcc")
#SET(CMAKE_CXX_COMPILER "/root/eabihf/gcc-linaro-4.9.4-2017.01-x86_64_arm-linux-gnueabihf/bin/arm-linux-gnueabihf-g++")

#SET(CMAKE_C_COMPILER "/usr/bin/gcc")
#SET(CMAKE_CXX_COMPILER "/usr/bin/gcc")
#set(CMAKE_C_COMPILER "/usr/local/Cellar/llvm/5.0.1/bin/clang")
#set(CMAKE_CXX_COMPILER "/usr/local/Cellar/llvm/5.0.1/bin/clang++")
#set(OPENMP_LIBRARIES "/usr/local/Cellar/llvm/5.0.1/lib")
#set(OPENMP_INCLUDES "/usr/local/Cellar/llvm/5.0.1/include")
#SET(CMAKE_FIND_ROOT_PATH "/root/eabihf/gcc-linaro-4.9.4-2017.01-x86_64_arm-linux-gnueabihf/")
#SET(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
#SET(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
#SET(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)

#set(TOOLCHAIN_ROOT /root/eabihf/gcc-linaro-4.9.4-2017.01-x86_64_arm-linux-gnueabihf)
#message("TOOLCHAIN_ROOT: ${TOOLCHAIN_ROOT}")
#set(CMAKE_C_COMPILER ${TOOLCHAIN_ROOT}/bin/arm-linux-gnueabihf-gcc)
#set(CMAKE_CXX_COMPILER ${TOOLCHAIN_ROOT}/bin/arm-linux-gnueabihf-g++)
#set(CMAKE_FIND_ROOT_PATH ${TOOLCHAIN_ROOT})
#set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
#set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
#set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
###

# sub CMakeLists.txt

# sources directories
aux_source_directory(./src SRCS_src)
aux_source_directory(./src/models SRCS_models)
aux_source_directory(./src/tongue SRCS_tongue)

# .h search directory
include_directories(src src/models src/tongue)
include_directories(3rdparty/ncnn/include)
include_directories(3rdparty/opencv340/include)

link_directories(${CMAKE_SOURCE_DIR}/3rdparty/ncnn/lib/${ANDROID_ABI})
link_directories(${CMAKE_SOURCE_DIR}/3rdparty/opencv340/lib/${ANDROID_ABI})

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11  -mfpu=neon ")

FIND_PACKAGE(OpenMP REQUIRED)
if(OpenMP_CXX_FOUND OR OPENMP_FOUND)
    set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${OpenMP_C_FLAGS}")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${OpenMP_CXX_FLAGS}")
    set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} ${OpenMP_EXE_LINKER_FLAGS}")
endif()

# main libdriverbehavior.so
add_library(tongueorSDK SHARED ${SRCS_src} ${SRCS_models} ${SRCS_tongue})
#target_link_libraries(tongue ncnn yuv opencv_java3 jnigraphics dl log)
target_link_libraries(tongueorSDK ncnn opencv_java3 jnigraphics dl log)
