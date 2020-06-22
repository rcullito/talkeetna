(ns talkeetna.handler-utils
  (:require [talkeetna.core :as c]))

;; utility fn that will help seed the API with data

(defn build-records [filenames]
  "takes a list of filenames and builds and in-memory seq of the data"
  (reduce (fn [acc filename] (into acc (c/parse-file filename)))
          (list)
          filenames))
