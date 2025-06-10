(ns clojure-automated-tests.logic
  (:require [clojure-automated-tests.models :as m]
            [schema.core :as s]))

(s/defn fits-queue?
  [{:keys [departments]} :- m/Hospital
   department :- m/Departments]
  (boolean (some-> (department departments)
                   count
                   (< 5))))

(s/defn arrives-at :- m/Hospital
  [hospital :- m/Hospital
   department :- m/Departments
   person :- s/Num]
  (if (fits-queue? hospital department)
    (update-in hospital [:departments department] conj person)
    hospital))
