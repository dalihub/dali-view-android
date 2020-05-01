cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fexceptions -frtti -w -Wall -std=gnu++11")

# DALi
set(DALI_ENV_DIR $ENV{DALI_ENV_DIR})
set(ENV{PKG_CONFIG_PATH} "${DALI_ENV_DIR}/lib/${ANDROID_ABI}/pkgconfig:$ENV{PKG_CONFIG_PATH}")
set(ENV{PKG_CONFIG_PATH} "${DALI_ENV_DIR}/lib/${ANDROID_ABI}/pkgconfig:$ENV{PKG_CONFIG_PATH}")
find_package(PkgConfig REQUIRED)

include_directories(${DALI_ENV_DIR}/include)
include_directories(${DALI_ENV_DIR}/include/dali)

if (${CMAKE_BUILD_TYPE} STREQUAL "Debug")
  set(ENABLE_TRACE ON)
else()
  set(ENABLE_TRACE OFF)
endif()

if (EXISTS "${DALI_ENV_DIR}")
  pkg_check_modules(DALI_VERSION_TEST QUIET dali2-core dali2-adaptor dali2-toolkit)
  if(DALI_VERSION_TEST_FOUND)
    pkg_check_modules(DALI_CORE dali2-core REQUIRED NO_CMAKE_PATH NO_CMAKE_ENVIRONMENT_PATH)
    pkg_check_modules(DALI_ADAPTOR dali2-adaptor REQUIRED NO_CMAKE_PATH NO_CMAKE_ENVIRONMENT_PATH)
    pkg_check_modules(DALI_TOOLKIT dali2-toolkit REQUIRED NO_CMAKE_PATH NO_CMAKE_ENVIRONMENT_PATH)
  else()
    pkg_check_modules(DALI_CORE dali-core REQUIRED NO_CMAKE_PATH NO_CMAKE_ENVIRONMENT_PATH)
    pkg_check_modules(DALI_ADAPTOR dali-adaptor REQUIRED NO_CMAKE_PATH NO_CMAKE_ENVIRONMENT_PATH)
    pkg_check_modules(DALI_TOOLKIT dali-toolkit REQUIRED NO_CMAKE_PATH NO_CMAKE_ENVIRONMENT_PATH)
  endif()
else()
  message(WARNING "${DALI_ENV_DIR} not found!")
endif()

add_library(daliview SHARED
        daliview.cpp)

set_target_properties(daliview
        PROPERTIES
        LIBRARY_OUTPUT_DIRECTORY "${DALI_ENV_DIR}/lib/${ANDROID_ABI}")

# Include libraries needed for daliview lib
target_link_libraries(daliview
                      ${DALI_TOOLKIT_LDFLAGS}
                      ${DALI_ADAPTOR_LDFLAGS}
                      ${DALI_CORE_LDFLAGS}
                      android
                      EGL
                      GLESv3)

