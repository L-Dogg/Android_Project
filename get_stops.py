import requests
import json

API_KEY = 'type in api key'
URL = 'https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=52.233333,21.016667&radius=1500&keyword={}&key={}'
STOPS = ['Centrum stop', 
        'Nowowiejska stop', 
        'Plac Politechniki stop',
        'Wawelska stop', 
        'Metro Marymont stop']


with open('stops.txt', 'w', encoding='utf-8') as outfile:
	for stop in STOPS:
		response = requests.post(URL.format(stop, API_KEY)).json()
		outfile.write('STOP: {}\n'.format(stop))
		for item in response['results']:
			geodata = item['geometry']['location']
			outfile.write('lat: {} lng: {}\n'.format(geodata["lat"], geodata["lng"]))
