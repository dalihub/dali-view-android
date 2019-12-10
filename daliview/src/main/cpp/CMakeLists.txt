cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fexceptions -frtti -w -Wall -std=gnu++11")

# DALi
get_filename_component(DALI_DIR "${CMAKE_CURRENT_SOURCE_DIR}/../../../../.." ABSOLUTE)

set(ENV{PKG_CONFIG_PATH}  "${DALI_DIR}/dali-env/android/lib/${ANDROID_ABI}/pkgconfig:$ENV{PKG_CONFIG_PATH}")
find_package(PkgConfig REQUIRED)

include_directories(${DALI_DIR}/dali-env/android/include)
include_directories(${DALI_DIR}/dali-env/android/include/dali)

if (${CMAKE_BUILD_TYPE} STREQUAL "Debug")
  set(ENABLE_TRACE ON)
else()
  set(ENABLE_TRACE OFF)
endif()

if (EXISTS "${DALI_DIR}/dali-env/android")
  pkg_check_modules(DALI_CORE dali-core REQUIRED NO_CMAKE_PATH NO_CMAKE_ENVIRONMENT_PATH)
  pkg_check_modules(DALI_ADAPTOR dali-adaptor REQUIRED NO_CMAKE_PATH NO_CMAKE_ENVIRONMENT_PATH)
  pkg_check_modules(DALI_TOOLKIT dali-toolkit NO_CMAKE_PATH NO_CMAKE_ENVIRONMENT_PATH)
else()
  message(WARNING "Clean Project after first gradle sync!")
endif()

add_library(daliview SHARED
        daliview.cpp)

set_target_properties(daliview
        PROPERTIES
        LIBRARY_OUTPUT_DIRECTORY "${DALI_DIR}/dali-env/android/lib/${ANDROID_ABI}")

# Include libraries needed for daliview lib
target_link_libraries(daliview
                      ${DALI_TOOLKIT_LDFLAGS}
                      ${DALI_ADAPTOR_LDFLAGS}
                      ${DALI_CORE_LDFLAGS}
                      android
                      EGL
                      GLESv3)

