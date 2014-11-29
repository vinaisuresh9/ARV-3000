from state import WorldState
from action import *
from agent import HeroAgent

import entity.location
import entity.item
import entity.person
import random

class World:
    def __init__(self, agent, location_pool, item_pool, person_pool):
        self.state = WorldState(location_pool, item_pool, person_pool)
        self.agent = agent
        self.history = []

    def get_agent(self):
        return self.agent

    def get_available_actions(self, agent):
        actions = {}
        actions['general'] = []
        actions['lose_memory'] = []

        astate = agent.get_state()

        for location in self.state.get_location_pool():
            if location.id != agent.get_state().get_current_location().id:
                actions['general'].append(MoveAction(location))

        # Applies only for Hero
        if agent.__class__.__name__ == 'HeroAgent':
            for item in astate.get_items():
                actions['general'].append(LoseItemAction(item))

            for item in self.state.get_item_pool():
                actions['general'].append(GetItemAction(item))

            for person in self.state.get_person_pool():
                actions['general'].append(TalkPersonAction(person))

            actions['lose_memory'].append(GetDrunkAction())
            actions['lose_memory'].append(HitWallAction())

        # Applies only for ReverseHero
        if agent.__class__.__name__ == 'ReverseHeroAgent':
            if astate.get_current_location() not in astate.get_examined_locations():
                actions['general'].append(ExamineLocationAction(astate.get_current_location()))

        return actions

    def progress_turn(self):
        action = self.agent.choose_action(self.state,
                self.get_available_actions(self.agent))
        action.apply(self.state, self.agent)
        self.history.append(action)

    def get_history(self):
        return self.history

if __name__ == "__main__":
    location_pool = entity.location.get_location_pool()
    inventory = entity.item.get_inventory()
    agent = HeroAgent("Hero", random.choice(location_pool), inventory)
    world = World(agent, location_pool, entity.item.get_item_pool(), entity.person.get_people_pool())

    while not world.get_agent().get_state().is_lost_memory():
        world.progress_turn()

    for action in world.get_history():
        print(action.generate_text(world.get_agent()))
