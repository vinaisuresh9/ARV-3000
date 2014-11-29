from entity.item import Item
from entity.location import Location

class Action:
    def __init__(self):
        pass

    def apply(self, state):
        return state
    
    def generate_text(self, state, agent):
        return ""

class MoveAction(Action):
    def __init__(self, location):
        self.new_location = location
        
    def apply(self, state, agent):
        agent.get_state().set_current_location(self.new_location)
        
    def generate_text(self, state, agent):
        return "%s went to %s" % (agent.get_name(), self.new_location.get_name())
        
class ExamineLocationAction(Action):
    def __init__(self, current_location):
        self.current_location = current_location
        
    def apply(self, state):
        # TODO
        pass
    
    def generate_text(self, state, agent):
        return "%s looked around %s" % (agent.get_name(), agent.get_state().get_current_location().get_name())
    
class GetItemAction(Action):
    def __init__(self, item):
        self.item = item
        
    def apply(self, state):
        state.get_items().append(self.item)
        
    def generate_text(self, state, agent):
        return "%s found %s" % (agent.get_name(), self.item.get_name())
    
class ExamineItemAction(Action):
    def __init__(self, item):
        self.item = item
        
    def apply(self, state):
        state.get_items().append(self.item)
        
    def generate_text(self, state, agent):
        return "%s examined %s" % (agent.get_name(), self.item.get_name())
