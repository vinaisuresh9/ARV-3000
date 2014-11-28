class Item:
    def __init__(self, name, desc, location):
        self.id = id
        self.name = name
        self.desc = desc
        self.location = location
        
def get_item_pool():
    items = [];
    items.append(Item(0, "wallet", "Your wallet", random.randint(0, 25)))
    items.append(Item(0, "phone", "Your phone", random.randint(0, 25)))
    items.append(Item(0, "wallet", "Your wallet", random.randint(0, 25)))
    items.append(Item(0, "wallet", "Your wallet", random.randint(0, 25)))

    return []