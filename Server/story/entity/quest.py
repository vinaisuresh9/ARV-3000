from json import JSONEncoder

class Quest (JSONEncoder) :
    # trigger currently points to previous quest
    # might need something more sophisticated later
    def __init__(self, desc, trigger, qtype, location, dialog, result):
        self.desc = desc
        self.trigger = trigger
        self.qtype = qtype
        self.location = location
        self.dialog = dialog
        self.result = result