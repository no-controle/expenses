(ns integration.helper.revenue-builder
  (:require [integration.helper.common :as helper]))

(defn create-data []
  (helper/do-post-request "/revenue" {:title     "Salary John"
                                      :amount    5000
                                      :recurrent true})
  (helper/do-post-request "/revenue" {:title     "Salary Jenny"
                                      :amount    7000
                                      :recurrent true})
  (helper/do-post-request "/revenue" {:title     "Video Clip"
                                      :amount    6300
                                      :recurrent false})
  (helper/do-post-request "/revenue" {:title     "Photo Shoot"
                                      :amount    700
                                      :recurrent false}))