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

by design, it will also print the output from the cli portion of the app

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

## about the codebase

The `-main` function in `/src/core.clj` is a good starting point for examining the codebase. `-main` relies on two other functions: `parse-file` and `assemble-sorts`. Both of these are composed of helpers from `input.clj` & `sort.clj`, respectively.

`handler.clj` drives the REST API, pulling from `input.clj` & `sort.clj` as needed. During refactoring, `handler_utils.clj` was created to build up some sample data for the api from the records already on the filesystem.


## about the routes

Beyond just parroting back the records from disk, each GET will also contain
any records that have been POSTed

This behavior can be tested by the user through POSTing a record and then
looking for it in the results. My favorite option was to choose a birthday in the
1800s and look for it at the top of the birthdate route.

an example request body for `POST /records` would be `{"person": "Aaberg, Chelsea, Female, Blue, 02/08/1897"}`


## assumptions
1. all strings read in from files are in a consistent case
2. step 2 GET routes should have the same sort order as in step 1 (I wanted to clarify this because the instructions
   do not explicitly state secondary sorting criteria for gender or ascending vs.descending, etc...)
3. files have accurate file extensions
4. logging and/or additional middleware in a production app considered to be outside the scope of this excercise

