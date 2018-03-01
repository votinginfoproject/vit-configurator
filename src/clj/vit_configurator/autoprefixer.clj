(ns vit-configurator.autoprefixer
  (:require [clojure.java.io :as io])
  (:import [java.io File]))

(defn load-js [engine filename]
  (->> filename
       io/resource
       io/reader
       (.eval engine)))

(defn -main []
  (println "Autoprefixing CSS rules...")
  (let [nashorn (.getEngineByName (javax.script.ScriptEngineManager.) "nashorn")]
    (load-js nashorn "buildjs/polyfills.js")
    (load-js nashorn "buildjs/autoprefixer.js")
    (.eval nashorn "var process = autoprefixer.process;")
    (let [raw-css (slurp "target/css/site.css")
          autoprefixed-css (-> nashorn
                               (.invokeFunction "process"
                                                (into-array [raw-css]))
                               (.callMember "stringify" (into-array []))
                               (.get "css"))]
      (-> "resources/public/css/compiled"
          File.
          .mkdirs)
      (spit "resources/public/css/compiled/site.css" autoprefixed-css))))
