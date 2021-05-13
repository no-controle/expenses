(ns expenses.db.purchases
  (:require [clojure.string :as string]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            [io.pedestal.log :as log]
            [expenses.logic.db-helper :as db-helper]))

(def purchases-collection "purchases")

(defn create-id [title date amount]
  (str (hash [title date amount])))

(defn create-purchase
  [purchase db]
  (->> purchase
       db-helper/add-id-and-created-at
       db-helper/add-updated-at
       (mc/insert-and-return db purchases-collection)))

(defn update-purchase [id value db]
  (->> value
       db-helper/add-updated-at
       (mc/update-by-id db purchases-collection id)))

(defn create-purchases-list
  [purchases-list db]
  (try
    (->> purchases-list
         (map #(->> %
                    db-helper/add-id-and-created-at
                    db-helper/add-updated-at))
         (mc/insert-batch db purchases-collection))
    (catch Exception ex
      (log/error :exception ex))))

(defn get-by-period
  [period db]
  (mc/find-maps db purchases-collection {:bill-date period}))

(defn search-purchase-with [search-parameters db]
  (mc/find-one-as-map db purchases-collection search-parameters))


(defn search-similar-purchases-for [title db]
  (let [title-without-installments (second (re-matches #"(.*) (.*/.*)" title))
        search-results (->> {:title {$regex (str title-without-installments ".*")}}
                            (mc/find-maps db purchases-collection))
        final (filter #(string/starts-with? (:title %) title-without-installments) search-results)]
    (clojure.pprint/pprint "db.search-similar-purchases-for")
    (clojure.pprint/pprint "search-results")
    (clojure.pprint/pprint search-results)
    (clojure.pprint/pprint "title-without-installments")
    (clojure.pprint/pprint title-without-installments)
    (clojure.pprint/pprint "final")
    (clojure.pprint/pprint final)
    final))

(defn search-purchase-in-category-with [category-list search-parameters db]
  (mc/find-maps db purchases-collection (merge search-parameters
                                               {:refunded {$ne true}
                                                :category {$regex (string/join "|" category-list) $options "i"}})))

(defn search-purchase-not-in-category-with [category-list search-parameters db]
  (mc/find-maps db purchases-collection (merge search-parameters
                                               {:refunded {$ne true}
                                                :category {$not {$regex (string/join "|" category-list) $options "i"}}})))
(def search-results [{:title "target", :amount 10}
                     {:title "target 2/2", :amount 20}
                     {:title "the amazing target", :amount 30}
                     {:title "look at this target now", :amount 40}])