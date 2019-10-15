(ns expenses.server
  (:gen-class)
  (:require [io.pedestal.http :as http]
            [io.pedestal.http :as server]
            [io.pedestal.http.route :as route]
            [expenses.service :as service]))

(def configuration {:env                :prod
                    ::http/routes        service/routes
                    ::http/resource-path "/public"
                    ::http/type          :jetty
                    ::http/port          8890})

(defonce runnable-service (server/create-server configuration))

(defn run-dev
  "The entry-point for 'lein run-dev'"
  [& args]
  (println "\nCreating your [DEV] server...")
  (-> configuration                                         ;; start with production configuration
      (merge {:env                     :dev
              ;; do not block thread that starts web server
              ::server/join?           false
              ;; Routes can be a function that resolve routes,
              ;;  we can use this to set the routes to be reloadable
              ::server/routes          #(route/expand-routes (deref #'service/routes))
              ;; all origins are allowed in dev mode
              ::server/allowed-origins {:creds true :allowed-origins (constantly true)}
              ;; Content Security Policy (CSP) is mostly turned off in dev mode
              ::server/secure-headers  {:content-security-policy-settings {:object-src "'none'"}}})
      ;; Wire up interceptor chains
      server/default-interceptors
      server/dev-interceptors
      server/create-server
      server/start))

(defn -main
  "The entry-point for 'lein run'"
  [& args]
  (println "\nCreating your server...")
  (server/start runnable-service))
