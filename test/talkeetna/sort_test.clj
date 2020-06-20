(ns talkeetna.sort-test
  (:require [talkeetna.sort :as so]
            [clojure.test :refer :all]))

(deftest sort-descending
  (testing "that descending sort works"
    (let [last-names        '({:last-name "Smith"}
                              {:last-name "Jones"}
                              {:last-name "Perler"})
          sorted-last-names (so/sort-last-name-desc last-names)]
      (is (= "Smith" (-> sorted-last-names
                         first
                         :last-name))))))



