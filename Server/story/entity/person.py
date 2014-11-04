class Person:
    def __init__(self, name, description, location):
        self.name = name
        self.description = description
        self.location = location

    def to_JSON(self):
        return json.dumps(self, default=lambda o: o.__dict__)