(ns talkeetna.handler-test
  (:require [clojure.test :refer :all]
            [talkeetna.handler :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as cheshire]
            [talkeetna.input :as i]))
;; there is a bit of additonal logic in `gr.handler` specific to the routes
;; so this file consists of both traditional unit tests as well as testing of the
;; handler itself. it seemed best to keep tests correlated to namespaces
(deftest delimiter-from-content
  (testing "that we can determine the delimiter from examining input rather than just file type"
    (let [stein   "Stein, David, Male, Blue, 02/08/1987"
          ariadne "Delius Ariadne Female Blue 12/03/1986"
          cobb    "Cobb| Dominic| Male| Blue| 04/16/1975"]
      (is (= i/comma-delimiter (content->delimiter stein)))
      (is (= i/space-delimiter (content->delimiter ariadne)))
      (is (= i/pipe-delimiter (content->delimiter cobb))))))

(deftest test-app
  (testing "get records"
    (let [response (app (mock/request :get "/records/name"))]
      (is (= (:status response) 200))
      ;; make sure there are some valid records being returned
      (is (> (count (cheshire/parse-string (:body response))) 3))))
  (testing "post record"
    (let [post-data {:person "Aaberg, Chelsea, Female, Blue, 02/08/1997"}
          response (app (-> (mock/request :post "/records")
                            (mock/content-type "application/json")
                            ;; mock/json-body not working properly so using chesire to aid                            
                            (mock/body (cheshire/generate-string post-data))))]
      (is (= (:status response) 200))
      (is (= "Chelsea" (-> response
                           :body
                           ;; can rely on this being first as the most recently conj'd entry
                           cheshire/parse-string
                           first
                           (get "first-name"))))))
  (testing "not-found"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
