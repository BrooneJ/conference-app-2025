import Dependencies
import DependencyExtra
import SwiftUI
import Theme

public struct EventMapScreen: View {
    @State private var presenter = EventMapPresenter()
    @State private var selectedFloorMap: FloorMap? = .first
    @Dependency(\.safari) var safari

    public init() {}

    public var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                // Description
                Text("DroidKaigiでは、セッション以外にも参加者が楽しめるイベントを開催。コミュニケーションや技術交流を通じてカンファレンスを満喫しましょう！")
                    .font(Typography.bodyMedium)
                    .foregroundColor(AssetColors.onSurfaceVariant.swiftUIColor)
                    .padding(.horizontal, 16)
                    .padding(.vertical, 10)
                // Floor selector
                FloorMapSelector(selected: $selectedFloorMap)
                    .onChange(of: selectedFloorMap) { _, newValue in
                        if let floor = newValue {
                            presenter.selectFloorMap(floor)
                        }
                    }

                // Map image
                if selectedFloorMap != nil {
                    // TODO: Replace with actual floor map images
                    Image(presenter.selectedFloorMap.image, bundle: .module)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(maxWidth: .infinity)
                        .padding(.all, 16)
                }

                // Events section
                VStack(alignment: .leading, spacing: 0) {
                    ForEach(presenter.eventMap.events, id: \.id) { event in
                        EventItem(event: event) { url in
                            Task {
                                await safari(url)
                            }
                        }
                        if event.id != presenter.eventMap.events.last?.id {
                            Divider()
                                .padding(.horizontal, 16)
                                .foregroundStyle(AssetColors.outlineVariant.swiftUIColor)
                        }
                    }
                }
                .padding(.bottom, 80)  // Tab bar padding
            }
        }
        .background(AssetColors.background.swiftUIColor)
        .navigationTitle("イベントマップ")
        #if os(iOS)
            .navigationBarTitleDisplayMode(.large)
        #endif
        .task {
            await presenter.loadInitial()
            if selectedFloorMap == nil {
                selectedFloorMap = .first
            }
        }
    }
}

#Preview {
    EventMapScreen()
}
