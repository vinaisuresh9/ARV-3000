class WorldState:
    def __init__(self):
        pass
    
class AgentState:
    def __init__(self):
        self.current_location = Location()
        self.items = []

    def get_current_location(self):
        return self.current_location
    
    def set_current_location(self, location):
        self.current_location = location
        
    def get_items(self):
        return self.items