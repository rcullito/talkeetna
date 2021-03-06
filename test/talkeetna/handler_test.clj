(ns talkeetna.handler-test
  (:require [clojure.test :refer :all]
            [talkeetna.handler :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as cheshire]
            [talkeetna.input :as i]))

(deftest test-app
  (testing "post should accept data with a single key: 'person'"
    (let [post-data {:person "Aaberg, Chelsea, Female, Blue, 02/08/1987"}
          response (app (-> (mock/request :post "/records")
                            (mock/content-type "application/json")
                            (mock/body (cheshire/generate-string post-data))))]
      (is (= 200 (:status response)))))
  (testing "improperly formatted post throws an error"
    (let [invalid-post-data {:martian "Aaberg, Hillary, Female, Blue, 02/08/1987"}
          response (app (-> (mock/request :post "/records")
                            (mock/content-type "application/json")
                            (mock/body (cheshire/generate-string invalid-post-data))))]
      (is (= 400 (:status response)))))
  (testing "get should return multiple records from disk as well as any record that has been posted"
    (let [response (app (mock/request :get "/records/gender"))
          returned-data (cheshire/parse-string (:body response))]
      (is (= (:status response) 200))
      (is (> (count returned-data) 3))
      ;; check that we have the record we just posted
      (is (some? (some #(= "Chelsea" (get % "first-name")) returned-data)))))
  (testing "not-found"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
