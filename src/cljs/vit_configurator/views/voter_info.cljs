(ns vit-configurator.views.voter-info
  (:require [re-frame.core :as re-frame]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]))

(defn customizer
  []
  (let [voter-info @(re-frame/subscribe [::subs/voter-info])]
    [:div
     [:p.mb-2 "Set up a message that users will see before their results. This is
               a simpler form of the Election Notice that is forthcoming in the 5.2
               specification, and can be used before that is available or if your
               data is in an earlier specification format."]
     [:input {:type "text" :name "voter-info" :value voter-info
              :on-change (fn [evt]
                           (let [val (.. evt -target -value)]
                             (re-frame/dispatch [::events/set-voter-info val])))}]]))
