(defproject vit-configurator "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.908"]
                 [reagent "0.7.0"]
                 [re-frame "0.10.5"]]

  :plugins [[lein-auto "0.1.3"]
            [lein-cljsbuild "1.1.5"]
            [deraen/lein-sass4clj "0.3.1"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :sass {:source-paths ["src/sass"]
         :target-path "target/css"
         :output-style :compressed}

  :auto {"autoprefix" {:paths ["target/css"]
                       :file-pattern #"site\.css$"}}

  :aliases {"autoprefix" ["run" "-m" "vit-configurator.autoprefixer"]
            "css" ["pdo"
                   ["sass4clj" "auto"]
                   ["auto" "autoprefix"]]
            "dev" ["do" "clean"
                   ["pdo"
                    ["figwheel" "dev"]
                    ["sass4clj" "auto"]
                    ["auto" "autoprefix"]]]
            "build" ["do" "clean"
                     ["cljsbuild" "once" "min"]
                     ["sass4clj" "once"]
                     ["autoprefix"]]}

  :profiles
  {:dev-overrides {}
   :dev
   [:dev-overrides
    {:dependencies [[binaryage/devtools "0.9.4"]
                    [figwheel-sidecar "0.5.13"]
                    [com.cemerick/piggieback "0.2.2"]]

     :plugins      [[lein-figwheel "0.5.13"]
                    [lein-pdo "0.1.1"]]}]}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "vit-configurator.core/mount-root"}
     :compiler     {:main                 vit-configurator.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}
                    }}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            vit-configurator.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}
    ]})
