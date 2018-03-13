(ns vit-configurator.views.homescreen
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]
            [vit-configurator.views.shared :as shared]))

(def title-text
  "Enter text below to customize the
title at the top of the home screen")

(def title-placeholder
  "Voting Info Project")

(def intro-text
  "Enter text below to customize the
intro message on the homescreen")

(def intro-placeholder
  "Everything you need to cast a ballot in your next election.")

(defn input-area [component-state max-length event textarea-config]
  (let [current-value @component-state
        diff (- max-length (count current-value))
        border (shared/border-class diff)
        change-fn (shared/max-length-change-fn
                   max-length component-state [event])]
    [:div
     [:textarea.w-100 (merge textarea-config
                             (when current-value
                               {:value current-value})
                             border
                             {:on-change change-fn})]
     [shared/countdown diff]]))

(defn customizer
  []
  (let [title (reagent/atom nil)
        intro (reagent/atom nil)]
    [:div
     [:p.mb-1 title-text]
     [input-area title 75 ::events/set-title
      {:rows "2" :placeholder title-placeholder}]
     [:p.pt-4.mb-1 intro-text]
     [input-area intro 100 ::events/set-intro
      {:rows "3" :placeholder intro-placeholder}]]))
