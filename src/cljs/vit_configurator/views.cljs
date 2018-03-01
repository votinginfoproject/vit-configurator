(ns vit-configurator.views
  (:require [re-frame.core :as re-frame]
            [vit-configurator.subs :as subs]
            ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div "Hello from " @name]))
