(ns vit-configurator.views.alert
  (:require [re-frame.core :as re-frame]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]))

(defn customizer
  []
  (let [alert (:en @(re-frame/subscribe [::subs/alert]))]
    [:div
     [:p.mb-1 "Set up a message that users will see before their results."]
     [:input {:type "text" :name "alert" :value alert
              :on-change (fn [evt]
                           (let [val (.. evt -target -value)]
                             (re-frame/dispatch [::events/set-alert val])))}]]))
