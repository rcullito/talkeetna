(ns talkeetna.core-test
  (:require [clojure.test :refer :all]
            [talkeetna.core :refer :all]
            [talkeetna.input :refer :all]))








(defn select-entity [entities sort-fn attribute]
  "used to expedite test cases below where position varies but other args do not for a given sort test"
  (fn [position]
    (-> entities
        sort-fn
        position
        attribute)))

(deftest sort-multipe-criteria
  (testing "that sorting on multiple criteria works as expected"
    (let [people      '({:gender "male" :last-name "jefferson"}
                        {:gender "female" :last-name "smith"}
                        {:gender "female" :last-name "adams"})
          select-name (select-entity people sort-gender-last-name :last-name)]
      (is (= "adams" (select-name first)))
      (is (= "smith" (select-name second)))
      (is (= "jefferson" (select-name last))))))

(deftest sort-dates
  (testing "that dates of birth are correctly sorted by their actual time - not just based on the string representation"
    (let [dates       '({:dob "10/18/1989"} {:dob "03/12/1989"} {:dob "02/08/1977"})
          select-date (select-entity dates sort-dob :dob)]
      (is (= "02/08/1977" (select-date first)))
      (is (= "03/12/1989" (select-date second)))
      (is (= "10/18/1989" (select-date last))))))

(deftest sort-descending
  (testing "that descending sort works"
    (let [names       '({:last-name "Consiglio"}
                        {:last-name "Sassoon"}
                        {:last-name "Mosenthal"})
          select-name (select-entity names sort-last-name-desc :last-name)]
      (is (= "Sassoon" (select-name first)))
      (is (= "Mosenthal" (select-name second)))
      (is (= "Consiglio" (select-name last))))))

(deftest entire-pipeline
  (testing "that step 1 of the requirements are met and that supplied filename results in 3 different views of output, all of which contain records"
    (let [{:keys [:output-1 :output-2 :output-3]} (step-1 "people.csv")]
      ;; make sure there are some valid records in each file
      (is (true? (every? #(> (count %) 3) [output-1 output-2 output-3]))))))
