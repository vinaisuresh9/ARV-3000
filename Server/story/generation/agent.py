from story.generation.state import AgentState

import random

class Agent:
    def __init__(self, initial_location, items):
        self.name = "Agent"
        self.state = AgentState(initial_location, items)
    
    def get_name(self):
        return self.name

    def choose_action(self, state, actions):
        return actions[0]

    def get_state(self):
        return self.state

class HeroAgent(Agent):
    def __init__(self, name, initial_location, items):
        self.name = name
        self.state = AgentState(initial_location, items)
        self.moved = True

    def choose_action(self, state, actions):
        if self.moved:
            self.moved = False
            return random.choice(actions['general'])
        else:
            self.moved = True
            if self.state.moves < 10:
                return random.choice(actions['move'])
            else:
                return random.choice(actions['lose_memory'])
        
class ReverseHeroAgent(Agent):
    def __init__(self, name, initial_location, action_history, items):
        self.name = name
        self.action_history = action_history
        self.state = AgentState(initial_location, items)

    def choose_action(self, state, actions):
        return actions['reverse'][0]
