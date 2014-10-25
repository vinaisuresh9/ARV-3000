#!flask/bin/python
from flask import Flask, jsonify, make_response, abort, request

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

@app.route('/api/tasks', methods=['GET'])
def get_tasks():
    return jsonify({'tasks': tasks})

@app.route('/api/tasks/<int:task_id>', methods=['GET'])
def get_task(task_id):
    task = filter(lambda t: t['id'] == task_id, tasks)
    if len(task) == 0:
        abort(404)
    return jsonify({'task': task[0]})

@app.route('/api/generatestory', methods=['GET'])
def generate_story():
    
@app.route('api/')

@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)

if __name__ == '__main__':
    app.run(host='0.0.0.0',port=10080,debug=True)
