from json import JSONEncoder

class Location:
    def __init__(self, id, name, desc, lat, lon, radius):
        self.id = id
        self.name = name
        self.desc = desc
        self.lat = lat
        self.lon = lon
        self.radius = radius

def get_location_pool():
    return []