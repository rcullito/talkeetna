(ns talkeetna.core-test
  (:require [clojure.test :refer :all]
            [talkeetna.core :refer :all]
            [talkeetna.input :refer :all]))


(deftest splitting
  (testing "that splitting on delimeter works as expected"
    (let [comma-person-string "Murphy, Colin, Male, Yellow, 10/18/1989"
          space-person-string "Balayan Levon Male Orange 08/12/1985"
          pipe-person-string  "Ericson |  Berrit |  Female |  Black |  04/14/1987"
          comma-result        (split-person comma-delimiter comma-person-string)
          space-result        (split-person space-delimiter space-person-string)
          pipe-result         (split-person pipe-delimiter pipe-person-string)]
      (is (true? (vector? comma-result)))
      (is (true? (vector? space-result)))
      (is (true? (vector? pipe-result)))
      (is (= 5 (count comma-result)))
      (is (= 5 (count space-result)))
      (is (= 5 (count pipe-result))))))


(deftest trimming
  (testing "that trimming whitespace is effective")
  (let [whitespace        #"\s"
        whitespace-person ["Ericson  " " Berrit" " Female" "  Black" "04/14/1987 "]
        trimmed-person    (trim-person whitespace-person)]
    (is true? (every? nil? (map #(re-find #"\s" %) trimmed-person)))))

(deftest file-reader
  (testing "that we can successfully read from disk and read into a list"
    (let [list-of-people (parse-file "people.csv" comma-delimiter)]
      (is true? (list? list-of-people))
      ;; make sure there are some valid records in each file
      (is (> (count list-of-people) 3)))))

(deftest build-map
  (testing "that namevec->map returns a map"
    (let [person-attributes ["culliton" "rob" "male" "green" "09/18/1986"]]
      (is (true? (map? (namevec->map person-attributes)))))))

(deftest delimeters
  (testing "that the right delimiter is chosen for a given file extension"
    (is (= comma-delimiter (select-delimiter "lions.csv")))
    (is (not= comma-delimiter (select-delimiter "cats.ssv")))
    (is (= space-delimiter (select-delimiter "cats.ssv")))
    (is (= pipe-delimiter (select-delimiter "frogs.psv")))))

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
