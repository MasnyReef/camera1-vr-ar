@echo off
"C:\\Users\\Kacper\\AppData\\Local\\Android\\Sdk\\cmake\\3.22.1\\bin\\cmake.exe" ^
  "-HC:\\Users\\Kacper\\Documents\\GitHub\\camera1-vr-ar\\openCV\\libcxx_helper" ^
  "-DCMAKE_SYSTEM_NAME=Android" ^
  "-DCMAKE_EXPORT_COMPILE_COMMANDS=ON" ^
  "-DCMAKE_SYSTEM_VERSION=24" ^
  "-DANDROID_PLATFORM=android-24" ^
  "-DANDROID_ABI=x86" ^
  "-DCMAKE_ANDROID_ARCH_ABI=x86" ^
  "-DANDROID_NDK=C:\\Users\\Kacper\\AppData\\Local\\Android\\Sdk\\ndk\\23.1.7779620" ^
  "-DCMAKE_ANDROID_NDK=C:\\Users\\Kacper\\AppData\\Local\\Android\\Sdk\\ndk\\23.1.7779620" ^
  "-DCMAKE_TOOLCHAIN_FILE=C:\\Users\\Kacper\\AppData\\Local\\Android\\Sdk\\ndk\\23.1.7779620\\build\\cmake\\android.toolchain.cmake" ^
  "-DCMAKE_MAKE_PROGRAM=C:\\Users\\Kacper\\AppData\\Local\\Android\\Sdk\\cmake\\3.22.1\\bin\\ninja.exe" ^
  "-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=C:\\Users\\Kacper\\Documents\\GitHub\\camera1-vr-ar\\openCV\\build\\intermediates\\cxx\\Debug\\5i631z66\\obj\\x86" ^
  "-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=C:\\Users\\Kacper\\Documents\\GitHub\\camera1-vr-ar\\openCV\\build\\intermediates\\cxx\\Debug\\5i631z66\\obj\\x86" ^
  "-DCMAKE_BUILD_TYPE=Debug" ^
  "-BC:\\Users\\Kacper\\Documents\\GitHub\\camera1-vr-ar\\openCV\\.cxx\\Debug\\5i631z66\\x86" ^
  -GNinja ^
  "-DANDROID_STL=c++_shared"
