(ns talkeetna.handler-utils-test
  (:require [talkeetna.handler-utils :as hu]
            [clojure.test :refer :all]))


(deftest build-records-from-files
  (testing "that we have working in-memory data for the API"
    (let [records (hu/build-records '("people.csv" "people.ssv" "people.psv"))]
        (is (= 14 (count records))))))
