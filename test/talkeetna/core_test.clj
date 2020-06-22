(ns talkeetna.core-test
  (:require [clojure.test :refer :all]
            [talkeetna.core :refer :all]))


(deftest entire-pipeline
  (testing "that step 1 of the requirements are met and that supplied filename results in 3 different views of output, all of which contain records"
    (let [{:keys [:output-1 :output-2 :output-3]} (step-1 "people.csv")]
      ;; make sure there are some valid records in each file
      (is (true? (every? #(> (count %) 3) [output-1 output-2 output-3]))))))
