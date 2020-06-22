(ns talkeetna.sort-test
  (:require [talkeetna.sort :as so]
            [clojure.test :refer :all]))


(deftest date-type
  (testing "that our date util fn returns a DateTime instance"
    (let [converted-date (so/date-util "09/18/1988")
          type-of-date (type converted-date)]
        (is (= org.joda.time.DateTime type-of-date)))))

(deftest sort-dates
  (testing "that dates of birth are correctly sorted by their actual time - not just based on the string representation"
    (let [dates        '({:dob "10/18/1989"} {:dob "03/12/1989"} {:dob "02/08/2002"})
          sorted-dates (so/sort-dob dates)]
      (is (= "03/12/1989" (-> sorted-dates
                              first
                              :dob))))))

(deftest sort-descending
  (testing "that descending sort works"
    (let [last-names        '({:last-name "Smith"}
                              {:last-name "Jones"}
                              {:last-name "Perler"})
          sorted-last-names (so/sort-last-name-desc last-names)]
      (is (= "Smith" (-> sorted-last-names
                         first
                         :last-name))))))

(deftest sort-multipe-criteria
  (testing "that sorting on multiple criteria works as expected"
    (let [people        '({:gender "male" :last-name "jefferson"}
                          {:gender "female" :last-name "smith"}
                          {:gender "female" :last-name "adams"})
          sorted-people (so/sort-gender-last-name people)]
      (is (= "adams" (:last-name (first sorted-people))))
      (is (= "jefferson" (:last-name (last sorted-people)))))))
