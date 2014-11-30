from story.generation.entity.item import Item
from story.generation.entity.location import Location

class Action:
    def __init__(self):
        pass

    def apply(self, state, agent):
        pass
    
    def generate_text(self, agent):
        return ""

# Generic Actions

class MoveAction(Action):
    def __init__(self, location):
        self.new_location = location
        
    def apply(self, state, agent):
        agent.get_state().set_current_location(self.new_location)
        agent.get_state().inc_moves()
        
    def generate_text(self, agent):
        return "%s went to %s" % (agent.get_name(), self.new_location.get_name())

class GetItemAction(Action):
    def __init__(self, item):
        self.item = item
        
    def apply(self, state, agent):
        agent.get_state().get_items().append(self.item)
        agent.get_state().inc_moves()
        state.get_item_pool().remove(self.item)
        
    def generate_text(self, agent):
        return "%s found %s" % (agent.get_name(), self.item.get_name())

class TalkPersonAction(Action):
    def __init__(self, person):
        self.person = person

    def apply(self, state, agent):
        agent.get_state().get_people().append(self.person)
        agent.get_state().inc_moves()
        state.get_person_pool().remove(self.person)

    def generate_text(self, agent):
        return "%s talked to %s" % (agent.get_name(), self.person.get_name())

# Hero Only Actions

class GetDrunkAction(Action):
    def __init__(self):
        pass

    def apply(self, state, agent):
        agent.get_state().set_lost_memory(True)
        agent.get_state().inc_moves()

    def generate_text(self, agent):
        return "%s got drunk and lost his memory" % agent.get_name()

class HitWallAction(Action):
    def __init__(self):
        pass

    def apply(self, state, agent):
        agent.get_state().set_lost_memory(True)
        agent.get_state().inc_moves()

    def generate_text(self, agent):
        return "%s walked into a wall and lost his memory" % agent.get_name()

class LoseItemAction(Action):
    def __init__(self, item):
        self.item = item

    def apply(self, state, agent):
        agent.get_state().get_items().remove(self.item)
        agent.get_state().inc_moves()

    def generate_text(self, agent):
        return "%s lost his %s" % (agent.get_name(), self.item.get_name())

# Reverse Hero Only Actions
        
class ExamineLocationAction(Action):
    def __init__(self, current_location):
        self.current_location = current_location
        
    def apply(self, state, agent):
        agent.get_state().get_examined_locations().append(self.current_location)
        agent.get_state().inc_moves()
    
    def generate_text(self, agent):
        return "%s looked around %s" % (agent.get_name(), agent.get_state().get_current_location().get_name())
    
class ExamineItemAction(Action):
    def __init__(self, item):
        self.item = item
        
    def apply(self, state, agent):
        agent.get_state().get_examined_items().append(self.item)
        agent.get_state().inc_moves()
        
    def generate_text(self, agent):
        return "%s examined %s" % (agent.get_name(), self.item.get_name())
