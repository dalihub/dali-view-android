DALi Android View
================

Pre-requisites
--------------
At least OpenJDK Version 17.
Clone this repo in dali folder where other dali repos are located,
run build.sh first time to download and setup all dependencies.

Usage
--------------
Import this library as a module into your project. Note that you still need to
build your DALi app with headers and package files from dali-env folder, setup for Android.
You can reuse dali-env for your application after the build.sh script is run.

Options
--------------

By default it builds for 32 bits.

$ TARGET=arm64-v8a ./build.sh

can be used to build for 64 bits.
