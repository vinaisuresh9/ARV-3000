from json import JSONEncoder

import random

class Location:
    def __init__(self, id, name, desc, lat, lon, radius):
        self.id = id
        self.name = name
        self.desc = desc
        self.lat = lat
        self.lon = lon
        self.radius = radius

    def get_name(self):
        return self.name

def get_location_pool():
    locations = []
    locations.append(Location(0,"College of Computing", "", 33.777569, -84.397363, None))
    locations.append(Location(1, "Klaus", "Klaus Advanced Computing Building",  33.777350, -84.396108, None))
    locations.append(Location(2, "TSRB", "Tech Square Research Building",   33.777052, -84.389878, None))
    locations.append(Location(3, "Institute of Bioengineering and Biosciences", "", 33.778685  ,-84.397388, None))
    locations.append(Location(4, "Clough", "Clough Undergraduate Learning Commons", 33.774966, -84.396385, None))
    locations.append(Location(5, "CRC", "Campus Recreation Center",  33.775408, -84.403412, None))
    locations.append(Location(6, "Student Center", "",   33.774159, -84.400247, None))
    locations.append(Location(7, "Library", "",   33.774284, -84.395956, None))
    locations.append(Location(8, "McCamish Pavilion", "",   33.780802, -84.392911, None))
    locations.append(Location(9, "Waffle House", "",   33.776664, -84.389451, None))
    locations.append(Location(10, "BookStore", "",   33.776695, -84.388593, None))
    locations.append(Location(11, "Ferst Center", "",   33.774925, -84.399134, None))
    locations.append(Location(12, "Instructional Center", "", 33.775438, -84.401274, None))
    locations.append(Location(13, "GLC", "",   33.781759, -84.396378, None))
    locations.append(Location(14, "Starbucks", "",   33.774228, -84.396491, None))
    locations.append(Location(15, "Tech Tower", "",   33.772436, -84.394830, None))
    locations.append(Location(16, "Phi Kappa Alpha", "",   33.776778, -84.395277, None))
    locations.append(Location(17, "Phi Mu", "",   33.776798, -84.394757, None))
    locations.append(Location(18, "Delta Chi", "",   33.776713, -84.392925, None))
    locations.append(Location(19, "Bobby Dodd Stadium", "",    33.773722, -84.392903, None))
    locations.append(Location(20, "Student Success Center", "",    33.772596, -84.394043, None))
    locations.append(Location(21, "Byers Tennis Complex", "",  33.781113, -84.393820, None))
    locations.append(Location(22, "Ray's Pizza", "",    33.776692, -84.388139, None))
    locations.append(Location(23, "Georgia Tech Hotel", "",  33.776331,-84.388992, None))
    locations.append(Location(24, "Burger Bowl", "",   33.778364, -84.403001, None))
    locations.append(Location(25, "E. Roe Stamps IV Field", "",   33.776755, -84.402738, None))
    return random.sample(locations, 10)
