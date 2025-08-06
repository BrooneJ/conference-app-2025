import SwiftUI
import Model
import Extension
import Theme

public struct RoomTag: View {
    let room: Room
    
    public init(room: Room) {
        self.room = room
    }
    
    public var body: some View {
        Text(room.displayName)
            .font(.system(size: 11, weight: .semibold))
            .foregroundStyle(.white)
            .padding(.horizontal, 6)
            .padding(.vertical, 3)
            .background(room.color)
            .cornerRadius(4)
    }
}

public struct LanguageTag: View {
    let language: TimetableLanguage
    
    public init(language: TimetableLanguage) {
        self.language = language
    }
    
    public var body: some View {
        Text(language.displayLanguage)
            .font(.system(size: 11, weight: .medium))
            .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
            .padding(.horizontal, 6)
            .padding(.vertical, 3)
            .overlay(
                RoundedRectangle(cornerRadius: 4)
                    .stroke(AssetColors.outline.swiftUIColor, lineWidth: 1)
            )
    }
}

public struct CircularUserIcon: View {
    let imageUrl: String?
    
    public init(imageUrl: String?) {
        self.imageUrl = imageUrl
    }
    
    public var body: some View {
        if let imageUrl = imageUrl, let url = URL(string: imageUrl) {
            AsyncImage(url: url) { image in
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
            } placeholder: {
                Image(systemName: "person.circle.fill")
                    .foregroundStyle(AssetColors.outline.swiftUIColor)
            }
            .clipShape(Circle())
        } else {
            Image(systemName: "person.circle.fill")
                .resizable()
                .foregroundStyle(AssetColors.outline.swiftUIColor)
        }
    }
}