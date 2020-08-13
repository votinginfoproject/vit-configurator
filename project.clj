(defproject vit-configurator "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.8"]]

  :plugins [[lein-auto "0.1.3"]
            [lein-cljsbuild "1.1.7"]
            [deraen/lein-sass4clj "0.3.1"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]
             :server-port 3450}

  :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}

  :sass {:source-paths ["src/sass"]
         :target-path "target/css"
         :output-style :compressed}

  :auto {"autoprefix" {:paths ["target/css"]
                       :file-pattern #"site\.css$"}}

  :doo {:build "test"
        :paths {:karma "env TZ=UTC node_modules/.bin/karma"}}

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
                     ["autoprefix"]]
            "test" ["doo" "firefox-headless" "test" "once"]
            "chrome-test" ["doo" "chrome-headless" "test" "once"]}

  :profiles
  {:dev-overrides {}
   :dev
   [:dev-overrides
    {:dependencies [[binaryage/devtools "0.9.10"]
                    [figwheel-sidecar "0.5.19"]
                    [cider/piggieback "0.4.1"]]

     :plugins      [[lein-figwheel "0.5.19"]
                    [lein-doo "0.1.11"]
                    [lein-pdo "0.1.1"]]}]}

  :cljsbuild
  {:builds
   {:dev
    {:source-paths ["src/cljs"]
     :figwheel     {:on-jsload "vit-configurator.core/mount-root"}
     :compiler     {:main                 vit-configurator.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}}}

    :min
    {:source-paths ["src/cljs"]
     :compiler     {:main            vit-configurator.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    :test
    {:source-paths ["src/cljs" "test/cljs"]
     :compiler     {:main          vit-configurator.runner
                    :output-to     "resources/public/js/compiled/test.js"
                    :output-dir    "resources/public/js/compiled/test/out"
                    :optimizations :none}}}})
