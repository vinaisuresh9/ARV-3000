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
    items.append(Item(2, "Keys", "Your keys",None))
    return items

def get_item_pool():
    items = []
    items.append(Item(3, "an interesting rock", "Just rock", None))
    items.append(Item(4, "a magic book", "Just book", None))
    items.append(Item(5, "an old sock", "It stinks", None))
    items.append(Item(6, "a football", "Harrison Butker's football", None))
    items.append(Item(7, "a book titled 'Tiger Whispers'","", None))
    return items
