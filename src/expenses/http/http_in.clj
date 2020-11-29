(ns expenses.http.http-in
  (:require [expenses.adapter.purchases :as adapter.purchases]
            [expenses.controller.purchases :as controller.purchases]
            [expenses.controller.revenue :as controller.revenue]
            [expenses.controller.fixed :as controller.fixed]
            [expenses.controller.monthly :as controller.monthly]))

(defn create-fixed-expense
  [{:keys [json-params db]}]
  {:status 200
   :body   (-> json-params
               (controller.fixed/create-fixed db))})

(defn delete-fixed-expense
  [{:keys [path-params db]}]
  {:status 200
   :body   (-> path-params
               :id
               (controller.fixed/delete-fixed db))})

(defn create-revenue
  [{:keys [json-params db]}]
  {:status 200
   :body (-> json-params
             (controller.revenue/create-revenue db))})

(defn delete-revenue
  [{:keys [path-params db]}]
  {:status 200
   :body   (-> path-params
               :id
               (controller.revenue/delete-revenue db))})

(defn get-purchases
  [{:keys [query-params db]}]
  {:status 200
   :body   (controller.purchases/get-by-period (:year query-params) (:month query-params) db)})

(defn create-purchase
  [{:keys [json-params db]}]
  {:status 200
   :body   (-> json-params
               adapter.purchases/http-in->purchase
               (controller.purchases/create-purchase db))})

(defn refund-purchase
  [{:keys [path-params db]}]
  {:status 200
   :body (-> path-params
             :id
             (controller.purchases/refund-purchase db))})

(defn get-purchases-summary
  [{:keys [query-params db]}]
  {:status 200
   :body   (controller.purchases/get-summary query-params db)})

(defn create-purchases-batch
  [{:keys [json-params db]}]
  {:status 200
   :body   (-> json-params
               adapter.purchases/http-in->purchases-list
               (controller.purchases/create-purchases-list db))})

(defn monthly-data-for-period
  [{:keys [query-params db]}]
  {:status 200
   :body (controller.monthly/data-for-period (:year query-params) (:month query-params) db)})


(defn variable-purchases-for-period
  [{:keys [query-params db]}]
  {:status 200
   :body (controller.purchases/variable-for-period (:year query-params) (:month query-params) db)})

(defn other-purchases-for-period
  [{:keys [query-params db]}]
  {:status 200
   :body (controller.purchases/other-for-period (:year query-params) (:month query-params) db)})
