(ns talkeetna.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [compojure.handler :as handler]
            [talkeetna.core :as c]
            [talkeetna.input :as i]))

(defn content->delimiter [person]
  "examine the input and determine the delimiter"
  (cond
    ;; spaces may be present in pipe and comma delimited files
    ;; so check for these first
    (re-find i/pipe-delimiter person) i/pipe-delimiter
    (re-find i/comma-delimiter person) i/comma-delimiter
    :else i/space-delimiter))
;; records via POST route will be stored in memory
(def posted-records (atom ()))

(defn process-posted-person [person]
  "perform operations from core on a single posted record and store in atom"
  (-> (content->delimiter person)
      ;; TODO we should not have to do all of these here in the handler
      ;; this should delegate to core
      (i/split-person person)
      i/trim-person
      i/namevec->map
      (->> (swap! posted-records conj))))
;; assemble all records on file to be used by the GET routes
;; since filename is not supplied in the request
(def file-records
  (reduce (fn [acc [f d]] (into acc (i/parse-file f d)))
          (list)
          [["people.csv" i/comma-delimiter]
           ["people.ssv" i/space-delimiter]
           ["people.psv" i/pipe-delimiter]]))

(defn total-records []
  "HTTP GETS should return stored results on file and POSTed records held in memory. this should update on every request"
  (into @posted-records file-records))

(defroutes app-routes
  (GET "/records/gender" []
       (c/sort-gender-last-name (total-records)))
  (GET "/records/birthdate" []
       (c/sort-dob (total-records)))
  (GET "/records/name" []
       (c/sort-last-name-desc (total-records)))
  (POST "/records" request
        (process-posted-person (get-in request [:body :person])))
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
       (middleware/wrap-json-body {:keywords? true})
       (middleware/wrap-json-response)))
