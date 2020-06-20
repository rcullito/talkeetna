(ns talkeetna.input-test
  (:require [clojure.test :refer :all]
            [talkeetna.input :as i]))


;; TODO run this before moving on
(deftest split-comma
  (testing "that splitting a row by comma produces a..."
    (let [result (i/split-person i/comma-delimiter "Murphy, Colin, Male")]
      (is (= result ["Murphy" "Colin" "Male"])))))












