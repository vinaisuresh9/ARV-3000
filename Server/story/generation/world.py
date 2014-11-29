from state import WorldState
from action import MoveAction
from agent import HeroAgent

import entity.location

class World:
    def __init__(self, agents):
        self.current_state = WorldState()
        self.agents = agents
        self.history = []

    def get_available_actions(self, agent):
        actions = []
        for location in entity.location.get_location_pool():
            if location.id != agent.get_state().get_current_location().id:
                actions.append(MoveAction(location))
        return actions

    def progress_turn(self):
        for agent in self.agents:
            action = agent.choose_action(self.current_state,
                    self.get_available_actions(agent))
            self.history.append(action.generate_text(self.current_state, agent))
            action.apply(self.current_state, agent)

    def get_history(self):
        return self.history

if __name__ == "__main__":
    world = World([HeroAgent("Azat", entity.location.get_location_pool()[0])])
    for i in range(5):
        world.progress_turn()
    print(world.get_history())
