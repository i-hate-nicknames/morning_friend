package com.domain.nvm.morningfriend.alert;

public class RingingState {

    private static RingingState sState;

    private boolean isRinging;

    public static RingingState get() {
        if (sState == null) {
            sState = new RingingState();
        }
        return sState;
    }

    public boolean isRinging() {
        return isRinging;
    }

    public void setRinging(boolean ringing) {
        isRinging = ringing;
    }
}
