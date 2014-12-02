from story.generation.state import WorldState
from story.generation.action import *
from story.generation.agent import HeroAgent, ReverseHeroAgent

import story.generation.entity.location
import story.generation.entity.item
import story.generation.entity.person
import random

class World:
    def __init__(self, agent, location_pool, item_pool, person_pool):
        self.locations = list(location_pool)
        self.qlocations = list(location_pool)
        self.people = list(person_pool)
        self.state = WorldState(location_pool, item_pool, person_pool)
        self.agent = agent
        self.history = []
        self.qid = 0
        self.tmp = []

    def set_agent(self, agent):
        self.agent = agent

    def get_agent(self):
        return self.agent

    def get_locations(self):
        return self.locations

    def get_people(self):
        return self.people

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
        if action.__class__.__name__ != 'MoveAction':
            self.history.append((self.agent.get_state().get_current_location(), action))
        action.apply(self.state, self.agent)

    def get_history(self):
        return self.history

    def generate_quests(self, last_quest_id):
        self.qid += 1
        quests = []
        if len(self.history) != 0 or len(self.tmp) != 0:
            if last_quest_id == 0:
                a = self.history.pop()
                quests.append(Quest(self.qid, a[1].gen_quest_text(), last_quest_id, "QTYPE_OTHER", None, None, None))
                self.qid += 1
            else:
                d = -1
                for i in range(len(self.tmp)):
                    if self.tmp[i][0].get_id() == last_quest_id:
                        d = i
                        break
                if d != -1:
                    self.tmp.pop(d)
                for q in self.tmp:
                    self.history.append(q[1])
                self.tmp = []

            if len(self.history) != 0:
                random.shuffle(self.history)
                a = self.history.pop()
                q = Quest(self.qid, "Go to %s" % a[0].get_name(), last_quest_id, "QTYPE_LOCATION", a[0], None, a[1].gen_quest_text())
                quests.append(q)
                self.tmp.append((q,a))
            else:
                quests.append(Quest(1000, "It looks like you completely recovered your memory. What a weird day!", last_quest_id, "QTYPE_OTHER", None, None, None))

            if len(self.history) != 0:
                self.qid += 1
                random.shuffle(self.history)
                a = self.history.pop()
                q = Quest(self.qid, "Go to %s" % a[0].get_name(), last_quest_id, "QTYPE_LOCATION", a[0], None, a[1].gen_quest_text())
                quests.append(q)
                self.tmp.append((q,a))
        elif last_quest_id != 1000:
            quests.append(Quest(1000, "It looks like you completely recovered your memory. What a weird day!", last_quest_id, "QTYPE_OTHER", None, None, None))
        return quests

def generate_story():
    location_pool = story.generation.entity.location.get_location_pool()
    person_pool = story.generation.entity.person.get_people_pool()
    item_pool = story.generation.entity.item.get_item_pool()
    inventory = story.generation.entity.item.get_inventory()
    agent = HeroAgent("Hero", location_pool.pop(), inventory)
    world = World(agent, location_pool, item_pool, person_pool)

    while not world.get_agent().get_state().is_lost_memory():
        world.progress_turn()
    return world

if __name__ == "__main__":
    world = generate_story()
