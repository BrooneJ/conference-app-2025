// swift-tools-version: 6.1
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "Native",
    platforms: [
        .iOS(.v18),
        .macOS(.v15),
    ],
    products: [
        .library(
            name: "Root",
            targets: ["Root"]
        ),
    ],
    dependencies: [
        .package(path: "../Core"),
        .package(url: "https://github.com/pointfreeco/swift-dependencies.git", exact: "1.9.2"),
        .package(url: "https://github.com/SwiftGen/SwiftGenPlugin", from: "6.6.2"),
    ],
    targets: [
        .target(
            name: "Component",
            dependencies: [
                .target(name: "Extension"),
                .product(name: "Model", package: "Core"),
            ]
        ),
        .testTarget(
            name: "ComponentTests",
            dependencies: ["Component"]
        ),

        .target(
            name: "Extension",
            dependencies: [
                .product(name: "Model", package: "Core"),
            ]
        ),

        .target(
            name: "Root",
            dependencies: [
                .target(name: "HomeFeature"),
                .target(name: "AboutFeature"),
                .target(name: "ContributorFeature"),
                .target(name: "EventMapFeature"),
                .target(name: "FavoriteFeature"),
                .target(name: "SearchFeature"),
                .target(name: "KMPFramework"),
                .product(name: "UseCase", package: "Core"),
                .product(name: "Model", package: "Core"),
                .product(name: "Dependencies", package: "swift-dependencies"),
                .target(name: "Extension"),
            ]
        ),

        .featureTarget(name: "Home", dependencies: [
            .target(name: "TimetableDetailFeature"),
            .target(name: "SearchFeature"),
        ]),
        .featureTarget(name: "TimetableDetail"),
        .featureTarget(name: "About"),
        .featureTarget(name: "Contributor"),
        .featureTarget(name: "EventMap"),
        .featureTarget(name: "Favorite", dependencies: [
            .target(name: "HomeFeature"),
            .target(name: "TimetableDetailFeature"),
        ]),
        .featureTarget(name: "Search", dependencies: [
            .target(name: "HomeFeature"),
            .target(name: "TimetableDetailFeature"),
        ]),

        .target(
            name: "Theme",
            resources: [
                .process("Resources"),
                .process("swiftgen.yml"),
            ],
            plugins: [.plugin(name: "SwiftGenPlugin", package: "SwiftGenPlugin")]
        ),
        .binaryTarget(name: "KMPFramework", path: "Sources/KMPFramework/shared.xcframework"),
    ],
    swiftLanguageModes: [.v6]
)

extension Target {
    static func featureTarget(
        name: String,
        dependencies: [Target.Dependency] = []
    ) -> Target {
        .target(
            name: "\(name)Feature",
            dependencies: dependencies + [
                .product(name: "Presentation", package: "Core"),
                .product(name: "Model", package: "Core"),
                .target(name: "Component"),
                .target(name: "Extension"),
                .target(name: "Theme"),
            ],
            path: "Sources/Feature/\(name)",
            resources: [
                .process("Resources")
            ]
        )
    }
}
