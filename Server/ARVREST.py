#!flask/bin/python

from flask import Flask, jsonify, make_response, abort, request
from json import JSONEncoder
from story.generation.entity.quest import *
from story.generation.entity.dialog import *
from story.generation.entity.item import *
from story.generation.entity.location import *
from story.generation.entity.person import *
from story.quests import *
from ARVJSONEncoder import *

app = Flask(__name__)
app.json_encoder = ARVJSONEncoder


@app.route('/api/tasks/<int:task_id>', methods=['GET'])
def get_task(task_id):
    task = filter(lambda t: t['id'] == task_id, tasks)
    if len(task) == 0:
        abort(404)
    return jsonify({'task': task[0]})

@app.route('/api/generate_story/<int:client_id>', methods=['GET'])
def generateStory(client_id):
    #generate story here and return the story_id
    return jsonify(story_id=create_story(client_id))
    
@app.route('/api/get_locations/<int:story_id>', methods=['GET'])
def getLocations(story_id):
    loc = get_locations(story_id)
    return jsonify(locations=loc)

@app.route('/api/get_people/<int:story_id>', methods=['GET'])
def getPeople(story_id):
    people = get_people(story_id)
    return jsonify(people=people)

@app.route('/api/get_available_quests/<int:story_id>/<int:quest_id>', methods=['GET'])
def getAvailableQuests(story_id,quest_id):
    quests = get_quests(story_id,quest_id)
    return jsonify(quests=quests)

@app.route('/api/get_story/<int:story_id>', methods=['GET'])
def getSTory(story_id):
    story = get_story(story_id)
    return jsonify(story=story)


@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)

if __name__ == '__main__':
    app.run(host='0.0.0.0',port=5000,debug=True)
