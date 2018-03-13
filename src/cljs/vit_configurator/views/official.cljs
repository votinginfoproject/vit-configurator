(ns vit-configurator.views.official
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]))

(def official-text
  "Do you want to return only official state data
from the Google Civic Information API?")

(defn radio-button [value current-value]
  (letfn [(change-fn [_]
            (re-frame/dispatch [::events/set-official-data-only
                                value]))]
    [:input (merge {:type "radio" :name "official" :value (str value)
                    :on-change change-fn}
                   (if (= value current-value)
                     {:checked true}
                     {:checked false}))]))

(defn customizer
  []
  (let [official @(re-frame/subscribe [::subs/official-data-only])]
    [:div
     [:p.mb-1 official-text]
     [radio-button true official]
     [:label.pl-1.pr-4 "Yes"]
     [radio-button false official]
     [:label.pl-1 "No"]]))
