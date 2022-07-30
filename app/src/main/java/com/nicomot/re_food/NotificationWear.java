package com.nicomot.re_food;

import android.app.PendingIntent;
import android.os.Bundle;

import androidx.core.app.RemoteInput;

import java.util.List;

public class NotificationWear {
    String packageName;
    PendingIntent pendingIntent;
    List<RemoteInput> remoteInputs;
    Bundle bundle;
    String tag;
    String id;

    public NotificationWear(String packageName, PendingIntent pendingIntent, List<RemoteInput> remoteInputs, Bundle bundle, String tag, String id) {
        this.packageName = packageName;
        this.pendingIntent = pendingIntent;
        this.remoteInputs = remoteInputs;
        this.bundle = bundle;
        this.tag = tag;
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }

    public List<RemoteInput> getRemoteInputs() {
        return remoteInputs;
    }

    public void setRemoteInputs(List<RemoteInput> remoteInputs) {
        this.remoteInputs = remoteInputs;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
