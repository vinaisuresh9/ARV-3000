from story.generation.entity.location import Location
from story.generation.entity.person import Person
from story.generation.entity.quest import Quest

import story.generation.world

QTYPE_LOCATION = "QTYPE_LOCATION"
QTYPE_DIALOG = "QTYPE_DIALOG"
QTYPE_OTHER = "QTYPE_OTHER"

stories = {}

# Generate story given client id
# Returns story id
def create_story(client_id):
    stories[client_id] = story.generation.world.generate_story()
    return client_id

# Returns list of locations for given story id
def get_locations(story_id):
    if story_id in stories:
        return stories[story_id].get_locations()
    else:
        return []

# Returns list of people for given story id
def get_people(story_id):
    if story_id in stories:
        return stories[story_id].get_people()
    else:
        return []

# Generate and return next batch of quests
def get_quests(story_id, last_quest_id):
    quests = []
    # if (last_quest_id == 0):
    #     quests.append(Quest(1, "You wake up in Van Leer, and you don't recall the previous day. But you think something big happened.", -1, QTYPE_OTHER, None, None, None))
    #     quests.append(Quest(2, "You reach into your pocket and see a receipt for Taco Bell. What would you like to do?", 0, QTYPE_OTHER, None, None, None))
    #     quests.append(Quest(3, "Go to Taco Bell", 1, QTYPE_LOCATION, Location(2, "Taco Bell", "Taco Bell", 33.772626, -84.373187, 5), None, "You see a friend and ask them if they saw you yesterday. He tells you that he saw you briefly last night at Pi Kappa Theta with your roommate."))
    #     quests.append(Quest(4, "Go to Brown", 2, QTYPE_LOCATION, Location(0, "Brown", "Brown", 33.771846, -84.391845, 5), None, "You go home and see your roommate on his bed. He tells you that he doesn't remember much thinks you went to Waffle House."))
    #     quests.append(Quest(5, "Go to Pi Kappa Theta", 2, QTYPE_LOCATION, Location(1, "Pi Kappa Theta", "Pi Kappa Theta", 33.776595, -84.393851, 5), None, "You see a fraternity member at the front of the house. He waves to you and says that he enjoyed meeting you last night and wants to extend a bid to you."))
    # if (last_quest_id == 4):
    #     quests.append(Quest(6, "Go to Waffle House", 4, QTYPE_LOCATION, Location(3, "Waffle House", "Waffle House", 33.772626, -84.373187, 5), None, "You're in Waffle House. Congrats."))
    #     quests.append(Quest(7, "Examine the wallet", 4, QTYPE_OTHER, None, None, "You examined your wallet. Nothing happened."))
    if story_id in stories:
        quests = stories[story_id].generate_quests(last_quest_id)
    return quests

def get_story(story_id):
    if story_id in stories:
        return stories[story_id].get_story()
    else:
        return ""
