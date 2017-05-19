/*
 * Copyright 2017 Sherchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sherchen.archi.dagger2_retrofit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;

/**
 * The description of use: the singleton class to do the application thing
 * <br />
 * Created by Sherchen on 2017/5/19.
 */
public class SystemInfo {

    private static SystemInfo s_Instance;
    private ToolHelper toolHelper;
    private SystemInfo() {
        toolHelper = new ToolHelper();
    }

    public static SystemInfo getInstance() {
        if (s_Instance == null) {
            s_Instance = new SystemInfo();
        }
        return s_Instance;
    }

    public void callOnDestroy() {
        s_Instance = null;
        toolHelper.callOnDestroy();
        toolHelper = null;
    }

    public void dismissDialog(Activity activity) {
        toolHelper.dismissDialog(activity);
    }

    public void showDialog(Activity activity, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        toolHelper.showDialog(activity, cancelable, cancelListener);
    }

    private class ToolHelper {

        ToolHelper() {
        }

        private void callOnDestroy() {
        }

        private ProgressDialog dialog;

        private void showDialog(Activity activity, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
            if (activity == null || activity.isFinishing()) return;
            dismissDialog(activity);
            dialog = ProgressDialog.show(
                    activity,
                    "Title",
                    "Message",
                    true,
                    cancelable,
                    cancelListener
            );
        }

        private void dismissDialog(Activity activity) {
//            if(activity == null || activity.isFinishing()) return;
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
    }
}
