from story.generation.state import WorldState
from story.generation.action import *
from story.generation.agent import HeroAgent, ReverseHeroAgent

import story.generation.entity.location
import story.generation.entity.item
import story.generation.entity.person
import random

class World:
    def __init__(self, agent, location_pool, item_pool, person_pool):
        self.state = WorldState(location_pool, item_pool, person_pool)
        self.agent = agent
        self.history = []

    def set_agent(self, agent):
        self.agent = agent

    def get_agent(self):
        return self.agent

    def get_available_actions(self, agent):
        actions = {}
        actions['general'] = []
        actions['move'] = []
        actions['lose_memory'] = []
        actions['reverse'] = []

        astate = agent.get_state()


        # Applies only for Hero
        if agent.__class__.__name__ == 'HeroAgent':
            for location in self.state.get_location_pool():
                if location.id != astate.get_current_location().id:
                    actions['move'].append(MoveAction(location))

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
            pass

        return actions

    def progress_turn(self):
        action = self.agent.choose_action(self.state,
                self.get_available_actions(self.agent))
        action.apply(self.state, self.agent)
        self.history.append(action)

    def get_history(self):
        return self.history

    def set_reverse(self):
        self.reverse = self.history
        self.history = []

def generate_quests(world):
    id = 0
    quests = []
    world.get_history().reverse()
    for action in world.get_history():
        q = action.generate_quest(id, id-1)
        quests.append(q)
        id += 1
    return quests

def generate_story():
    location_pool = story.generation.entity.location.get_location_pool()
    person_pool = story.generation.entity.person.get_people_pool()
    item_pool = story.generation.entity.item.get_item_pool()
    inventory = story.generation.entity.item.get_inventory()
    agent = HeroAgent("Hero", random.choice(location_pool), inventory)
    world = World(agent, list(location_pool), list(item_pool), list(person_pool))

    while not world.get_agent().get_state().is_lost_memory():
        world.progress_turn()
    return (generate_quests(world), location_pool, person_pool, inventory + item_pool)
