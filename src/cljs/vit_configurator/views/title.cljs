(ns vit-configurator.views.title
  (:require [re-frame.core :as re-frame]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]))

(defn customizer
  []
  (let [title (:en @(re-frame/subscribe [::subs/title]))]
    [:div
     [:p.mb-1 "Provide a title for the Voting Information Tool"]
     [:input {:type "text" :name "title" :value title
              :on-change (fn [evt]
                           (let [val (.. evt -target -value)]
                             (re-frame/dispatch [::events/set-title val])))}]]))
