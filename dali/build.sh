#!/bin/bash
if [ -z "$ANDROID_SDK" ]; then
  . ./env.sh
fi

if [ ! -d "$ANDROID_SDK" ]; then
  echo "Please install Android SDK into "$ANDROID_SDK
  exit 1
fi

if [ ! -d "$ANDROID_NDK" ]; then
  echo "Please install Android NDK into "$ANDROID_NDK
  exit 1
fi

if [ "$1" = "clean" ]; then
echo "Cleaning dali..."
rm -rf ./dali-core
rm -rf ./dali-adaptor
rm -rf ./dali-toolkit
exit 0
fi

if [ ! -z "$DEBUG" ]; then
export ENABLE_TRACE=ON
fi

ANDROID_PLATFORM=26 ANDROID_ABI=armeabi-v7a ./build_core.sh
ANDROID_PLATFORM=26 ANDROID_ABI=armeabi-v7a ./build_adaptor.sh
ANDROID_PLATFORM=26 ANDROID_ABI=armeabi-v7a ./build_toolkit.sh

