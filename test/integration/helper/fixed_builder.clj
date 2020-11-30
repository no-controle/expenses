(ns integration.helper.fixed-builder
  (:require [integration.helper.common :as helper]))

(defn create-data []
  (helper/do-post-request "/expenses/fixed" {:title  "Rent"
                                             :amount 1500
                                             :source "cash"})
  (helper/do-post-request "/expenses/fixed" {:title  "Internet provider"
                                             :amount 200
                                             :source "cash"})
  (helper/do-post-request "/expenses/fixed" {:title  "Netflix"
                                             :amount 35
                                             :source "credit card"})
  (helper/do-post-request "/expenses/fixed" {:title  "Spotify"
                                             :amount 25
                                             :source "credit card"})
  (helper/do-post-request "/expenses/fixed" {:title  "Electric Company"
                                             :amount 150
                                             :source "cash"}))