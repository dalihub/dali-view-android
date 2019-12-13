if [ ! -d "$ANDROID_SDK" ]; then
if [ ! -d "$HOME/Android/Sdk" ]; then
mkdir -p "$HOME/Android/Sdk"
cd "$HOME/Android/Sdk"
wget https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip
unzip sdk-tools-linux-4333796.zip
tools/bin/sdkmanager --update
yes | tools/bin/sdkmanager "patcher;v4" "platform-tools" "platforms;android-29" "build-tools;29.0.2" "lldb;3.1" "cmake;3.10.2.4988404" "ndk-bundle" "ndk;20.1.5948944"
cd -
fi
fi

if [ ! -d "$ANDROID_SDK" ]; then
# try default path
if [ -d "$HOME/Android/Sdk" ]; then
export ANDROID_SDK=$HOME/Android/Sdk
fi
fi

if [ ! -d "$ANDROID_NDK" ]; then
if [ -d "$ANDROID_SDK" ]; then
NDK_DIR=$(find $ANDROID_SDK -maxdepth 2 -name ndk-build | sed 's/\/ndk-build//')
if [ -d "$NDK_DIR" ]; then
export ANDROID_NDK=$NDK_DIR
fi
fi
fi

if [ ! -d "$HOME/gradle/gradle-5.4.1" ]; then
mkdir -p $HOME/gradle
cd $HOME/gradle
wget https://services.gradle.org/distributions/gradle-5.4.1-bin.zip
unzip gradle-5.4.1-bin.zip
cd -
fi

export PATH=$PATH:$HOME/gradle/gradle-5.4.1/bin
echo 'sdk.dir='$(echo $ANDROID_SDK) > local.properties

if [ ! -d "$DALI_DIR" ]; then
export DALI_DIR=$(cd ..;pwd)
fi

if [ ! -d "$DALI_DIR/android-dependencies" ]; then
git clone https://github.com/dalihub/android-dependencies.git
fi

if [ ! -d "$DALI_DIR/dali-core" ]; then
git clone --branch devel/master ssh://review.tizen.org:29418/platform/core/uifw/dali-core
fi

if [ ! -d "$DALI_DIR/dali-adaptor" ]; then
git clone --branch devel/master ssh://review.tizen.org:29418/platform/core/uifw/dali-adaptor
fi

if [ ! -d "$DALI_DIR/dali-toolkit" ]; then
git clone --branch devel/master ssh://review.tizen.org:29418/platform/core/uifw/dali-toolkit
fi

gradle wrapper
if [ -z "$DEBUG" ]; then
./gradlew assembleRelease
else
./gradlew assembleDebug
fi

