from state import AgentState

class Agent:
    def __init__(self):
        self.name = "Agent"
        self.state = AgentState()
    
    def get_name(self):
        return self.name

    def choose_action(self, state, actions):
        return actions[0]

class HeroAgent(Agent):
    def __init__(self, name):
        self.name = name
        
class ReverseHeroAgent(Agent):
    def __init_(self, name, action_history):
        self.name = name
        self.action_history = action_history