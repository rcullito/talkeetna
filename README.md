# Talkeetna

## install dependencies

	lein deps

## command line app
each file has a few records there already

	lein run people.csv
	lein run people.psv
	lein run people.ssv

## test suite

	lein test

## test coverage

	lein cloverage

## webserver

a dev server on localhost:3000 can be started with

	lein ring server-headless
	
once it's started, the routes will be available at:

`POST /records`
`GET /records/gender`
`GET /records/birthdate`
`GET /records/name`

## about the routes

Each GET will contain records from all three files, 
as well as the POSTed records, which are stored in memory.

This behavior can be tested by the user through POSTing a record and then
looking for it in the results. My favorite option was to choose a birthday in the
1800s and look for it at the top of the birthdate route.

an example request body for `POST /records` would be `{"person": "Aaberg, Chelsea, Female, Blue, 02/08/1897"}`

## assumptions
1. all strings read in from files are in a consistent case
2. step 2 GET routes should have the same sort order as in step 1 (I wanted to clarify this because the instructions
   do not explicitly state secondary sorting criteria for gender or ascending vs.descending, etc...)
3. file types have accurate file extensions


