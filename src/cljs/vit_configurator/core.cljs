(ns vit-configurator.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [vit-configurator.events :as events]
            [vit-configurator.views :as views]
            [vit-configurator.config :as config]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn handshake
  "An event handler that gets fired by the preview iframe posting
   a message to the parent window. This lets us store the DOM element
   for the iframe, which can then be used to post messages back to it
   in a secure manner, such as for loading a new configuration."
  [evt]
  (re-frame/dispatch [::events/preview-ready (.-source evt)]))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (.addEventListener js/window "message" handshake)
  (mount-root))
