(ns gr.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [compojure.handler :as handler]
            [gr.core :as core]))

(defn content->delimiter [person]
  "examine the input and determine the delimiter"
  (cond
    ;; spaces may be present in pipe and comma delimited files
    ;; so check for these first
    (re-find core/pipe-delimiter person) core/pipe-delimiter
    (re-find core/comma-delimiter person) core/comma-delimiter
    :else core/space-delimiter))
;; records via POST route will be stored in memory
(def posted-records (atom ()))

(defn process-posted-person [person]
  "perform operations from core on a single posted record and store in atom"
  (-> (content->delimiter person) 
      (core/split-person person)
      core/trim-person
      core/namevec->map
      (->> (swap! posted-records conj))))
;; assemble all records on file to be used by the GET routes
;; since filename is not supplied in the request
(def file-records
  (reduce (fn [acc [f d]] (into acc (core/parse-file f d)))
          (list)
          [["people.csv" core/comma-delimiter]
           ["people.ssv" core/space-delimiter]
           ["people.psv" core/pipe-delimiter]]))

(defn total-records []
  "HTTP GETS should return stored results on file and POSTed records held in memory. this should update on every request"
  (into @posted-records file-records))

(defroutes app-routes
  (GET "/records/gender" []
       (core/sort-gender-last-name (total-records)))
  (GET "/records/birthdate" []
       (core/sort-dob (total-records)))
  (GET "/records/name" []
       (core/sort-last-name-desc (total-records)))
  (POST "/records" request
        (process-posted-person (get-in request [:body :person])))
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
       (middleware/wrap-json-body {:keywords? true})
       (middleware/wrap-json-response)))
