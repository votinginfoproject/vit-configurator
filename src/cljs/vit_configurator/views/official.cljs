(ns vit-configurator.views.official
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]))

(def official-text
  "Set this parameter to True to return only official state data from the Google Civic Information API. [NOTE: This may result in gaps in coverage due to jurisdictions without official data.] Set to False to return official AND unofficial data.")

(defn radio-button [value current-value]
  (letfn [(change-fn [_]
            (re-frame/dispatch [::events/set-official-only
                                value]))]
    [:input (merge {:type "radio" :name "official" :value (str value)
                    :on-change change-fn}
                   (if (= value current-value)
                     {:checked true}
                     {:checked false}))]))

(defn customizer
  []
  (let [official @(re-frame/subscribe [::subs/official-only])]
    [:div
     [:p.mb-2 official-text]
     [radio-button true official]
     [:label.pl-1.pr-4 "True"]
     [radio-button false official]
     [:label.pl-1 "False"]]))
