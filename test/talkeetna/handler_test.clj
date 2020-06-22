(ns talkeetna.handler-test
  (:require [clojure.test :refer :all]
            [talkeetna.handler :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as cheshire]
            [talkeetna.input :as i]))

(deftest test-app
  (testing "post record"
    (let [post-data {:person "Aaberg, Chelsea, Female, Blue, 02/08/1987"}
          response (app (-> (mock/request :post "/records")
                            (mock/content-type "application/json")
                            (mock/body (cheshire/generate-string post-data))))]
      (is (= (:status response) 200))))
  (testing "get records"
    (let [response (app (mock/request :get "/records/gender"))
          returned-data (cheshire/parse-string (:body response))]
      (is (= (:status response) 200))
      (is (> (count returned-data) 3))
      ;; check that we have the record we just posted
      (is (some? (some #(= "Chelsea" (get % "first-name")) returned-data)))))
  
  (testing "not-found"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
