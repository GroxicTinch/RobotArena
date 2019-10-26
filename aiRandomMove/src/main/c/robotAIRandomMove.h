#include <jni.h>
/* Header for class robotarena_RobotAIRandomMove */

#ifndef _Included_robotarena_RobotAIRandomMove
#define _Included_robotarena_RobotAIRandomMove
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     robotarena_RobotAIRandomMove
 * Method:    runAI
 * Signature: (Lrobotarena/RobotControl;)V
 */
JNIEXPORT void JNICALL Java_robotarena_RobotAIRandomMove_runAI(JNIEnv *, jobject, jobject);
void sleep(unsigned);

#ifdef __cplusplus
}
#endif
#endif
