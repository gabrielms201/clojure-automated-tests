(ns clojure-automated-tests.logic-test
  (:require [clojure-automated-tests.logic :refer :all]
            [clojure.test :refer :all]
            [schema.core :as s]))

(s/set-fn-validation! true)
(declare thrown-with-msg?)

(deftest fits-queue-test
  (testing "It should be able to tell that that an empty queue fits a new person"
    (let [hospital   {:departments {:cardiology []}}
          department :cardiology]
      (is (= true (fits-queue? hospital department)))))

  (testing "It should be able to tell that that an filled queue doesn't fit a new person"
    (let [hospital   {:departments {:cardiology [1 2 3 4 5 6]}}
          department :cardiology]
      (is (= false (fits-queue? hospital department)))))

  (testing "It should be able to tell that that an filled with 5 people queue doesn't fit a new person"
    (let [hospital   {:departments {:cardiology [1 2 3 4 5]}}
          department :cardiology]
      (is (= false (fits-queue? hospital department)))))

  (testing "It should throw schema error when provided invalid hospital"
    (let [hospital   {:invalid-keyword {:cardiology [1]}}
          department :cardiology]
      (is (thrown-with-msg? Exception #"does not match schema*" (fits-queue? hospital department)))))

  (testing "It should throw schema error when provided invalid department"
    (let [hospital   {:departments {:invalid-department [1]}}
          department :cardiology]
      (is (thrown-with-msg? Exception #"does not match schema*" (fits-queue? hospital department)))))

  (testing "It should be able to tell that that an nil queue doesn't fit a new person"
    (let [hospital   {:departments {:cardiology nil}}
          department :cardiology]
      (is (= false (fits-queue? hospital department))))))


(deftest arrives-at-test
  (testing "It should be able to insert a new person at the queue if is not empty"
    (let [hospital        {:departments {:cardiology [1 2 3]}}
          expected-output {:departments {:cardiology [1 2 3 5]}}]
      (is (= expected-output (arrives-at hospital :cardiology 5)))))

  (testing "It should NOT be able to insert a new person at the queue if is full"
    (doseq [{:keys [description input-hospital expected-output person-to-add]}
            [{:description     "Can't add when reach five people"
              :input-hospital  {:departments {:cardiology [1 2 3 4 5]}}
              :expected-output {:departments {:cardiology [1 2 3 4 5]}}
              :person-to-add   6}

             {:description     "Can't add when reach MORE than five people"
              :input-hospital  {:departments {:cardiology [1 2 3 4 5 6]}}
              :expected-output {:departments {:cardiology [1 2 3 4 5 6]}}
              :person-to-add   7}]]
      (testing description
        (is (= expected-output (arrives-at input-hospital :cardiology person-to-add))))))

  (testing "It should NOT be able to insert a new person at the queue if is null"
    (let [hospital        {:departments {:cardiology nil}}
          expected-output {:departments {:cardiology nil}}]
      (is (= expected-output (arrives-at hospital :cardiology 6))))))

