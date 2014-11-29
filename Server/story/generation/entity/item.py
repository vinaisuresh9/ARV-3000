class Item:
    def __init__(self, id, name, desc, location):
        self.id = id
        self.name = name
        self.desc = desc
        self.location = location

    def get_name(self):
        return self.name
        
def get_inventory():
    items = [];
    items.append(Item(0, "Wallet", "Your wallet", None))
    items.append(Item(1, "Phone", "Your phone", None))
    return items

def get_item_pool():
    items = []
    items.append(Item(2, "Interesting rock", "Just rock", None))
    items.append(Item(3, "Magic book", "Just book", None))
    items.append(Item(4, "Old sock", "It stinks", None))
    return items
