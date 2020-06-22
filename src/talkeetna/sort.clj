(ns talkeetna.sort
  (:require [clj-time.format :as f]))

;; general sorting reminders
;; keep in mind that each key supplied to sort-by is really acting as a keyfn
;; in this case, it is just getting us a list of values for a particular field
;; which we then sort by :)

;; juxt returns the results of its functions in a vector

(def date-formatter (f/formatter "MM/dd/YYYY"))

(defn date-util [date]
  "takes date and returns a DateTime instance"
  (f/parse date-formatter date))

(defn sort-dob [data]
  "sort by dob ascending"
  (sort-by #(date-util (:dob %)) data))

(defn sort-last-name-desc [data]
  "sort by last name, descending"
  (reverse (sort-by :last-name data)))

(defn sort-gender-last-name [data]
  "sort by gender (females before males), last name ascending"
  (sort-by (juxt :gender :last-name) data))


