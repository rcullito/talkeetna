(ns talkeetna.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [compojure.handler :as handler]
            [talkeetna.core :as c]
            [talkeetna.input :as i]))


;; records via POST route will be stored in memory
(def posted-records (atom ()))

(defn process-posted-person [person]
  "perform operations from core on a single posted record and store in atom"
  (-> (content->delimiter person)
      ;; TODO we should not have to do all of these here in the handler
      ;; this should delegate to core
      (i/split-record person)
      i/trim-record
      i/namevec->map
      (->> (swap! posted-records conj))))

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
