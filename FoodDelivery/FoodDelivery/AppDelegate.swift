import FirebaseCore
import FirebaseMessaging
import shared
import SwiftUI
import UIKit
import UserNotifications

final class AppDelegate: NSObject, UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions _: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        FirebaseApp.configure()

        UNUserNotificationCenter.current().delegate = self
        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(options: authOptions) { _, _ in }

        application.registerForRemoteNotifications()
        FirebaseApp.configure()

        Messaging.messaging().delegate = self

        return true
    }

    func application(
        _: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        print("Registered for Apple Remote Notifications")
        Messaging.messaging().apnsToken = deviceToken
    }

    func application(
        _: UIApplication,
        didFailToRegisterForRemoteNotificationsWithError error: Error
    ) {
        print("Failed to register for remote notifications: \(error)")
    }

    /// Вызывается для фоновых и «тихих» пушей (content-available), когда баннер может не показаться.
    func application(
        _: UIApplication,
        didReceiveRemoteNotification userInfo: [AnyHashable: Any],
        fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void
    ) {
        print("[Push] didReceiveRemoteNotification (background/silent): \(userInfo)")
        completionHandler(.newData)
    }
}

extension AppDelegate: UNUserNotificationCenterDelegate {
    func userNotificationCenter(
        _: UNUserNotificationCenter,
        willPresent notification: UNNotification
    ) async -> UNNotificationPresentationOptions {
        let content = notification.request.content
        print("[Push] willPresent — приложение на экране, уведомление дошло")
        print("[Push] title=\(content.title) body=\(content.body)")
        print("[Push] userInfo=\(content.userInfo)")
        return [.banner, .sound, .badge]
    }

    func userNotificationCenter(
        _: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse
    ) async {
        let content = response.notification.request.content
        print("[Push] didReceive (tap по уведомлению)")
        print("[Push] actionIdentifier=\(response.actionIdentifier)")
        print("[Push] title=\(content.title) body=\(content.body)")
        print("[Push] userInfo=\(content.userInfo)")
    }
}

extension AppDelegate: MessagingDelegate {
    func messaging(_: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        print("AppDelegate" + (fcmToken ?? ""))
        
        guard let fcmToken else {
            IosNotificationBridge.shared.refreshNotificationToken()
            return
        }

        IosNotificationBridge.shared.updateNotificationToken(notificationToken: fcmToken)
    }
}
