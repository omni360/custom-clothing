(ns com.nerdboy.custom-clothing.utils
  (:require [clojure.reflect :as r]
            [clojure.pprint :as pp]))

(defn format-name [name]
  (format "@modId@_%s" name))

(defn add-if [pred & numbers]
  (if (pred)
    (apply + numbers)
    (first numbers)))

(defn print-methods [obj]
  (pp/print-table
    (sort-by :name
             (filter :exception-types (:members (r/reflect obj))))))
