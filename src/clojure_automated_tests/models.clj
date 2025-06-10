(ns clojure-automated-tests.models
  (:require [schema.core :as s]))

(s/defschema Departments (s/enum :cardiology :neurology :pediatrics))

(s/defschema Department {Departments [s/Num]})

(s/defschema Hospital {(s/required-key :departments) Department})
