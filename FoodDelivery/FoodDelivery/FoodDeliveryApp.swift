//
//  FoodDeliveryApp.swift
//  FoodDelivery
//
//  Created by Mark.Shavlovsky on 06.04.2026.
//

import shared
import SwiftUI

@main
struct FoodDeliveryApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) private var appDelegate

    init() {
        KoinKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ComposeView()
                .ignoresSafeArea()
        }
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let vc = AppIosKt.MainViewController()
        vc.view.insetsLayoutMarginsFromSafeArea = false
        vc.additionalSafeAreaInsets = .zero
        return vc
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
