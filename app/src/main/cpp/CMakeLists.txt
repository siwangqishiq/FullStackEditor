cmake_minimum_required(VERSION 3.22.1)

project("fullstackeditor")

include_directories("include/libavcodec")
include_directories("include/libavdevice")
include_directories("include/libavfilter")
include_directories("include/libavformat")
include_directories("include/libavutil")
include_directories("include/libswresample")
include_directories("include/libswscale")

add_library(${CMAKE_PROJECT_NAME} SHARED
        # List C/C++ source files with relative paths to this CMakeLists.txt.
        native-lib.cpp)

# Specifies libraries CMake should link to your target library. You
# can link libraries from various origins, such as libraries defined in this
# build script, prebuilt third-party libraries, or Android system libraries.
target_link_libraries(${CMAKE_PROJECT_NAME}
        # List libraries link to the target library
        android
        log)