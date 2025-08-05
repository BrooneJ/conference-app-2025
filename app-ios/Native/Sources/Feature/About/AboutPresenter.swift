import Model
import Observation
import Presentation
import Foundation

@MainActor
@Observable
final class AboutPresenter {
    
    init() {}
    
    func contributorsTapped() {
        print("Contributors tapped")
    }
    
    func staffsTapped() {
        print("Staffs tapped")
    }
    
    func sponsorsTapped() {
        print("Sponsors tapped")
    }
    
    func codeOfConductTapped() {
        print("Code of Conduct tapped")
    }
    
    func privacyPolicyTapped() {
        print("Privacy Policy tapped")
    }
    
    func youtubeTapped() {
        print("YouTube tapped")
    }
    
    func xcomTapped() {
        print("X.com tapped")
    }
    
    func mediumTapped() {
        print("Medium tapped")
    }
    
    func switchToComposeMultiplatformTapped() {
        print("Switch to Compose Multiplatform tapped")
    }
}
