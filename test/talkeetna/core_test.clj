(ns talkeetna.core-test
  (:require [clojure.test :refer :all]
            [talkeetna.core :refer :all]))

(deftest entire-pipeline
  (testing "that the cli portion of the app can read a file and output 3 sort orders"
    (let [{:keys [:gender-last-name
                  :birth-date-ascending
                  :last-name-descending]} (-main "people.psv")]
      (is (= "Claire" (-> gender-last-name
                       first
                       :first-name)))
      (is (= "Andreea" (-> birth-date-ascending
                       first
                       :first-name)))
      (is (= "Levon" (-> last-name-descending
                       last
                       :first-name))))))
