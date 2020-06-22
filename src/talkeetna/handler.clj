(ns talkeetna.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [compojure.handler :as handler]
            [talkeetna.sort :as so]
            [talkeetna.input :as i]
            [talkeetna.handler-utils :as hu]))


;; records via POST route will be stored in memory
;; seed the 'working db' with values from our 3 delimited files
(def posted-records (-> '("people.csv" "people.ssv" "people.psv")
                        hu/build-records
                        ;; atom will give us some persistence from the POST to the subsequent GET requests
                        atom))

(defn process-posted-person [person]
  "perform similar set of operations as core's parse-file, though now just for one record and no file handling"
  (-> (i/select-delimiter-from-input person)
      (i/split-record person)
      i/trim-record
      i/namevec->map
      (->> (swap! posted-records conj))))

(defroutes app-routes
  (GET "/records/gender" []
       (so/sort-gender-last-name @posted-records))
  (GET "/records/birthdate" []
       (so/sort-dob @posted-records))
  (GET "/records/name" []
       (so/sort-last-name-desc @posted-records))
  (POST "/records" request
        (let [new-person (get-in request [:body :person])]
          (when (process-posted-person new-person)
            (str "Successfully posted: " new-person))))
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
       (middleware/wrap-json-body {:keywords? true})
       (middleware/wrap-json-response)))
