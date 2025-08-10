
package com.safe.eldershield.svc

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class ElderNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) { /* hook for live detection */ }
    override fun onNotificationRemoved(sbn: StatusBarNotification?) { }
}
