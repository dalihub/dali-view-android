DALi Android View
================

Pre-requisites
--------------
Clone this repo in dali folder where other dali repos are located, 
run build.sh first time to download and setup all dependencies.

Usage
--------------
Import this library as a module into your project. Note that you still need to 
build your DALi app with headers and package files from dali-env folder, setup for Android.
You can reuse dali-env for your application after the build.sh script is run.

Options
--------------

By default it builds for 64 bits.

$ TARGET=armeabi-v7a ./build.sh

can be used to build for 32 bits.

Note: a git clean must be done before building for a different target.