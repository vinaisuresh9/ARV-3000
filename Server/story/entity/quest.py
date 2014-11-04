import location
import json

class Quest:
    # trigger currently points to previous quest
    # might need something more sophisticated later
    def __init__(self, desc, trigger, qtype, location, dialog, result):
        self.desc = desc
        self.trigger = trigger
        self.qtype = qtype
        self.location = location
        self.dialog = dialog
        self.result = result
        
    def to_JSON(self):
        return json.dumps(self, default=lambda o: o.__dict__)