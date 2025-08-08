import Model
import SwiftUI
import Theme

public struct TimetableCard: View {
    let timetableItem: any TimetableItem
    let isFavorite: Bool
    let onTap: (any TimetableItem) -> Void
    let onTapFavorite: (any TimetableItem, CGPoint?) -> Void

    @State private var dragLocation: CGPoint?

    public init(
        timetableItem: any TimetableItem,
        isFavorite: Bool,
        onTap: @escaping (any TimetableItem) -> Void,
        onTapFavorite: @escaping (any TimetableItem, CGPoint?) -> Void
    ) {
        self.timetableItem = timetableItem
        self.isFavorite = isFavorite
        self.onTap = onTap
        self.onTapFavorite = onTapFavorite
    }

    public var body: some View {
        Button {
            onTap(timetableItem)
        } label: {
            VStack(alignment: .leading, spacing: 8) {
                headerRow

                Text(timetableItem.title.currentLangTitle)
                    .font(.system(size: 16, weight: .medium))
                    .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                    .multilineTextAlignment(.leading)
                    .fixedSize(horizontal: false, vertical: true)

                if !timetableItem.speakers.isEmpty {
                    speakersList
                }
            }
            .padding(12)
            .frame(maxWidth: .infinity, alignment: .leading)
            .overlay(
                RoundedRectangle(cornerRadius: 4)
                    .stroke(AssetColors.outlineVariant.swiftUIColor, lineWidth: 1)
            )
            .cornerRadius(4)
        }
        .buttonStyle(PlainButtonStyle())
    }

    private var headerRow: some View {
        HStack(spacing: 8) {
            RoomTag(room: timetableItem.room)

            LanguageTag(language: timetableItem.language)

            Spacer()

            favoriteButton
        }
    }

    private var favoriteButton: some View {
        Button {
            let location = dragLocation
            onTapFavorite(timetableItem, location)
        } label: {
            Image(systemName: isFavorite ? "heart.fill" : "heart")
                .foregroundStyle(
                    isFavorite
                        ? AssetColors.primaryFixed.swiftUIColor
                        : AssetColors.onSurfaceVariant.swiftUIColor
                )
                .frame(width: 24, height: 24)
                .accessibilityLabel(isFavorite ? "Remove from favorites" : "Add to favorites")
        }
        .buttonStyle(PlainButtonStyle())
        .background(
            GeometryReader { _ in
                Color.clear
                    .onContinuousHover { phase in
                        switch phase {
                        case .active(let location):
                            dragLocation = location
                        case .ended:
                            break
                        }
                    }
            }
        )
    }

    private var speakersList: some View {
        VStack(alignment: .leading, spacing: 4) {
            ForEach(timetableItem.speakers, id: \.id) { speaker in
                HStack(spacing: 8) {
                    CircularUserIcon(imageUrl: speaker.iconUrl)
                        .frame(width: 32, height: 32)

                    Text(speaker.name)
                        .font(.system(size: 14, weight: .medium))
                        .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                }
            }
        }
    }
}
