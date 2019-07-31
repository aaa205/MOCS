package com.mocs.main.model;

import com.mocs.common.bean.User;

public interface OnUserListener {
    void onGetQQInfoSuccess(User user);
    void onGetQQInfoError(String msg);
}
