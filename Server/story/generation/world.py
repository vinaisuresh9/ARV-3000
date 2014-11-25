from state import State

class World:
    def __init__(self):
        self.current_state = State()

    def get_available_actions(self, agent):
        return []

    def progress_turn(self):
        pass
