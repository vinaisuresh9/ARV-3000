from state import WorldState

class World:
    def __init__(self):
        self.current_state = WorldState()

    def get_available_actions(self, agent):
        return []

    def progress_turn(self):
        pass