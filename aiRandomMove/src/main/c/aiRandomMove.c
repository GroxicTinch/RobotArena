#include <jni.h>
#include <stdlib.h>

#ifdef _WIN32
  #include <windows.h>
  void sleep(unsigned milliseconds) {
    Sleep(milliseconds);
  }
#else
  #include <unistd.h>
  void sleep(unsigned milliseconds) {
    // uses microseconds not miliseconds so adjust value
    usleep(milliseconds * 1000);
  }
#endif

JNIEXPORT void JNICALL Java_robotarena_RobotAIRandomMove_runAI(JNIEnv *env, jobject this,
                                                               jobject robotControl) {
  int myX;
  int myY;

  // Defining RobotControl
  jclass robotControl_C = (*env)->GetObjectClass(env, robotControl);
  jmethodID robotControl_getRobot = (*env)->GetMethodID(env, robotControl_C, "getRobot", "()Lrobotarena/RobotInfo;");
  jmethodID robotControl_getAllRobots = (*env)->GetMethodID(env, robotControl_C, "getAllRobots", "()[Lrobotarena/RobotInfo;");
  jmethodID robotControl_fire = (*env)->GetMethodID(env, robotControl_C, "fire", "(II)Z");
  jmethodID robotControl_moveNorth = (*env)->GetMethodID(env, robotControl_C, "moveNorth", "()Z");
  jmethodID robotControl_moveEast = (*env)->GetMethodID(env, robotControl_C, "moveEast", "()Z");
  jmethodID robotControl_moveSouth = (*env)->GetMethodID(env, robotControl_C, "moveSouth", "()Z");
  jmethodID robotControl_moveWest = (*env)->GetMethodID(env, robotControl_C, "moveWest", "()Z");

  // RobotInfo myRobot = robotControl.getRobot();
  jobject myRobot = (*env)->CallObjectMethod(env, robotControl, robotControl_getRobot);

  // Defining RobotInfo
  jclass robotInfo_C = (*env)->GetObjectClass(env, myRobot);
  jmethodID robotInfo_getName = (*env)->GetMethodID(env, robotInfo_C, "getName", "()Ljava/lang/String;");
  jmethodID robotInfo_isAlive = (*env)->GetMethodID(env, robotInfo_C, "isAlive", "()Z");
  jmethodID robotInfo_getX = (*env)->GetMethodID(env, robotInfo_C, "getX", "()I");
  jmethodID robotInfo_getY = (*env)->GetMethodID(env, robotInfo_C, "getY", "()I");

  while(1) {

    // robotArray[] = robotControl.getAllRobots();
    jobjectArray robotInfoArray = (*env)->CallObjectMethod(env, robotControl, robotControl_getAllRobots);

    int robotCount = (*env)->GetArrayLength(env, robotInfoArray);

    // Shoot at the first robot found that is in range
    int i;
    for(i = 0; i < robotCount; i++) {

      jobject robotInfo = (*env)->GetObjectArrayElement(env, robotInfoArray, i);

      jstring myName = (*env)->CallObjectMethod(env, myRobot, robotInfo_getName);
      jstring robotName = (*env)->CallObjectMethod(env, robotInfo, robotInfo_getName);

      // Convert to char array, compare and then release
      const char *myNameChars = (*env)->GetStringUTFChars(env, myName, 0);
      const char *robotNameChars = (*env)->GetStringUTFChars(env, robotName, 0);

      int isMe = strncmp(myNameChars, robotNameChars, strlen(myNameChars));

      (*env)->ReleaseStringUTFChars(env, myName, myNameChars);
      (*env)->ReleaseStringUTFChars(env, robotName, robotNameChars);

      // if isMe is 0 then myName and robotName is equal, value can be less or greater than 0
      if(isMe != 0) {
        // robot.isAlive();
        jboolean isAlive = (*env)->CallBooleanMethod(env, robotInfo, robotInfo_isAlive);
        if(isAlive == JNI_TRUE) {
          // robot.getX and robot.getY()
          myX = (*env)->CallIntMethod(env, robotInfo, robotInfo_getX);
          myY = (*env)->CallIntMethod(env, robotInfo, robotInfo_getY);

          int robotX = (*env)->CallIntMethod(env, robotInfo, robotInfo_getX);
          int robotY = (*env)->CallIntMethod(env, robotInfo, robotInfo_getX);

          if(abs(myX - robotX) <= 2 &&
             abs(myY - robotY) <= 2) {

            // robotControl.fire(myX, myY)
            (*env)->CallVoidMethod(env, robotControl, robotControl_fire,
                                   myX, myY);

            if((*env)->ExceptionCheck(env)){
              /*Clean up resources if necessary.*/
              return;
            }

            break;
          }
        }
      }
    }

    // Choose a random direction, if that fails then go in the next direction clockwise
    int dir = rand() % 4;

    int moved = 0;

    while(moved == 0) {
      switch(dir) {
        case 0:
          if((*env)->CallObjectMethod(env, robotControl, robotControl_moveNorth)) {
            moved = 1;
          } else {
            dir++;
          }
          break;
        case 1:
          if((*env)->CallObjectMethod(env, robotControl, robotControl_moveEast)) {
            moved = 1;
          } else {
            dir++;
          }
          break;
        case 2:
          if((*env)->CallObjectMethod(env, robotControl, robotControl_moveSouth)) {
            moved = 1;
          } else {
            dir++;
          }
          break;
        case 3:
          if((*env)->CallObjectMethod(env, robotControl, robotControl_moveWest)) {
            moved = 1;
          } else {
            dir = 0;
          }
          break;
      }

      if((*env)->ExceptionCheck(env)){
        /*Clean up resources if necessary.*/
        return;
      }
    }
    sleep(1000);
  }
}