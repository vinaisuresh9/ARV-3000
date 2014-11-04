class Dialog:
    # Not sure how to represent dialog trees yet,
    # Might be reworked
    def __init__(self, person, tree):
        self.person = person
        self.tree = tree

    def to_JSON(self):
        return json.dumps(self, default=lambda o: o.__dict__)