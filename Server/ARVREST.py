#!flask/bin/python

from flask import Flask, jsonify, make_response, abort, request
from story.entity.quest import *
from story.entity.dialog import *
from story.entity.item import *
from story.entity.location import *
from story.entity.person import *
from story.quests import *

app = Flask(__name__)

tasks = [
         {
         'id': 1,
         'title': 'Buy groceries',
         'description': 'Milk, Cheese, Pizza, Fruit, Tylenol',
         'done': False
         },
         {
         'id': 2,
         'title': 'Learn Python',
         'description': 'Need to find a good Python tutorial on the web',
         'done': False
         },
         {
         'id': 3,
         'title': 'Fake Python',
         'description': 'Need to find a good BLAHHHH tutorial on the web',
         'done': False
         }
         ]

@app.route('/api/tasks/<int:task_id>', methods=['GET'])
def get_task(task_id):
    task = filter(lambda t: t['id'] == task_id, tasks)
    if len(task) == 0:
        abort(404)
    return jsonify({'task': task[0]})

@app.route('/api/generate_story/<int:client_id>', methods=['GET'])
def generate_story(client_id):
    
    #generate story here and return the story_id, placeholder code for now
    story_id = 1
    return jsonify({'story_id': story_id})
    
@app.route('/api/get_locations/<int:story_id>', methods=['GET'])
def get_locations(story_id):
    return;

@app.route('/api/get_people/<int:story_id>', methods=['GET'])
def get_people(story_id):
    return;

@app.route('/api/get_people/<int:story_id>/<int:quest_id>', methods=['GET'])
def get_available_quests(story_id,quest_id):
    return;


@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)

if __name__ == '__main__':
    app.run(host='0.0.0.0',port=10080,debug=True)
