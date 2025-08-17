import Dependencies
import DependenciesMacros
import Model

@DependencyClient
public struct TimetableUseCase: Sendable {
    public var load: @Sendable () -> any AsyncSequence<Model.Timetable, Never> = { AsyncStream.never }
    public var toggleFavorite: @Sendable (_ itemId: Model.TimetableItemId) async -> Void = { _ in }
}

public enum TimetableUseCaseKey: TestDependencyKey {
    public static let testValue = TimetableUseCase()
}

extension DependencyValues {
    public var timetableUseCase: TimetableUseCase {
        get { self[TimetableUseCaseKey.self] }
        set { self[TimetableUseCaseKey.self] = newValue }
    }
}
