package com.cmput301f21t35.habitude;

import java.util.ArrayList;

public interface SharingListCallBack {
    void onCallbackSuccess(ArrayList<String> dataList);

    void onCallbackFailure(String reason);
}
