from state import AgentState

import random

class Agent:
    def __init__(self, initial_location):
        self.name = "Agent"
        self.state = AgentState(initial_location)
    
    def get_name(self):
        return self.name

    def choose_action(self, state, actions):
        return actions[0]

    def get_state(self):
        return self.state

class HeroAgent(Agent):
    def __init__(self, name, initial_location):
        self.name = name
        self.state = AgentState(initial_location)

    def choose_action(self, state, actions):
        # Random
        return random.choice(actions)
        
class ReverseHeroAgent(Agent):
    def __init_(self, name, initial_location, action_history):
        self.name = name
        self.action_history = action_history
        self.state = AgentState(initial_location)
