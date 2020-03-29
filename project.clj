(defproject expenses "0.0.1-SNAPSHOT"
  :description "API to help on monthly expenses control"
  :dependencies [[com.novemberain/monger "3.1.0"]
                 [io.pedestal/pedestal.jetty "0.5.7"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/data.json "0.2.6"]
                 [org.slf4j/slf4j-simple "1.7.30"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  :profiles {:dev     {:aliases      {"run-dev" ["trampoline" "run" "-m" "expenses.server/run-dev"]}
                       :dependencies [[io.pedestal/pedestal.service-tools "0.5.7"]
                                      [midje "1.9.9"]]
                       :plugins      [[lein-midje "3.2.1"]]}
             :uberjar {:aot [expenses.server]}}
  :main ^{:skip-aot true} expenses.server)
