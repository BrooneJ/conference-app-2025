import Dependencies
import SwiftUI
import HomeFeature
import AboutFeature
import ContributorFeature
import EventMapFeature
import FavoriteFeature
import ProfileCardFeature
import SearchFeature
import SponsorFeature
import StaffFeature
import TimetableDetailFeature
import Model

private enum TabType: CaseIterable, Hashable {
    case timetable
    case map
    case favorite
    case info
    case profileCard

    internal func tabImageName(_ selectedTab: TabType) -> String {
        switch self {
        case .timetable:
            return selectedTab == self ? "ic_timetable.fill" : "ic_timetable"
        case .map:
            return selectedTab == self ? "ic_map.fill" : "ic_map"
        case .favorite:
            return selectedTab == self ? "ic_fav.fill" : "ic_fav"
        case .info:
            return selectedTab == self ? "ic_info.fill" : "ic_info"
        case .profileCard:
            return selectedTab == self ? "ic_profileCard.fill" : "ic_profileCard"
        }
    }
}

public struct RootScreen: View {
    @Environment(\.scenePhase) private var scenePhase
    @State private var selectedTab: TabType = .timetable
    @State private var navigationPath = NavigationPath()
    @State private var aboutNavigationPath = NavigationPath()
    @State private var favoriteNavigationPath = NavigationPath()
    private let presenter = RootPresenter()
    
    public init() {
        UITabBar.appearance().unselectedItemTintColor = UIColor(named: "tab_inactive")
    }
    
    public var body: some View {
        TabView(selection: $selectedTab) {
            NavigationStack(path: $navigationPath) {
                HomeScreen(onNavigate: handleHomeNavigation)
                    .navigationDestination(for: NavigationDestination.self) { destination in
                        let navigationHandler = NavigationHandler(
                            handleSearchNavigation: handleSearchNavigation
                        )
                        destination.view(with: navigationHandler)
                    }
            }
            .tabItem {
                Label("Timetable", image: TabType.timetable.tabImageName(selectedTab))
            }
            .tag(TabType.timetable)
            
            NavigationStack {
                EventMapScreen()
            }
            .tabItem {
                Label("Map", image: TabType.map.tabImageName(selectedTab))
            }
            .tag(TabType.map)
            
            NavigationStack(path: $favoriteNavigationPath) {
                FavoriteScreen(onNavigate: handleFavoriteNavigation)
                    .navigationDestination(for: FavoriteNavigationDestination.self) { destination in
                        switch destination {
                        case .timetableDetail(let item):
                            TimetableDetailScreen(timetableItem: item)
                        }
                    }
            }
            .tabItem {
                Label("Favorite", image: TabType.favorite.tabImageName(selectedTab))
            }
            .tag(TabType.favorite)
            
            NavigationStack(path: $aboutNavigationPath) {
                AboutScreen(onNavigate: handleAboutNavigation)
                    .navigationDestination(for: AboutNavigationDestination.self) { destination in
                        switch destination {
                        case .contributors:
                            ContributorScreen()
                        case .staff:
                            StaffScreen()
                        case .sponsors:
                            SponsorScreen()
                        }
                    }
            }
            .tabItem {
                Label("Info", image: TabType.info.tabImageName(selectedTab))
            }
            .tag(TabType.info)
            
            NavigationStack {
                ProfileCardScreen()
            }
            .tabItem {
                Label("Profile", image: TabType.profileCard.tabImageName(selectedTab))
            }
            .tag(TabType.profileCard)
        }
        .onAppear {
            presenter.prepareWindow()
        }
        .onChange(of: scenePhase) {
            ScenePhaseHandler.handle(scenePhase)
        }
        .preferredColorScheme(.dark)
    }
    
    private func handleHomeNavigation(_ destination: HomeNavigationDestination) {
        switch destination {
        case .timetableDetail(let item):
            navigationPath.append(NavigationDestination.timetableDetail(item))
        case .search:
            navigationPath.append(NavigationDestination.search)
        }
    }
    
    private func handleAboutNavigation(_ destination: AboutNavigationDestination) {
        aboutNavigationPath.append(destination)
    }
    
    private func handleFavoriteNavigation(_ destination: FavoriteNavigationDestination) {
        favoriteNavigationPath.append(destination)
    }
    
    private func handleSearchNavigation(_ destination: SearchNavigationDestination) {
        switch destination {
        case .timetableDetail(let item):
            navigationPath.append(NavigationDestination.timetableDetail(item))
        }
    }
}

#Preview {
    RootScreen()
}
