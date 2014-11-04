# Not used currently, but might be needed in the future
class Item:
    def __init__(self, name, desc, location):
        self.name = name
        self.desc = desc
        self.location = location

    def to_JSON(self):
        return json.dumps(self, default=lambda o: o.__dict__)