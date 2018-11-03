package com.test.kani.songhistory.utility;

public interface FireStoreCallbackListener
{
    void doNext(boolean isSuccesful, Object obj);
    void occurError(int errorCode);
}
