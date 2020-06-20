# gr

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

As there was no stated requirement for what the POST route should return, it seemed most helpful to return 
the current records stored in-memory (though perhaps just returning the individual resource would be more idiomatic)

## assumptions
1. all records are of a consistent case and format (whitespace is explicitly tackled as it seems tied to working with
   delimiters but different cases and other variations among nonstandardized input struck me as outside the
   scope of this exercise)
2. step 2 GET routes should have the same sort order as in step 1 (I wanted to clarify this because the instructions
   do not explicitly state secondary sorting criteria for gender or ascending vs.descending, for instance) 
3. file types have known delimiters via file extensions, while input via POST is unkown (both are accounted for,
   just at different places in the repo).

