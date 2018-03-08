(ns vit-configurator.views.language
  (:require [re-frame.core :as re-frame]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]))

(def language-text
  "What language do you want to appear on your site?")

(def languages
  [[:en "English"]
   [:es "Spanish"]])

(defn option
  [[kw text]]
  [:option {:value kw :key (name kw)} text])

(defn customizer
  []
  (let [language @(re-frame/subscribe [::subs/language])]
    [:div
     [:p.mb-1 language-text]
     [:select.mb-2 {:value language
                    :on-change (fn [selection]
                                 (let [abbrev (-> selection
                                                  (.. -target -value)
                                                  keyword)]
                                   (re-frame/dispatch
                                    [::events/set-language abbrev])))}
      (map option languages)]]))
