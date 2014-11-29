class WorldState:
    def __init__(self, location_pool, item_pool, person_pool):
        self.location_pool = location_pool
        self.item_pool = item_pool
        self.person_pool = person_pool

    def get_location_pool(self):
        return self.location_pool

    def get_item_pool(self):
        return self.item_pool

    def get_person_pool(self):
        return self.person_pool
    
class AgentState:
    def __init__(self, initial_location, items):
        self.current_location = initial_location
        self.examined_locations = []
        self.examined_items = []
        self.items = items
        self.lost_memory = False
        self.moves = 0
        self.people = []

    def get_current_location(self):
        return self.current_location
    
    def set_current_location(self, location):
        self.current_location = location
        
    def get_items(self):
        return self.items

    def get_examined_locations(self):
        return self.examined_locations

    def get_examined_items(self):
        return self.examined_items

    def get_people(self):
        return self.people

    def is_lost_memory(self):
        return self.lost_memory

    def set_lost_memory(self, memory):
        self.lost_memory = memory

    def inc_moves(self):
        self.moves += 1
