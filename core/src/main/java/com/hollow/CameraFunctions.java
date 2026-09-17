package com.hollow;

public interface CameraFunctions {
    void shakeCamera(float intensity, float duration);
    void triggerHitStop(float duration);
    void triggerBlackOut(float duration);
    void resetLevel();
}
