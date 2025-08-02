import Extension
import SwiftUI
import Model
import Presentation
import Theme

struct TimetableGridCard: View {
    let timetableItem: any TimetableItem
    let cellCount: Int
    let onTap: (any TimetableItem) -> Void
    
    var body: some View {
        Button(action: {
            onTap(timetableItem)
        }) {
            VStack(alignment: .leading, spacing: 4) {
                Text("10:00 ~ 11:00")
                    .font(.system(size: 14, weight: .medium))
                    .foregroundStyle(timetableItem.room.color)
                    .multilineTextAlignment(.leading)

                Text(timetableItem.title.currentLangTitle)
                    .font(.system(size: 14, weight: .medium))
                    .foregroundStyle(timetableItem.room.color)
                    .multilineTextAlignment(.leading)
                    .lineLimit(2)

                Spacer()

                if !timetableItem.speakers.isEmpty {
                    HStack {
                        CircularUserIcon(imageUrl: timetableItem.speakers.first?.iconUrl)
                            .frame(width: 32, height: 32)

                        Text(timetableItem.speakers.map(\.name).joined(separator: ", "))
                            .font(.system(size: 12))
                            .foregroundStyle(AssetColors.onSurface)
                            .lineLimit(1)

                        Spacer()
                    }
                }
            }
            .padding(8)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(timetableItem.room.color.opacity(0.1))
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(timetableItem.room.color, lineWidth: 1)
            )
            .cornerRadius(8)
        }
        .buttonStyle(PlainButtonStyle())
        .frame(width: CGFloat(192 * cellCount + 4 * (cellCount - 1)), height: 153)
    }
}

// TODO: Add preview with proper test data
//#Preview {
//    TimetableGridCard(
//        timetableItem: ...,
//        cellCount: 1,
//        onTap: { _ in }
//    )
//}
