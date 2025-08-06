import Model
import Observation
import Presentation
import Foundation

@MainActor
@Observable
final class EventMapPresenter {
    var selectedFloorMap: FloorMap = .first
    var events: [Event] = []
    
    init() {}
    
    func loadInitial() {
        // TODO: Load actual event data
        events = Event.mockEvents
    }
    
    func selectFloorMap(_ floorMap: FloorMap) {
        selectedFloorMap = floorMap
    }
    
    func moreDetailButtonTapped(_ url: URL) {
        print("More detail tapped: \(url)")
        // TODO: Open in Safari
    }
}

// Mock models - TODO: Replace with actual models from shared module
enum FloorMap: String, CaseIterable {
    case first = "1F"
    case b1f = "B1F"
    
    var image: String {
        switch self {
        case .b1f:
            // TODO: Replace with actual B1F map image
            return "map"
        case .first:
            // TODO: Replace with actual 1F map image
            return "map.fill"
        }
    }
}

extension Event {
    @MainActor static let mockEvents: [Event] = [
        Event(
            id: "1",
            title: "Welcome Talk",
            description: "Opening ceremony and keynote presentation",
            room: Room(id: 1, name: .init(jaTitle: "roomF", enTitle: "roomF"), type: .roomF, sort: 0),
            moreDetailUrl: URL(string: "https://droidkaigi.jp/2025/welcome-talk")
        ),
        Event(
            id: "2",
            title: "Party",
            description: "Networking party with food and drinks",
            room: Room(id: 1, name: .init(jaTitle: "roomG", enTitle: "roomG"), type: .roomG, sort: 0),
            moreDetailUrl: URL(string: "https://droidkaigi.jp/2025/party")
        ),
        Event(
            id: "3",
            title: "Ask the Speaker",
            description: "Q&A session with conference speakers",
            room: Room(id: 1, name: .init(jaTitle: "roomH", enTitle: "roomH"), type: .roomH, sort: 0),
            moreDetailUrl: URL(string: "https://droidkaigi.jp/2025/ask-speaker")
        ),
    ]
}
