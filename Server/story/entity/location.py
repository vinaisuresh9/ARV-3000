import json

class Location:
    def __init__(self, name, desc, lat, lon, radius):
        self.name = name
        self.desc = desc
        self.lat = lat
        self.lon = lon
        self.radius = radius
        
    def to_JSON(self):
        return json.dumps(self, default=lambda o: o.__dict__)
    