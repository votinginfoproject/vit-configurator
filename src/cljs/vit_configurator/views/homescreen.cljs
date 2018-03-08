(ns vit-configurator.views.homescreen
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]))

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

(defn input-area [current-value max-length event textarea-config]
  (let [diff (- max-length (count @current-value))
        border (when (neg? diff)
                 {:class "border-danger"})
        countdown-color (when (neg? diff)
                          {:class "text-danger"})
        change-fn (fn [evt]
                    (let [val (.. evt -target -value)]
                      (reset! current-value val)
                      (when (>= (- max-length (count val)) 0)
                        (re-frame/dispatch [event val]))))]
    [:div
     [:textarea.w-100 (merge textarea-config
                             (when-let [val @current-value]
                               {:value val})
                             (when border
                               border)
                             {:on-change change-fn})]
     [:p.small countdown-color
      (if (>= diff 0)
        (str "(" diff " characters remaining)")
        (str "(" (Math/abs diff) " characters over)"))]]))

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
