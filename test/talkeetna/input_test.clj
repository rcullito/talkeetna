(ns talkeetna.input-test
  (:require [clojure.test :refer :all]
            [talkeetna.input :as i]))


(deftest split-comma
  (testing "that splitting an input string by a comma regex produces a vector of the expected length"
    (let [result (i/split-person i/comma-delimiter "Murphy, Colin, Male")]
      (is (true? (vector? result)))
      (is (= 3 (count result))))))












