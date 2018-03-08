(ns vit-configurator.views.official
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]))

(def official-text
  "Do you want to return only official state data
from the Google Civic Information API?")

(defn customizer
  []
  (let [official @(re-frame/subscribe [::subs/official-data-only])]
    [:div
     [:p.mb-1 official-text]
     [:input (merge {:type "radio" :name "official" :value "true"
                     :on-change #(re-frame/dispatch
                                  [::events/set-official-data-only true])}
                    (if (= true official)
                      {:checked true}
                      {:checked false}))]
     [:label.pl-1.pr-4 "Yes"]
     [:input (merge {:type "radio" :name "official" :value "false"
                     :on-change #(re-frame/dispatch
                                  [::events/set-official-data-only false])}
                    (if (= false official)
                      {:checked true}
                      {:checked false}))]
     [:label.pl-1 "No"]]))
