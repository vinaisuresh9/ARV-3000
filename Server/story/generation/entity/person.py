class Person:
    def __init__(self, id, name, desc, location):
        self.id = id
        self.name = name
        self.desc = desc
        self.location = location

def get_people_pool():
    persons = []
    persons.append(Person(0, "John Stacy", "roomate", 0))
    persons.append(Person(1, "Trey Songz", "singer extraordinaire", 6))
    persons.append(Person(2, "Mike Tyson", "tiger enthusiast", 9))
    persons.append(Person(3, "John Dorian", "doctor", 22))
    persons.append(Person(4, "Jeff Tranny", "transfer student", random.randint(0,25)))

    return []