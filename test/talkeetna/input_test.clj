(ns talkeetna.input-test
  (:require [clojure.test :refer :all]
            [talkeetna.input :as i]))


(deftest split-comma
  (testing "that splitting a record by a comma regex produces a vector of the expected length"
    (let [result (i/split-record i/comma-delimiter "Murphy, Colin, Male")]
      (is (true? (vector? result)))
      (is (= 3 (count result))))))


(deftest split-space
  (testing "that splitting a record by a space produces a vector of the expected length"
    (let [result (i/split-record i/space-delimiter "Murphy Colin Male")]
      (is (true? (vector? result)))
      (is (= 3 (count result))))))


(deftest split-pipe
  (testing "that splitting a record with a pipe produces a vector of the expected length"
    (let [result (i/split-record i/pipe-delimiter "Murphy | Colin | Male")]
      (is (true? (vector? result)))
      (is (= 3 (count result))))))


(deftest use-correct-delimeter
  (testing "that using different delimiters on the same record can produce vectors of different lengths")
  (let [record "Murphy | Colin | Male"
        pipe-result (i/split-record i/pipe-delimiter record)
        space-result (i/split-record i/space-delimiter record)]
    (is (> (count space-result) (count pipe-result)))))
