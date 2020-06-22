(defproject gr "0.1.0-SNAPSHOT"
  :description "Journey to the Far North"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-time "0.14.2"]
                 [compojure "1.6.0"]
                 [cheshire "5.8.0"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-mock "0.3.2"]]
  :plugins [[lein-cloverage "1.0.10"]
            [lein-ring "0.7.1"]]
  :ring {:handler talkeetna.handler/app}
  :main talkeetna.core
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.0"]]}})
