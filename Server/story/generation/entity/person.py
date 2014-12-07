class Person:
    def __init__(self, id, name, desc, location):
        self.id = id
        self.name = name
        self.desc = desc
        self.location = location

    def get_name(self):
        return self.name

def get_people_pool():
    persons = []
    persons.append(Person(0, "John Stacy", "roommate", None))
    persons.append(Person(1, "Trey Songz", "singer extraordinaire", None))
    persons.append(Person(2, "Mike Tyson", "tiger enthusiast", None))
    persons.append(Person(3, "John Dorian", "doctor", None))
    persons.append(Person(4, "Jeff Tranny", "transfer student", None))
    persons.append(Person(5, "Mark Riedl", "professor", None))
    persons.append(Person(6, "Harrison Butker", "football kicker", None))
    return persons
