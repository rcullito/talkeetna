(ns talkeetna.core-test
  (:require [clojure.test :refer :all]
            [talkeetna.core :refer :all]
            [talkeetna.input :refer :all]))


(deftest sort-multipe-criteria
  (testing "that sorting on multiple criteria works as expected"
    (let [people      '({:gender "male" :last-name "jefferson"}
                        {:gender "female" :last-name "smith"}
                        {:gender "female" :last-name "adams"})
          select-name (select-entity people sort-gender-last-name :last-name)]
      (is (= "adams" (select-name first)))
      (is (= "smith" (select-name second)))
      (is (= "jefferson" (select-name last))))))





(deftest entire-pipeline
  (testing "that step 1 of the requirements are met and that supplied filename results in 3 different views of output, all of which contain records"
    (let [{:keys [:output-1 :output-2 :output-3]} (step-1 "people.csv")]
      ;; make sure there are some valid records in each file
      (is (true? (every? #(> (count %) 3) [output-1 output-2 output-3]))))))
