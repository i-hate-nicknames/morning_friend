package com.domain.nvm.morningfriend;

import java.util.Date;

public interface RingingControls {
    void stopRinging();
    void stopAndRestartRinging(Date restartTime);
}
