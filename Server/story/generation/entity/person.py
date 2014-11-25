class Person:
    def __init__(self, id, name, desc, location):
        self.id = id
        self.name = name
        self.desc = desc
        self.location = location

def get_people_pool():
    return []