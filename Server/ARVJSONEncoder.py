from story.entity.quest import *
from story.entity.dialog import *
from story.entity.item import *
from story.entity.location import *
from story.entity.person import *
from story.quests import *

class ARVJSONEncoder(JSONEncoder):
    def default(self, obj):
        if isinstance(obj, Quest):
            return {
                'id': obj.id,
                'desc': obj.desc, 
                'trigger': obj.trigger,
                'qtype': obj.qtype,
                'location': obj.location, 
                'dialog': obj.dialog,
                'result': obj.result,
            }
        if isinstance(obj, Location):
            return {
                'id': obj.id,
                'name': obj.name, 
                'desc': obj.desc,
                'lat': obj.lat,
                'lon': obj.lon, 
                'radius': obj.radius,
            }
        if isinstance(obj, Dialog):
            return {
                'person': obj.person, 
                'tree': obj.tree,
            }
        if isinstance(obj, Person):
            return {
                'id': obj.id,
                'name': obj.name, 
                'description': obj.desc,
                'location': obj.location,
            }
        if isinstance(obj, Item):
            return {
                'name': obj.name, 
                'desc': obj.desc,
                'location': obj.location,
            }
        return super(MYJSONEncoder, self).default(obj)
